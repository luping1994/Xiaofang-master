package com.suntrans.xiaofang.activity.check;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.edit.EditCommcmyInfo_activity;
import com.suntrans.xiaofang.activity.edit.EditCompanyInfo_activity;
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

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/12.
 */
public class CheckBindDetailActivity extends BaseActivity {
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
        toolbar.setTitle("审核绑定");
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
        array3.put(0, "绑定人名称");
        array3.put(1, "");
        datas.add(array3);


        SparseArray<String> array8 = new SparseArray<>();
        array8.put(0, "绑定人电话");
        array8.put(1, "");
        datas.add(array8);


        getData();

    }

    public CompanyDetailnfo myInfo;


    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;

            v = LayoutInflater.from(CheckBindDetailActivity.this).inflate(R.layout.item_cominfo, parent, false);
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

        datas.get(2).put(1, info.binder.truename == null ? "--" : info.binder.truename );

        datas.get(3).put(1, info.binder.mobile == null ? "--" : info.binder.mobile );


        adapter.notifyDataSetChanged();
    }


    public void passCheck(View view) {
        new AlertDialog.Builder(CheckBindDetailActivity.this)
                .setMessage("是否通过?")
                .setTitle("提示")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        verify("1");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }


    public void unpassCheck(View view) {
        new AlertDialog.Builder(CheckBindDetailActivity.this)
                .setMessage("不通过?")
                .setTitle("提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        verify("0");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();


    }

    private void verify(String status) {
            RetrofitHelper.getApi().verifyBind(id, status)
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
                                    new AlertDialog.Builder(CheckBindDetailActivity.this)
                                            .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            }).setMessage(result.msg)
                                            .create().show();
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            }
                        }
                    });
    }

    private void getData() {
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
    }

}
