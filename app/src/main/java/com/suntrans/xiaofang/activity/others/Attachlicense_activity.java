package com.suntrans.xiaofang.activity.others;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.fragment.infodetail.Type5__info_fragment;
import com.suntrans.xiaofang.model.company.AddCompanyResult;
import com.suntrans.xiaofang.model.license.LicenseDetailInfo;
import com.suntrans.xiaofang.model.license.LicenseDetailResult;
import com.suntrans.xiaofang.model.license.LicenseItemInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.tencent.bugly.beta.ui.h.u;

/**
 * Created by Looney on 2016/12/1.
 */

public class Attachlicense_activity extends BasedActivity {


    private ArrayList<SparseArray<String>> datas = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter myAdapter;


    public String companyId;//单位的id,通过id查找详细信息
    public String licenseId;//单位
    private Toolbar toolbar;
    private LicenseDetailInfo info;
    private List<String> licenseItemIds;
    private List<String> licenseItemIds_xiugai;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachlicense);
        setupToolBar();
        initView();
    }

    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("选择你要绑定审批项目");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.attach:
                new AlertDialog.Builder(this).setCancelable(true)
                        .setMessage("是否绑定以下审批信息")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                attachLicense();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean attachLicense() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("正在绑定,请稍后...");
        dialog.show();
        String licenseDetilId = "";

//        if (manager.getChildCount() > 5) {
//            JSONArray jsonArray = new JSONArray();
//            int a = manager.getItemCount();
//            for (int i = 5; i < manager.getItemCount(); i++) {
//                View view = manager.findViewByPosition(i);
//                boolean ischecked = ((AppCompatCheckBox) view.findViewById(R.id.checkbox)).isChecked();
//                if (ischecked) {
//                    if (!licenseItemIds.contains(datas.get(i).get(4))) {
//                        jsonArray.put(datas.get(i).get(4));
//                    }
//                }
//            }
//            licenseDetilId = jsonArray.toString();
//        }
        JSONArray jsonArray = new JSONArray();
        if (licenseItemIds_xiugai.size() != 0) {
            for (String s :
                    licenseItemIds_xiugai) {
                jsonArray.put(s);
            }
        }
        licenseDetilId = jsonArray.toString();
        if (!Utils.isVaild(licenseDetilId)||licenseDetilId.equals("[]")) {
            UiUtils.showToast("未选择任何审批条目");
            dialog.dismiss();
            return false;
        }
        LogUtil.i(TAG, "公司id==>" + companyId + "detailId==>" + licenseDetilId);
        RetrofitHelper.getApi().attachLicense(companyId, licenseDetilId)
                .compose(this.<AddCompanyResult>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddCompanyResult>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        UiUtils.showToast("服务器错误!绑定失败!");
                    }

                    @Override
                    public void onNext(AddCompanyResult result) {
                        if (result != null) {
                            if (result.status.equals("1")) {
                                dialog.dismiss();
                                String msg = result.result;
                                new AlertDialog.Builder(Attachlicense_activity.this)
                                        .setMessage(msg)
                                        .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        }).setCancelable(false).create().show();
                            } else {
                                dialog.dismiss();
                                String msg = result.msg;
                                new AlertDialog.Builder(Attachlicense_activity.this)
                                        .setMessage(msg)
                                        .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog1, int which) {
                                                dialog1.dismiss();
                                            }
                                        }).setCancelable(false).create().show();
                            }
                        } else {
                            dialog.dismiss();
                            UiUtils.showToast("服务器错误!绑定失败!");
                        }
                    }
                });
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attach, menu);
        return true;
    }

    private void initView() {
        licenseItemIds = new ArrayList<>();
        licenseItemIds_xiugai = new ArrayList<>();
        companyId = getIntent().getStringExtra("companyID");
        licenseId = getIntent().getStringExtra("licenseID");
        String jsonIds = getIntent().getStringExtra("licenseItemIds");
        if (jsonIds != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonIds);
                for (int i = 0; i < jsonArray.length(); i++) {
                    licenseItemIds.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    static final String TAG = "Attachlicense_Activity";

    private void getData() {
        LogUtil.i(TAG, "licenseID==>" + licenseId);
        RetrofitHelper.getApi().getLicenseDetailInfo(licenseId)
                .compose(this.<LicenseDetailResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<LicenseDetailResult>() {
                    @Override
                    public void call(LicenseDetailResult result) {
                        if (result != null) {
                            if (result.status.equals("1")) {
                                LicenseDetailInfo info = result.result;

                                Attachlicense_activity.this.info = info;
                                LogUtil.i(info.toString());
                                refreshView(Attachlicense_activity.this.info);
                            }
                        } else {
                            UiUtils.showToast("请求失败!");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        UiUtils.showToast(App.getApplication(), "服务器错误!");
                    }
                });
    }

    private void refreshView(LicenseDetailInfo info) {
        datas.clear();
        SparseArray<String> array0 = new SparseArray<>();
        array0.put(0, "建设单位");
        array0.put(1, info.cmyname == null ? "" : info.cmyname);
        array0.put(2, "cmyname");
        datas.add(array0);

        SparseArray<String> array4 = new SparseArray<>();
        array4.put(0, "名称");
        array4.put(1, info.name);
        array4.put(2, "name");
        datas.add(array4);

        SparseArray<String> array1 = new SparseArray<>();
        array1.put(0, "地址");
        array1.put(1, info.addr);
        array1.put(2, "addr");
        datas.add(array1);

        SparseArray<String> array2 = new SparseArray<>();
        array2.put(0, "联系人");
        array2.put(1, info.contact);
        array2.put(2, "contact");
        datas.add(array2);

        SparseArray<String> array3 = new SparseArray<>();
        array3.put(0, "联系电话");
        array3.put(1, info.phone);
        array3.put(2, "phone");
        datas.add(array3);


        List<LicenseItemInfo> items = info.detail;
        for (LicenseItemInfo item :
                items) {
            System.out.println(item.toString());
            SparseArray<String> array = new SparseArray<>();
            if (item.type.equals("1")) {
                array.put(0, "建审");
            } else if (item.type.equals("2")) {
                array.put(0, "验收");
            } else if (item.type.equals("3")) {
                array.put(0, "开业前");
            }
            array.put(1, item.number);
            array.put(2, item.time);
            array.put(3, item.isqualified.equals("1") ? "合格" : "不合格");
            array.put(4, item.id);//行政审批的详情id,用于绑定社会单位。
            datas.add(array);
        }
        myAdapter = null;
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View v;
                v = LayoutInflater.from(Attachlicense_activity.this).inflate(R.layout.item_cominfo, parent, false);
                return new Attachlicense_activity.MyAdapter.ViewHolder1(v);
            } else if (viewType == 1) {
                View view = LayoutInflater.from(Attachlicense_activity.this).inflate(R.layout.item_attachlicense_detail, parent, false);
                return new Attachlicense_activity.MyAdapter.ViewHolder2(view);
            } else {
                return null;
            }


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof Attachlicense_activity.MyAdapter.ViewHolder1) {
                ((Attachlicense_activity.MyAdapter.ViewHolder1) holder).setData(position);
            } else if (holder instanceof Attachlicense_activity.MyAdapter.ViewHolder2) {
                ((Attachlicense_activity.MyAdapter.ViewHolder2) holder).setData(position);
            } else if (holder instanceof Attachlicense_activity.MyAdapter.ViewHolder3) {
                ((Attachlicense_activity.MyAdapter.ViewHolder3) holder).setData(position);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position < 5)
                return 0;
            return 1;
        }


        class ViewHolder1 extends RecyclerView.ViewHolder {
            TextView name;
            TextView value;

            public ViewHolder1(View itemView) {
                super(itemView);
                value = (TextView) itemView.findViewById(R.id.value);
                name = (TextView) itemView.findViewById(R.id.name);
            }

            public void setData(int position) {
                name.setText(datas.get(position).get(0));
                value.setText(datas.get(position).get(1));
            }
        }

        class ViewHolder3 extends RecyclerView.ViewHolder {
            Button button;

            public ViewHolder3(View itemView) {
                super(itemView);
                button = (Button) itemView.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });
            }

            public void setData(int position) {

            }
        }


        class ViewHolder2 extends RecyclerView.ViewHolder {
            TextView title;
            TextView isq;
            TextView number;
            TextView time;
            AppCompatCheckBox checkbox;

            public ViewHolder2(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                isq = (TextView) itemView.findViewById(R.id.isqualified);
                number = (TextView) itemView.findViewById(R.id.number);
                time = (TextView) itemView.findViewById(R.id.time);
                checkbox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox);
                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int position = getAdapterPosition();
                        if (!licenseItemIds.contains(datas.get(position).get(4))) {
                            if (isChecked) {
                                if (!licenseItemIds_xiugai.contains(datas.get(position).get(4)))
                                    licenseItemIds_xiugai.add(datas.get(position).get(4));
                            } else {
                                if (licenseItemIds_xiugai.contains(datas.get(position).get(4)))
                                    licenseItemIds_xiugai.remove(datas.get(position).get(4));
                            }
                        }
                    }
                });
            }

            public void setData(int position) {
                title.setText(datas.get(position).get(0));
                number.setText(datas.get(position).get(1));
                time.setText(datas.get(position).get(2));
                isq.setText(datas.get(position).get(3));
                if (licenseItemIds != null) {
                    if (licenseItemIds.size() != 0) {
                        if (licenseItemIds.contains(datas.get(position).get(4))) {
                            checkbox.setChecked(true);
                            checkbox.setEnabled(false);
                        } else {
                            checkbox.setChecked(false);
                            checkbox.setEnabled(true);
                        }
                    }
                }


            }
        }
    }


    private static Handler handler = new Handler();
}
