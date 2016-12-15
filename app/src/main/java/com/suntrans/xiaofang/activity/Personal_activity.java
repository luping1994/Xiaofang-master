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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suntrans.xiaofang.R;

/**
 * Created by Looney on 2016/11/24.
 */

public class Personal_activity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TextView tvTitle;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        setUpToolbar();
        initView();
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
        recyclerView  = (RecyclerView) findViewById(R.id.recycleview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        MyAdapter adapter = new MyAdapter();
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

            public ViewHolder1(View itemView) {
                super(itemView);
            }

            public void setData(int position) {

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
                    value.setText("武汉市江夏区");
                }
            }
        }
    }
}
