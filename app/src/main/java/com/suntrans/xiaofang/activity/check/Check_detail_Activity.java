package com.suntrans.xiaofang.activity.check;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.base.BaseActivity;
import com.suntrans.xiaofang.model.company.CompanyDetailnfo;
import com.suntrans.xiaofang.model.company.CompanyDetailnfoResult;
import com.suntrans.xiaofang.model.company.CompanyPassResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.UiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Looney on 2016/12/12.
 */
public class Check_detail_Activity extends BaseActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private String id;
    private ArrayList<SparseArray<String>> datas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void setupToolbar() {
        super.setupToolbar();
        toolbar.setTitle("审核信息");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_checked_detail;
    }

    @Override
    protected void setUpView() {
        id = getIntent().getStringExtra("id");
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(this);
        adapter = new MyAdapter();
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void setUpData() {
        SparseArray<String> array = new SparseArray<>();
        array.put(0, "单位名称");
        array.put(1, "--");
        datas.add(array);

        SparseArray<String> array1 = new SparseArray<>();
        array1.put(0, "单位地址");
        array1.put(1, "--");
        datas.add(array1);

        SparseArray<String> array2 = new SparseArray<>();
        array2.put(0, "消防管辖");
        array2.put(1, "--");
        datas.add(array2);


        SparseArray<String> array3 = new SparseArray<>();
        array3.put(0, "火灾危险性");
        array3.put(1, "--");
        datas.add(array3);

        SparseArray<String> array4 = new SparseArray<>();
        array4.put(0, "建筑面积");
        array4.put(1, "--");
        datas.add(array4);

        SparseArray<String> array5 = new SparseArray<>();
        array5.put(0, "安全出口数");
        array5.put(1, "--");
        datas.add(array5);

        SparseArray<String> array6 = new SparseArray<>();
        array6.put(0, "疏散楼梯数");
        array6.put(1, "--");
        datas.add(array6);

        SparseArray<String> array7 = new SparseArray<>();
        array7.put(0, "自动消防设施");
        array7.put(1, "--");
        datas.add(array7);

        SparseArray<String> array8 = new SparseArray<>();
        array8.put(0, "单位属性");
        array8.put(1, "--");
        datas.add(array8);

        SparseArray<String> array9 = new SparseArray<>();
        array9.put(0, "法定代表人");
        array9.put(1, "--");
        datas.add(array9);

        SparseArray<String> array10 = new SparseArray<>();
        array10.put(0, "法定代表人身份证");
        array10.put(1, "--");
        datas.add(array10);


        SparseArray<String> array11 = new SparseArray<>();
        array11.put(0, "法定代表人电话");
        array11.put(1, "--");
        datas.add(array11);

        SparseArray<String> array12 = new SparseArray<>();
        array12.put(0, "消防安全管理人");
        array12.put(1, "--");
        datas.add(array12);
        SparseArray<String> array13 = new SparseArray<>();
        array13.put(0, "消防安全管理人身份证");
        array13.put(1, "--");
        datas.add(array13);


        SparseArray<String> array14 = new SparseArray<>();
        array14.put(0, "消防安全管理人电话");
        array14.put(1, "--");
        datas.add(array14);

        SparseArray<String> array15 = new SparseArray<>();
        array15.put(0, "消防安全责任人");
        array15.put(1, "--");
        datas.add(array15);


        SparseArray<String> array16 = new SparseArray<>();
        array16.put(0, "消防安全责任人身份证");
        array16.put(1, "--");
        datas.add(array16);

        SparseArray<String> array17 = new SparseArray<>();
        array17.put(0, "消防安全责任人电话");
        array17.put(1, "--");
        datas.add(array17);

        SparseArray<String> array18 = new SparseArray<>();
        array18.put(0, "组织机构代码");
        array18.put(1, "--");
        datas.add(array18);


        SparseArray<String> array19 = new SparseArray<>();
        array19.put(0, "上级单位");
        array19.put(1, "--");
        datas.add(array19);

        SparseArray<String> array20 = new SparseArray<>();
        array20.put(0, "单位成立时间");
        array20.put(1, "--");
        datas.add(array20);


        SparseArray<String> array21 = new SparseArray<>();
        array21.put(0, "单位电话");
        array21.put(1, "--");
        datas.add(array21);

        SparseArray<String> array22 = new SparseArray<>();
        array22.put(0, "职工人数");
        array22.put(1, "--");
        datas.add(array22);

        SparseArray<String> array23 = new SparseArray<>();
        array23.put(0, "占地面积");
        array23.put(1, "--");
        datas.add(array23);

        SparseArray<String> array24 = new SparseArray<>();
        array24.put(0, "单位专职（志愿）消防员数");
        array24.put(1, "--");
        datas.add(array24);


        SparseArray<String> array25 = new SparseArray<>();
        array25.put(0, "消防车道数");
        array25.put(1, "--");
        datas.add(array25);

        SparseArray<String> array26 = new SparseArray<>();
        array26.put(0, "消防电梯数");
        array26.put(1, "--");
        datas.add(array26);


        SparseArray<String> array27 = new SparseArray<>();
        array27.put(0, "消防车道类型");
        array27.put(1, "--");
        datas.add(array27);

        SparseArray<String> array28 = new SparseArray<>();
        array28.put(0, "避难层数");
        array28.put(1, "--");
        datas.add(array28);

        SparseArray<String> array29 = new SparseArray<>();
        array29.put(0, "避难层位置");
        array29.put(1, "--");
        datas.add(array29);

        SparseArray<String> array30 = new SparseArray<>();
        array30.put(0, "单位毗邻情况");
        array30.put(1, "--");
        datas.add(array30);
        RetrofitHelper.getApi().getCompanyCheckDetail(id).enqueue(new Callback<CompanyDetailnfoResult>() {
            @Override
            public void onResponse(Call<CompanyDetailnfoResult> call, Response<CompanyDetailnfoResult> response) {
                CompanyDetailnfoResult result = response.body();
                if (result != null) {
                    if (!result.status.equals("0")) {
                        CompanyDetailnfo info = result.info;
                        myInfo = info;
                        refreshView(info);
                    }
                }
            }

            @Override
            public void onFailure(Call<CompanyDetailnfoResult> call, Throwable t) {

            }
        });

    }

    public CompanyDetailnfo myInfo;


    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;

            v = LayoutInflater.from(Check_detail_Activity.this).inflate(R.layout.item_cominfo, parent, false);
            return new ViewHolder1(v);

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ((ViewHolder1) holder).setData(position);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
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
    }


    private void refreshView(CompanyDetailnfo info) {
        datas.get(0).put(1, info.name);//名字
        datas.get(1).put(1, info.addr);//地址
        datas.get(2).put(1, info.incharge == null ? "--" : info.incharge);
        datas.get(3).put(1, info.dangerlevel == null ? "--" : info.dangerlevel);
        datas.get(4).put(1, info.buildarea == null ? "--" : info.buildarea + "平方米");
        datas.get(5).put(1, info.exitnum == null ? "--" : info.exitnum + "个");
        datas.get(6).put(1, info.stairnum == null ? "--" : info.stairnum + "个");
        datas.get(7).put(1, info.facility == null ? "--" : info.facility);
        datas.get(8).put(1, info.mainattribute + info.subattribute);
        datas.get(9).put(1, info.artiname);
        datas.get(10).put(1, info.artiid);
        datas.get(11).put(1, info.artiphone);
        datas.get(12).put(1, info.managername);
        datas.get(13).put(1, info.managerid);
        datas.get(14).put(1, info.managerphone);
        datas.get(15).put(1, info.responname);
        datas.get(16).put(1, info.responid);
        datas.get(17).put(1, info.responphone);
        datas.get(18).put(1, info.orgid);
        datas.get(19).put(1, info.leaderdepart);
        datas.get(20).put(1, info.foundtime);
        datas.get(21).put(1, info.phone);
        datas.get(22).put(1, info.staffnum);
        datas.get(23).put(1, info.area);
        datas.get(24).put(1, info.firemannum);
        datas.get(25).put(1, info.lanenum);
        datas.get(26).put(1, info.elevatornum + "个");
        datas.get(27).put(1, info.elevatornum);
        datas.get(28).put(1, info.refugenum);
        datas.get(29).put(1, info.refugepos);
        datas.get(30).put(1, "东:" + info.east + "西" + info.west + "南" + info.south + "北" + info.north);
        adapter.notifyDataSetChanged();

    }

//    final int REFRESH_VIEW = 0;
//    Handler handler =new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what==REFRESH_VIEW){
//                CompanyDetailnfo info = (CompanyDetailnfo) msg.obj;
//            }
//        }
//    };
//

    public void passCheck(View view) {
        RetrofitHelper.getApi().passCompany("id").enqueue(new Callback<CompanyPassResult>() {
            @Override
            public void onResponse(Call<CompanyPassResult> call, Response<CompanyPassResult> response) {
                if (response.body() != null) {
                    CompanyPassResult result = response.body();
                    try {
                        if (result.status.equals("1")) {
                            UiUtils.showToast(App.getApplication(), result.result);
                        } else {
                            UiUtils.showToast(App.getApplication(), result.msg);
                        }
                    } catch (NullPointerException e) {
                        UiUtils.showToast(App.getApplication(), "操作失败!");
                    }

                }
            }

            @Override
            public void onFailure(Call<CompanyPassResult> call, Throwable t) {
                UiUtils.showToast(App.getApplication(), "操作失败!");
            }
        });
    }


    public void unpassCheck(View view) {
        RetrofitHelper.getApi().unpassCompany("id").enqueue(new Callback<CompanyPassResult>() {
            @Override
            public void onResponse(Call<CompanyPassResult> call, Response<CompanyPassResult> response) {
                if (response.body() != null) {
                    CompanyPassResult result = response.body();
                    try {
                        if (result.status.equals("1")) {
                            UiUtils.showToast(App.getApplication(), result.result);
                        } else {
                            UiUtils.showToast(App.getApplication(), result.msg);
                        }
                    } catch (NullPointerException e) {
                        UiUtils.showToast(App.getApplication(), "操作失败!");
                    }

                }
            }

            @Override
            public void onFailure(Call<CompanyPassResult> call, Throwable t) {
                UiUtils.showToast(App.getApplication(), "操作失败!");
            }
        });
    }
}
