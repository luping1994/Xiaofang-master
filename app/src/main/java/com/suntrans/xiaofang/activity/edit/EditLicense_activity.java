package com.suntrans.xiaofang.activity.edit;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.model.license.AddLicenseResult;
import com.suntrans.xiaofang.model.license.LicenseDetailInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.StatusBarCompat;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/3.
 * 修改消防室信息
 */

public class EditLicense_activity extends BasedActivity {


    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.addr)
    TextView addr;
    @BindView(R.id.leader)
    TextView leader;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.isqualified)
    TextView isqualified;


    @BindView(R.id.number_complete)
    EditText numberComplete;
    @BindView(R.id.time_complete)
    TextView timeComplete;
    @BindView(R.id.isqualified_complete)
    Spinner isqualifiedComplete;

    @BindView(R.id.number_open)
    EditText numberOpen;
    @BindView(R.id.time_open)
    TextView timeOpen;
    @BindView(R.id.isqualified_open)
    Spinner isqualifiedOpen;


    private Toolbar toolbar;
    private EditText txName;
    private LicenseDetailInfo info;
    private String openIsqualified;
    private String comIsqualified;
    private DatePickerDialog timeOpenDialog;
    private DatePickerDialog timeCompleteDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editlicense_info);
        ButterKnife.bind(this);
        setupToolBar();
        initView();
        initData();
    }

    private void initView() {
        info = (LicenseDetailInfo) getIntent().getSerializableExtra("info");

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        timeOpenDialog = new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        timeCompleteDialog = new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);


        timeOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeOpenDialog.show();
            }
        });
        timeComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeCompleteDialog.show();
            }
        });

        isqualifiedComplete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comIsqualified = position == 0 ? "1" : "0";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        isqualifiedOpen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                openIsqualified = position == 0 ? "1" : "0";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    Handler handler = new Handler();

    private void initData() {
        if (info.building != null) {
            name.setText(info.building.name);
            addr.setText(info.building.addr);
            leader.setText(info.building.leader);
            phone.setText(info.building.phone);

            number.setText(info.building.number);
            time.setText(info.building.created_at);
            isqualified.setText(info.building.isqualified == null ? "--" : info.building.isqualified.equals("1") ? "是" : "否");
        }

        numberComplete.setText(info.cnumber);
        timeComplete.setText(info.completetime);
        if (info.cisqualified != null && !info.cisqualified.equals("")) {
            isqualifiedComplete.setSelection(info.cisqualified.equals("1") ? 0 : 1);
        }
        timeComplete.setText(info.completetime);

        numberOpen.setText(info.onumber);
        timeOpen.setText(info.opentime);
        if (info.oisqualified != null && !info.oisqualified.equals("")) {
            isqualifiedOpen.setSelection(info.oisqualified.equals("1") ? 0 : 1);
        }


    }

    private void setupToolBar() {
        StatusBarCompat.compat(this, Color.rgb(0x2f, 0x9d, 0xce));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("修改单位信息");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在修改请稍候");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int mYear;
    private int mMonth;
    private int mDay;
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    if (timeOpenDialog.getDatePicker().equals(view)) {
                        timeOpen.setText(
                                new StringBuilder()
                                        .append(mYear).append("-")
                                        .append(pad(mMonth + 1)).append("-")
                                        .append(pad(mDay))
                        );
                    } else {
                        timeComplete.setText(
                                new StringBuilder()
                                        .append(mYear).append("-")
                                        .append(pad(mMonth + 1)).append("-")
                                        .append(pad(mDay))
                        );
                    }

                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    Map<String, String> map;
    Map<String, String> map2;
    ProgressDialog dialog;

    public void commit(View view) {
        AlertDialog dialog = new AlertDialog.Builder(EditLicense_activity.this)
                .setMessage("确认提交修改?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commitData();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    private void commitData() {
        dialog.show();
        map = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        ImmutableMap.Builder<String, String> builder2 = ImmutableMap.builder();

        //开业前信息
        builder.put("id", info.id);
        String numberOpen1 = numberOpen.getText().toString();
        String timeOpen1 = timeOpen.getText().toString();
        if (Utils.isVaild(numberOpen1)) {
            builder.put("number", numberOpen1.replace(" ", ""));
        }
        if (Utils.isVaild(timeOpen1)) {
            builder.put("time", timeOpen1.replace(" ", ""));
        }
        builder.put("isqualified", openIsqualified);


        //验收信息
        builder2.put("id", info.id);
        String numberComplete1 = numberComplete.getText().toString();
        String timeComplete1 = timeComplete.getText().toString();
        if (Utils.isVaild(numberComplete1)) {
            builder2.put("number", numberComplete1.replace(" ", ""));
        }
        if (Utils.isVaild(timeComplete1)) {
            builder2.put("time", timeComplete1.replace(" ", ""));
        }
        builder2.put("isqualified", comIsqualified);

        map = builder.build();
        map2 = builder2.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
        for (Map.Entry<String, String> entry : map2.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
        RetrofitHelper.getApi().completeLicense(map2)
                .compose(this.<AddLicenseResult>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddLicenseResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        UiUtils.showToast(UiUtils.getContext(), "服务器错误!");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AddLicenseResult result) {
                        dialog.dismiss();
                        try {
                            if (result == null) {
                                UiUtils.showToast(UiUtils.getContext(), "更新信息失败!");
                            } else if (result.status.equals("1")){
                                UiUtils.showToast(UiUtils.getContext(), "提示:" + result.result);
                            }else {
                                UiUtils.showToast(UiUtils.getContext(), "提示:" + result.msg);

                            }
                        } catch (Exception e) {

                        }
                    }
                });


        RetrofitHelper.getApi().openLicense(map)
                .compose(this.<AddLicenseResult>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddLicenseResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        UiUtils.showToast(UiUtils.getContext(), "服务器错误!");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AddLicenseResult result) {
                        dialog.dismiss();
                        try {
                            if (result == null) {
                                UiUtils.showToast(UiUtils.getContext(), "更新信息失败!");
                            } else if (result.status.equals("1")){
                                UiUtils.showToast(UiUtils.getContext(), "提示:" + result.result);
                            }else {
                                UiUtils.showToast(UiUtils.getContext(), "提示:" + result.msg);

                            }
                        } catch (Exception e) {

                        }
                    }
                });
    }
}
