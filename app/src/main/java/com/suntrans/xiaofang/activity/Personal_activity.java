package com.suntrans.xiaofang.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.model.personal.UserInfo;
import com.suntrans.xiaofang.model.personal.UserInfoResult;
import com.suntrans.xiaofang.network.RetrofitHelper;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/11/24.
 */

public class Personal_activity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TextView tvTitle;
    RecyclerView recyclerView;
    private ArrayList<SparseArray<String>> datas = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        setUpToolbar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private UserInfo info;
    private void getData() {
        RetrofitHelper.getUserInfoApi()
                .getUserInfo()
                .subscribeOn(Schedulers.io())
                .filter(new Func1<UserInfoResult, Boolean>() {
                    @Override
                    public Boolean call(UserInfoResult userInfoResult) {
                        return userInfoResult.status==1?true:false;
                    }
                })
                .map(new Func1<UserInfoResult, UserInfo>() {
                    @Override
                    public UserInfo call(UserInfoResult userInfoResult) {
                        return userInfoResult.lists;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo userInfo) {
                        info.truename =userInfo.truename;
                        info.mobile = userInfo.mobile;
                        info.area1 = userInfo.area1;
                        info.area2 = userInfo.area2;
                        info.area3 = userInfo.area3;
                        info.area4 = userInfo.area4;
                        collapsingToolbarLayout.setTitle(info.truename);
                        adapter.notifyDataSetChanged();
                        System.out.println(userInfo.toString());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();

                    }
                });
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("张警官");
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    private void initView() {
        info = new UserInfo();
        recyclerView  = (RecyclerView) findViewById(R.id.recycleview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter = new MyAdapter();
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal,menu);
        return true;
    }

    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (viewType==1){
                v =LayoutInflater.from(Personal_activity.this).inflate(R.layout.item_contacts,parent,false);
                return new ViewHolder1(v);
            }else {
                v =LayoutInflater.from(Personal_activity.this).inflate(R.layout.item_email,parent,false);
                return new ViewHolder2(v);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder2){
                ((ViewHolder2)holder).setData(position);
            }else {
                ((ViewHolder1)holder).setData(position);
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            if (position==0){
                return 1;
            }
            return 0;
        }

        class ViewHolder1 extends RecyclerView.ViewHolder{
            TextView mobile;
            public ViewHolder1(View itemView) {
                super(itemView);
                mobile = (TextView) itemView.findViewById(R.id.mobile);
            }

            public void setData(int position) {
                mobile.setText(info.mobile);
            }
        }

        class ViewHolder2 extends RecyclerView.ViewHolder{
            ImageView imageView ;
            TextView name;
            TextView value;
            public ViewHolder2(View itemView) {
                super(itemView);
                value = (TextView) itemView.findViewById(R.id.value);
                name = (TextView) itemView.findViewById(R.id.name);
                imageView = (ImageView) itemView.findViewById(R.id.image);
            }

            public void setData(int position) {
                if (position==1){
                    imageView.setImageResource(R.drawable.ic_email);
                    name.setText("电子邮件");
                    value.setText("dany@qq.com");
                }else {
                    imageView.setImageResource(R.drawable.ic_addr);
                    name.setText("地址");
                    value.setText(info.area1+info.area2+info.area3+info.area4);
                }
            }
        }
    }
}
