package com.suntrans.xiaofang.activity.check;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.edit.EditCommcmyInfo_activity;
import com.suntrans.xiaofang.activity.edit.EditCompanyInfo_activity;
import com.suntrans.xiaofang.activity.others.InfoDetail_activity;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.base.BaseActivity;
import com.suntrans.xiaofang.model.company.CompanyDetailnfo;
import com.suntrans.xiaofang.model.company.CompanyDetailnfoResult;
import com.suntrans.xiaofang.model.company.CompanyPassResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.DbHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.suntrans.xiaofang.R.id.error;

/**
 * Created by Looney on 2016/12/12.
 */
public class Check_detail_Activity extends BaseActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private String id;
    private ArrayList<SparseArray<String>> datas = new ArrayList<>();
    private Button pass;
    private Button unpass;
    private String special;
    private String source_id;//source_id=1 网格员录入 保存和修改 source_id==3 微信录入 审核通过和不通过

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
        this.source_id = getIntent().getStringExtra("source_id");
        this.special = getIntent().getStringExtra("special");
        pass = (Button) findViewById(R.id.pass);
        unpass = (Button) findViewById(R.id.unpass);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(this);
        adapter = new MyAdapter();
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void setUpData() {

        if (source_id != null) {
            if (source_id.equals("1")) {
                pass.setText("保存");
                unpass.setText("修改");
            } else if (source_id.equals("3")) {
                pass.setText("通过");
                unpass.setText("不通过");
            }
        }
        SparseArray<String> array = new SparseArray<>();
        array.put(0, "单位名称");
        array.put(1, "");
        datas.add(array);

        SparseArray<String> array1 = new SparseArray<>();
        array1.put(0, "单位地址");
        array1.put(1, "");
        datas.add(array1);


        SparseArray<String> array3 = new SparseArray<>();
        array3.put(0, "火灾危险性");
        array3.put(1, "");
        datas.add(array3);


        SparseArray<String> array8 = new SparseArray<>();
        array8.put(0, "单位属性");
        array8.put(1, "");
        datas.add(array8);

        SparseArray<String> array2 = new SparseArray<>();
        array2.put(0, "消防管辖");
        array2.put(1, "");
        datas.add(array2);


        SparseArray<String> array4 = new SparseArray<>();
        array4.put(0, "建筑面积");
        array4.put(1, "");
        datas.add(array4);

        SparseArray<String> array5 = new SparseArray<>();
        array5.put(0, "安全出口数");
        array5.put(1, "");
        datas.add(array5);

        SparseArray<String> array6 = new SparseArray<>();
        array6.put(0, "疏散楼梯数");
        array6.put(1, "");
        datas.add(array6);

        SparseArray<String> array7 = new SparseArray<>();
        array7.put(0, "自动消防设施");
        array7.put(1, "");
        datas.add(array7);

//        SparseArray<String> array8 = new SparseArray<>();
//        array8.put(0, "单位属性");
//        array8.put(1, "");
//        datas.add(array8);

        SparseArray<String> array9 = new SparseArray<>();
        array9.put(0, "法定代表人");
        array9.put(1, "");
        datas.add(array9);

//        SparseArray<String> array10 = new SparseArray<>();
//        array10.put(0, "法定代表人身份证");
//        array10.put(1, "");
//        datas.add(array10);


        SparseArray<String> array11 = new SparseArray<>();
        array11.put(0, "法定代表人电话");
        array11.put(1, "");
        datas.add(array11);

        SparseArray<String> array12 = new SparseArray<>();
        array12.put(0, "消防安全管理人");
        array12.put(1, "");
        datas.add(array12);

//        SparseArray<String> array13 = new SparseArray<>();
//        array13.put(0, "消防安全管理人身份证");
//        array13.put(1, "");
//        datas.add(array13);


        SparseArray<String> array13 = new SparseArray<>();
        array13.put(0, "消防安全管理人电话");
        array13.put(1, "");
        datas.add(array13);

        SparseArray<String> array14 = new SparseArray<>();
        array14.put(0, "消防安全责任人");
        array14.put(1, "");
        datas.add(array14);


//        SparseArray<String> array16 = new SparseArray<>();
//        array16.put(0, "消防安全责任人身份证");
//        array16.put(1, "");
//        datas.add(array16);

        SparseArray<String> array15 = new SparseArray<>();
        array15.put(0, "责任人电话");
        array15.put(1, "");
        datas.add(array15);


        SparseArray<String> array16 = new SparseArray<>();
        array16.put(0, "单位编码");
        array16.put(1, "");
        datas.add(array16);


        SparseArray<String> array17 = new SparseArray<>();
        array17.put(0, "组织机构代码");
        array17.put(1, "");
        datas.add(array17);

        SparseArray<String> array18 = new SparseArray<>();
        array18.put(0, "单位其它情况");
        array18.put(1, "");
        datas.add(array18);

        SparseArray<String> array19 = new SparseArray<>();
        array19.put(0, "上级单位");
        array19.put(1, "");
        datas.add(array19);

        SparseArray<String> array20 = new SparseArray<>();
        array20.put(0, "单位成立时间");
        array20.put(1, "");
        datas.add(array20);


        SparseArray<String> array21 = new SparseArray<>();
        array21.put(0, "单位电话");
        array21.put(1, "");
        datas.add(array21);

        SparseArray<String> array22 = new SparseArray<>();
        array22.put(0, "职工人数");
        array22.put(1, "");
        datas.add(array22);

        SparseArray<String> array23 = new SparseArray<>();
        array23.put(0, "占地面积");
        array23.put(1, "");
        datas.add(array23);
//
        SparseArray<String> array24 = new SparseArray<>();
        array24.put(0, "消防员人数");
        array24.put(1, "");
        datas.add(array24);


        SparseArray<String> array25 = new SparseArray<>();
        array25.put(0, "消防车道数");
        array25.put(1, "");
        datas.add(array25);

        SparseArray<String> array26 = new SparseArray<>();
        array26.put(0, "消防电梯数");
        array26.put(1, "");
        datas.add(array26);


        SparseArray<String> array27 = new SparseArray<>();
        array27.put(0, "消防车道类型");
        array27.put(1, "");
        datas.add(array27);

        SparseArray<String> array28 = new SparseArray<>();
        array28.put(0, "避难层数");
        array28.put(1, "");
        datas.add(array28);

        SparseArray<String> array29 = new SparseArray<>();
        array29.put(0, "避难层位置");
        array29.put(1, "");
        datas.add(array29);

        SparseArray<String> array30 = new SparseArray<>();
        array30.put(0, "单位毗邻情况");
        array30.put(1, "");
        datas.add(array30);

        SparseArray<String> array31 = new SparseArray<>();
        array31.put(0, "备注");
        array31.put(1, "");
        datas.add(array31);
        getData();

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
        LogUtil.i(info.toString());
        datas.get(0).put(1, info.name);//名字
        datas.get(1).put(1, info.addr);//地址
        String dangerlevels = "";
        if (info.dangerlevel != null) {
            if (info.dangerlevel.equals("1")) {
                dangerlevels = "火灾高危单位";
            } else if (info.dangerlevel.equals("2")) {
                dangerlevels = "一般消防安全重点单位";
            } else if (info.dangerlevel.equals("3")) {
                dangerlevels = "十小场所";
            } else if (info.dangerlevel.equals("4")) {
                dangerlevels = "其他非重点单位";
            }
        }
        datas.get(2).put(1, dangerlevels);
        datas.get(4).put(1, info.incharge == null ? "" : info.incharge);

        datas.get(5).put(1, info.buildarea == null ? "" : info.buildarea + "平方米");
        datas.get(6).put(1, info.exitnum == null ? "" : info.exitnum + "个");
        datas.get(7).put(1, info.stairnum == null ? "" : info.stairnum + "个");
        datas.get(8).put(1, info.facility == null ? "" : info.facility);

        String mainId = info.mainattribute;
        String mainId_small = info.mainattribute_small;
        if (info.special.equals("1")) {
            if (mainId != null) {
                StringBuilder sb = new StringBuilder();
                DbHelper helper = new DbHelper(this, "Fire", null, 1);
                SQLiteDatabase db = helper.getReadableDatabase();
                db.beginTransaction();
                Cursor cursor = db.rawQuery("select Name from attr_main where Id=?", new String[]{mainId});
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        sb.append(cursor.getString(0));
                    }
                }
                if (info.subattribute != null) {
                    Cursor cursor2 = db.rawQuery("select Name from attr_sub where Id=?", new String[]{info.subattribute});
                    if (cursor2.getCount() > 0) {
                        while (cursor2.moveToNext()) {
                            sb.append("(")
                                    .append(cursor2.getString(0))
                                    .append(")");
                        }
                    }
                    cursor2.close();
                }
                String attr = sb.toString();
                if (Utils.isVaild(attr))
                    datas.get(3).put(1, attr);
                cursor.close();
                db.setTransactionSuccessful();
                db.endTransaction();
            }
        } else {
            if (mainId_small != null) {
                String[] ids = mainId_small.split("#");
                String attr = "";
                DbHelper helper = new DbHelper(this, "Fire", null, 1);
                SQLiteDatabase db = helper.getReadableDatabase();
                for (int i = 0; i < ids.length; i++) {
                    if (info.special.equals("0")) {

                        db.beginTransaction();
                        Cursor cursor = db.rawQuery("select Name from attr_general where Id=?", new String[]{ids[i]});
                        if (cursor.getCount() > 0) {
                            while (cursor.moveToNext()) {
                                attr += cursor.getString(0);
                            }
                        }
                        cursor.close();

                    }
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                datas.get(3).put(1, attr);

            }
        }


        datas.get(9).put(1, info.artiname == null ? "" : info.artiname);
//        datas.get(10).put(1, info.artiid == null ? "" : info.artiid);
        datas.get(10).put(1, info.artiphone == null ? "" : info.artiphone);

        datas.get(11).put(1, info.managername == null ? "" : info.managername);
//        datas.get(13).put(1, info.managerid == null ? "" : info.managerid);
        datas.get(12).put(1, info.managerphone == null ? "" : info.managerphone);

        datas.get(13).put(1, info.responname == null ? "" : info.responname);
//        datas.get(16).put(1, info.responid == null ? "" : info.responid);
        datas.get(14).put(1, info.responphone == null ? "" : info.responphone);
        datas.get(15).put(1, info.companyid == null ? "" : info.companyid);

        datas.get(16).put(1, info.orgid == null ? "" : info.orgid);
        datas.get(17).put(1, info.otherdisp == null ? "" : info.otherdisp);

        datas.get(18).put(1, info.leaderdepart == null ? "" : info.leaderdepart);
        datas.get(19).put(1, info.foundtime == null ? "" : info.foundtime);
        datas.get(20).put(1, info.phone == null ? "" : info.phone);
        datas.get(21).put(1, info.staffnum == null ? "" : info.staffnum + "人");
        datas.get(22).put(1, info.area == null ? "" : info.area + "平方米");
        datas.get(23).put(1, info.firemannum == null ? "" : info.firemannum + "人");
        datas.get(24).put(1, info.lanenum == null ? "" : info.lanenum + "个");
        datas.get(25).put(1, info.elevatornum == null ? "" : info.elevatornum + "个");
        datas.get(26).put(1, info.lanepos);//数据库暂无该字段
        datas.get(27).put(1, info.refugenum == null ? "" : info.refugenum + "个");
        datas.get(28).put(1, info.refugepos == null ? "" : info.refugepos + "");
//        if (info.east == null && info.west == null && info.south == null && info.north == null) {
//            datas.get(30).put(1, "");
//        } else {
//
//            datas.get(30).put(1,"东:"+ info.east + "\n" + "西:" + info.west + "\n" + "南:" + info.south + "\n" + "北:" + info.north);
//        }
        if (info.nearby != null) {
            datas.get(29).put(1, info.nearby);
        }
        datas.get(30).put(1, info.remark);

        adapter.notifyDataSetChanged();
    }


    public void passCheck(View view) {
        new AlertDialog.Builder(Check_detail_Activity.this)
                .setMessage("是否通过/保存?")
                .setTitle("提示")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        passCheck();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

    private void passCheck() {
        if (special.equals("1")) {
            RetrofitHelper.getApi().passCompany(id, "1")
                    .compose(this.<CompanyPassResult>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CompanyPassResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            UiUtils.showToast("服务器错误");

                        }

                        @Override
                        public void onNext(CompanyPassResult result) {
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    UiUtils.showToast(result.result);
                                    finish();
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            }
                        }
                    });
        } else if (special.equals("0")) {
            RetrofitHelper.getApi().passCommCompany(id, "1")
                    .compose(this.<CompanyPassResult>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CompanyPassResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            UiUtils.showToast("服务器错误");

                        }

                        @Override
                        public void onNext(CompanyPassResult result) {
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    new AlertDialog.Builder(Check_detail_Activity.this)
                                            .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            }).setMessage(result.result)
                                            .create().show();
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            }
                        }
                    });
        }
    }


    public void unpassCheck(View view) {
        if (source_id.equals("3")) {
            new AlertDialog.Builder(Check_detail_Activity.this)
                    .setMessage("不通过?")
                    .setTitle("提示")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            unpass();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();

        } else if (source_id.equals("1")) {
            Intent intent = new Intent();
            intent.putExtra("info", myInfo);

            if (special.equals("1")) {
                intent.setClass(this, EditCompanyInfo_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (special.equals("0")) {
                intent.setClass(this, EditCommcmyInfo_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            finish();
        }

    }

    private void unpass() {
        if (special.equals("1")) {
            RetrofitHelper.getApi().passCompany(id, "2")
                    .compose(this.<CompanyPassResult>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CompanyPassResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            UiUtils.showToast("服务器错误");

                        }

                        @Override
                        public void onNext(CompanyPassResult result) {
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    new AlertDialog.Builder(Check_detail_Activity.this)
                                            .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            }).setMessage(result.result)
                                            .create().show();
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            }
                        }
                    });
        } else {
            RetrofitHelper.getApi().passCommCompany(id, "2")
                    .compose(this.<CompanyPassResult>bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CompanyPassResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            UiUtils.showToast("服务器错误");

                        }

                        @Override
                        public void onNext(CompanyPassResult result) {
                            if (result != null) {
                                if (result.status.equals("1")) {
                                    new AlertDialog.Builder(Check_detail_Activity.this)
                                            .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            }).setMessage(result.result)
                                            .create().show();
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            }
                        }
                    });
        }
    }

    //获取单位详情信息
    private void getData() {
        if (special.equals("1")) {
            RetrofitHelper.getApi().getCompanyDetail(id)
                    .compose(this.<CompanyDetailnfoResult>bindUntilEvent(ActivityEvent.DESTROY))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<CompanyDetailnfoResult>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(CompanyDetailnfoResult result) {

                            if (result != null) {
                                if (!result.status.equals("0")) {
                                    final CompanyDetailnfo info = result.info;
                                    myInfo = info;
                                    refreshView(info);
                                } else {
                                    UiUtils.showToast(result.msg);

                                }
                            } else {
                                UiUtils.showToast(App.getApplication(), "请求失败!");

                            }
                        }
                    });
        } else if (special.equals("0")) {
            RetrofitHelper.getApi().getCommcmyDetail(id)
                    .compose(this.<CompanyDetailnfoResult>bindUntilEvent(ActivityEvent.DESTROY))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<CompanyDetailnfoResult>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(CompanyDetailnfoResult result) {

                            if (result != null) {
                                if (!result.status.equals("0")) {
                                    final CompanyDetailnfo info = result.info;
                                    myInfo = info;
                                    refreshView(info);
                                } else {
                                    UiUtils.showToast(result.msg);

                                }
                            } else {
                                UiUtils.showToast(App.getApplication(), "请求失败!");

                            }
                        }
                    });
        }

    }
}
