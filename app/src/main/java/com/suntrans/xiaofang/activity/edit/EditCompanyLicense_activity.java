package com.suntrans.xiaofang.activity.edit;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.activity.others.Search_license_activity;
import com.suntrans.xiaofang.model.company.AddCompanyResult;
import com.suntrans.xiaofang.model.company.CompanyLicenseInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.suntrans.xiaofang.utils.Utils.pad;

/**
 * Created by Looney on 2017/2/24.
 */

public class EditCompanyLicense_activity extends BasedActivity {
    @BindView(R.id.bt_banding)
    Button btBanding;
    @BindView(R.id.banding)
    RelativeLayout banding;

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.addr)
    TextView addr;

    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    @BindView(R.id.disattach)
    TextView disattach;
    @BindView(R.id.ll_projectinfo)
    LinearLayout llProjectinfo;
    @BindView(R.id.cmyname)
    TextView cmyname;
    private Toolbar toolbar;


    int mYear;
    int mMonth;
    int mDay;
    private String companyId;


    private int companyType;
    private CompanyLicenseInfo.License projectInfo = null;
    private ArrayList<CompanyLicenseInfo.CompanyLicenseItem> comLicenseLists = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcompanylicense);
        ButterKnife.bind(this);
        setupToolBar();
        initView();
    }

    private void setupToolBar() {
        companyType = getIntent().getIntExtra("type", MarkerHelper.S0CIETY);
        projectInfo = getIntent().getParcelableExtra("projectInfo");
        companyId = getIntent().getStringExtra("companyID");

        LogUtil.i("EditCompanyLicenseActivity","社会单位类型:"+companyType);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (companyType == MarkerHelper.S0CIETY)
            toolbar.setTitle(R.string.title_modify_company_license);
        else if (companyType == MarkerHelper.COMMONCOMPANY)
            toolbar.setTitle(R.string.title_modify_commcmy_license);
        else
            toolbar.setTitle(R.string.title_error);
        toolbar.setTitleTextColor(Color.WHITE);



        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        comLicenseLists = getIntent().getParcelableArrayListExtra("companyLicenseItems");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("");

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


    }

    private void initView() {


        if (projectInfo == null) {
            llProjectinfo.setVisibility(View.GONE);
            banding.setVisibility(View.VISIBLE);
        } else {
            cmyname.setText(getString(R.string.license_builder) + projectInfo.cmyname);
            name.setText("项目名称：" + projectInfo.name);
            addr.setText("地址：" + projectInfo.addr);
            contact.setText("联系人：" + projectInfo.contact);
            phone.setText("电话：" + projectInfo.phone);
        }

        if (comLicenseLists != null) {
            if (comLicenseLists.size() != 0) {
                for (int i = 0; i < comLicenseLists.size(); i++) {
                    final CompanyLicenseInfo.CompanyLicenseItem item = comLicenseLists.get(i);
                    final View view = LayoutInflater.from(this).inflate(R.layout.item_edit_comlicense, llContent, false);
                    view.setTag(comLicenseLists.get(i).id);
                    if (i == 0) {
                        view.findViewById(R.id.header).setVisibility(View.VISIBLE);
                    } else {
                        view.findViewById(R.id.header).setVisibility(View.GONE);
                    }
                    ((TextView) view.findViewById(R.id.number)).setText(comLicenseLists.get(i).number);
                    ((TextView) view.findViewById(R.id.time)).setText(comLicenseLists.get(i).time);
                    String type = comLicenseLists.get(i).type;
                    String s = "";
                    if (type.equals("1")) {
                        s = "建审";
                    } else if (type.equals("2")) {
                        s = "验收";
                    } else if (type.equals("3")) {
                        s = "开业前";
                    }
                    ((TextView) view.findViewById(R.id.title)).setText(s);
                    if (comLicenseLists.get(i).isqualified != null) {
                        if (comLicenseLists.get(i).isqualified.equals("1")) {
                            ((RadioButton)view.findViewById(R.id.radio_hege)).setChecked(true);

                        } else {
                            ((RadioButton)view.findViewById(R.id.radio_buhege)).setChecked(true);
                        }
                    }

                    view.findViewById(R.id.delete).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.edit).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteLicenseItem(view);
                        }
                    });
                    view.findViewById(R.id.time).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            DatePickerDialog pickerDialog = new DatePickerDialog(EditCompanyLicense_activity.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    mYear = year;
                                    mMonth = month;
                                    mDay = dayOfMonth;
                                    ((TextView) v).setText(
                                            new StringBuilder()
                                                    .append(mYear).append("-")
                                                    .append(pad(mMonth + 1)).append("-")
                                                    .append(pad(mDay))
                                    );
                                }
                            }, mYear, mMonth, mDay);
                            pickerDialog.show();
                        }
                    });
                    view.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(EditCompanyLicense_activity.this)
                                    .setMessage("是否修改该条目?")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateComLicense(view);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).create().show();
                        }
                    });
                    llContent.addView(view);
                }
            }
        }
    }

    private void updateComLicense(final View view) {
        EditText number = (EditText) view.findViewById(R.id.number);
        TextView time = (TextView) view.findViewById(R.id.time);
        RadioGroup groupHege = (RadioGroup) view.findViewById(R.id.radioGroup);
        String number1 = number.getText().toString().trim();
        String time1 = time.getText().toString().trim();
        String isqualified = "";


        switch (groupHege.getCheckedRadioButtonId()) {
            case R.id.radio_hege:
                isqualified = "1";
                break;
            case R.id.radio_buhege:
                isqualified = "0";
                break;
        }
        if (!Utils.isVaild(number1)) {
            UiUtils.showToast("请输入文号");
            return;
        }
        if (!Utils.isVaild(time1)) {
            UiUtils.showToast("请选择时间");
            return;
        }

        if (!Utils.isVaild(isqualified)) {
            UiUtils.showToast("请选择是否合格");
            return;
        }
        Map<String, String> map = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("id", (String) view.getTag());
        builder.put("time", time1);
        builder.put("isqualified", isqualified);
        builder.put("number", number1);
        builder.put("company_id", companyId);
        map = builder.build();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
        progressDialog.show();
        if (companyType==MarkerHelper.S0CIETY){
            RetrofitHelper.getApi().updateCompanyLicense(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddCompanyResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            dismissDialog();
                            UiUtils.showToast("修改失败");
                        }

                        @Override
                        public void onNext(AddCompanyResult result) {
                            dismissDialog();
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    UiUtils.showToast(result.result);
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            } else {
                                UiUtils.showToast("修改失败");
                            }

                        }
                    });
        }else {
            RetrofitHelper.getApi().updateCommcmyLicense(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddCompanyResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            dismissDialog();
                        }

                        @Override
                        public void onNext(AddCompanyResult result) {
                            dismissDialog();
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    UiUtils.showToast(result.result);
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            } else {
                                UiUtils.showToast("修改失败");
                            }

                        }
                    });
        }

    }


    private ProgressDialog progressDialog;



    private void deleteLicenseItem(final View view) {
        LogUtil.i("要删除的licenseId=" + (String) view.getTag());
        new AlertDialog.Builder(this).setMessage("是否删除?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete((String) view.getTag(), view);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    private void delete(String id, final View view) {
        progressDialog.show();
        if (companyType == MarkerHelper.S0CIETY) {
            RetrofitHelper.getApi().deleteCompanyLicnese(id,companyId)
                    .compose(EditCompanyLicense_activity.this.<AddCompanyResult>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddCompanyResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            dismissDialog();
                        }

                        @Override
                        public void onNext(AddCompanyResult result) {
                            dismissDialog();
                            if (result.status.equals("1")) {
                                UiUtils.showToast("删除成功！");
                                llContent.removeView(view);
                            }
                        }
                    });
        } else {
            RetrofitHelper.getApi().deleteCommcmyLicnese(id,companyId)
                    .compose(EditCompanyLicense_activity.this.<AddCompanyResult>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddCompanyResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            dismissDialog();
                        }

                        @Override
                        public void onNext(AddCompanyResult result) {
                            dismissDialog();
                            if (result.status.equals("1")) {
                                UiUtils.showToast("删除成功！");
                                llContent.removeView(view);
                            }
                        }
                    });
        }

    }

    private void dismissDialog() {
        if (progressDialog!=null){
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @OnClick({R.id.bt_banding, R.id.disattach})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_banding:
                Intent intent = new Intent(this, Search_license_activity.class);
                intent.putExtra("companyId",companyId);
                intent.putExtra("companyType",companyType);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.disattach:
                new AlertDialog.Builder(this).setMessage("是否解绑定?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                disAttachLicense(projectInfo.id);

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
                break;
        }
    }

    private void disAttachLicense(String id) {
        if (id==null&&companyId==null){
            return;
        }
        progressDialog.show();
        JSONArray array = new JSONArray();
        array.put(id);
        LogUtil.i("companyId="+companyId+"licenseid="+array.toString());

        if (companyType==MarkerHelper.S0CIETY){
            RetrofitHelper.getApi().detachCompanyLicense(companyId, array.toString())
                    .compose(this.<AddCompanyResult>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddCompanyResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                        @Override
                        public void onNext(AddCompanyResult result) {
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    UiUtils.showToast(result.result);
                                    llProjectinfo.setVisibility(View.GONE);
                                    banding.setVisibility(View.VISIBLE);
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            } else {
                                UiUtils.showToast("修改失败");
                            }
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    });
        }else if (companyType==MarkerHelper.COMMONCOMPANY){
            RetrofitHelper.getApi().detachCommcmyLicense(companyId, array.toString())
                    .compose(this.<AddCompanyResult>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddCompanyResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            UiUtils.showToast("解绑失败,请检查网络设置");

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }

                        @Override
                        public void onNext(AddCompanyResult result) {
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    UiUtils.showToast(result.result);
                                    llProjectinfo.setVisibility(View.GONE);
                                    banding.setVisibility(View.VISIBLE);
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            } else {
                                UiUtils.showToast("解绑失败");
                            }
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    });
        }
    }

}
