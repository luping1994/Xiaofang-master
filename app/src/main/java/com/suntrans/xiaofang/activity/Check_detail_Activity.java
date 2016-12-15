package com.suntrans.xiaofang.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.base.BaseActivity;
import com.suntrans.xiaofang.base.BaseAdapter;
import com.suntrans.xiaofang.base.BaseViewHolder;
import com.suntrans.xiaofang.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.suntrans.xiaofang.R.id.state;

/**
 * Created by Looney on 2016/12/12.
 */
public class Check_detail_Activity extends BaseActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private ArrayList<Map<String,String>> datas = new ArrayList<>();
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
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(this);
        adapter = new MyAdapter(datas, new BaseAdapter.OnItemClickListener<ViewHolder1>() {
            @Override
            public void onItemClick(ViewHolder1 holder) {
                System.out.println(holder.getAdapterPosition());
            }
        });
        recyclerView.addItemDecoration(new RecyclerViewDivider(this,LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void setUpData() {
        Map<String,String> map1 = new HashMap<>();
        map1.put("name","单位地址");
        datas.add(map1);
        Map<String,String> map2 = new HashMap<>();
        map2.put("name","消防管辖");
        datas.add(map2);

        Map<String,String> map3 = new HashMap<>();
        map3.put("name","火灾危险性");
        datas.add(map3);
        Map<String,String> map4 = new HashMap<>();
        map4.put("name","建筑面积");
        datas.add(map4);

        Map<String,String> map5 = new HashMap<>();
        map5.put("name","安全出口");
        datas.add(map5);
        Map<String,String> map6 = new HashMap<>();
        map6.put("name","疏散楼梯数量");
        datas.add(map6);
        Map<String,String> map7 = new HashMap<>();
        map7.put("name","自动消防设施");
        datas.add(map7);
        Map<String,String> map8 = new HashMap<>();
        map8.put("name","单位属性");
        datas.add(map8);
        Map<String,String> map9 = new HashMap<>();
        map9.put("name","法定代表人");
        datas.add(map9);
        Map<String,String> map10 = new HashMap<>();
        map10.put("name","消防安全管理人");
        datas.add(map10);
        Map<String,String> map11 = new HashMap<>();
        map11.put("name","消防安全责任人");
        datas.add(map11);

    }


    class ViewHolder1 extends BaseViewHolder<Map<String,String>>{
        TextView name;
        public ViewHolder1(ViewGroup parent, @LayoutRes int resId) {
            super(parent, resId);
            name = getView(R.id.name);
        }

        @Override
        public void setData(Map<String, String> data) {
            name.setText(data.get("name"));
        }
    }

    class MyAdapter extends BaseAdapter<Map<String,String>,ViewHolder1>{
        /**
         * 设置数据,并设置点击回调接口
         *
         * @param list     数据集合
         * @param listener 回调接口
         */
        public MyAdapter(@Nullable List<Map<String, String>> list, @Nullable OnItemClickListener<ViewHolder1> listener) {
            super(list, listener);
        }

        @Override
        public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder1 holder1 = new ViewHolder1(parent,R.layout.item_checked_detail);
            return holder1;
        }
    }
}
