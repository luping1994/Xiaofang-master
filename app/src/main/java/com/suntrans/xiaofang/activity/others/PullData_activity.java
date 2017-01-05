package com.suntrans.xiaofang.activity.others;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;

/**
 * Created by Looney on 2016/12/1.
 */

public class PullData_activity extends BasedActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Spinner spinner1;
    private Spinner spinner2;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private TextView selectAll;
    private TextView unSelectAll;
    private TextView pull;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulldata);
        initview();
        setupToolbar();
        setListener();
    }



    private void initview() {
        selectAll = (TextView) findViewById(R.id.select_all);
        unSelectAll = (TextView) findViewById(R.id.unselect_all);
        pull = (TextView) findViewById(R.id.pull);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1 = (Spinner) findViewById(R.id.spinner2);
        adapter = new MyAdapter();
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("导出数据");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    private void setListener() {
        selectAll.setOnClickListener(this);
        unSelectAll.setOnClickListener(this);
        pull.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_all:
                for (int i=0;i<5;i++){
                    ((CheckBox)(manager.findViewByPosition(i).findViewById(R.id.ckeckbox))).setChecked(true);
                }
                break;
            case R.id.unselect_all:
                for (int i=0;i<5;i++){
                    ((CheckBox)(manager.findViewByPosition(i).findViewById(R.id.ckeckbox))).setChecked(false);
                }
                break;
            case R.id.pull:
                boolean isSelect=false;
                for (int i=0;i<5;i++){
                    isSelect = isSelect||((CheckBox)(manager.findViewByPosition(i).findViewById(R.id.ckeckbox))).isChecked();
                }
                if (isSelect){
                    Toast.makeText(PullData_activity.this,"正在导出,请稍后。。。",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PullData_activity.this,"请至少选择一项导出",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder= new ViewHolder1(LayoutInflater.from(
                    PullData_activity.this).inflate(R.layout.item_pulldata, parent,false));

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }

        class ViewHolder1 extends RecyclerView.ViewHolder{
            CheckBox cb ;
            TextView name;//单位名称
            ImageView imageView;//图标

            public ViewHolder1(View itemView) {
                super(itemView);
                cb = (CheckBox) itemView.findViewById(R.id.ckeckbox);
                name = (TextView) itemView.findViewById(R.id.name);
            }


        }
    }
}
