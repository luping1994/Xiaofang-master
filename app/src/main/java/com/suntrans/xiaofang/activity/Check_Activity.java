package com.suntrans.xiaofang.activity;

import android.content.Intent;
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

/**
 * Created by Looney on 2016/12/12.
 */
public class Check_Activity extends BaseActivity {
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
        return R.layout.activity_checked_info;
    }

    @Override
    protected void setUpView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        manager = new LinearLayoutManager(this);
        adapter = new MyAdapter(datas, new BaseAdapter.OnItemClickListener<ViewHolder1>() {
            @Override
            public void onItemClick(ViewHolder1 holder) {
                System.out.println(holder.getAdapterPosition());
                startActivity(new Intent(Check_Activity.this,Check_detail_Activity.class));
            }
        });
        recyclerView.addItemDecoration(new RecyclerViewDivider(this,LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void setUpData() {
        Map<String,String> map1 = new HashMap<>();
        map1.put("state","1");
        datas.add(map1);
        Map<String,String> map2 = new HashMap<>();
        map2.put("state","0");
        datas.add(map2);
        datas.add(map2);
        datas.add(map2);
        datas.add(map2);
    }


    class ViewHolder1 extends BaseViewHolder<Map<String,String>>{
        TextView state;
        public ViewHolder1(ViewGroup parent, @LayoutRes int resId) {
            super(parent, resId);
            state = getView(R.id.state);
        }

        @Override
        public void setData(Map<String, String> data) {
            state.setText(data.get("state").equals("1")?"已审核":"未审核");
            state.setTextColor(data.get("state").equals("1")? Color.BLUE:Color.RED);
        }
    }

    class MyAdapter extends BaseAdapter<Map<String,String>,ViewHolder1>{
        /**
         * 设置数据,并设置点击回调接口
         *
         * @param list     数据集合
         * @param listener 回调接口
         */
        public MyAdapter(@Nullable List<Map<String, String>> list, @Nullable BaseAdapter.OnItemClickListener<ViewHolder1> listener) {
            super(list, listener);
        }

        @Override
        public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder1 holder1 = new ViewHolder1(parent,R.layout.item_checked);
            return holder1;
        }
    }
}
