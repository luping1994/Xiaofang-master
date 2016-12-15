package com.suntrans.xiaofang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.utils.StatusBarCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Looney on 2016/11/24.
 */

public class Search_activity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ImageView imageView;
    private MyAdapter adapter;
    private ListView listView;
    private ArrayList<Map<String,String>> datas = new ArrayList<>();
    private LinearLayoutManager manager;
    private TextWatcher watcher;
    private EditText editText;
    private Toolbar toolbar;
    private ImageView imageView1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupToolbar();
        imageView1 = (ImageView) findViewById(R.id.back);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
//        searchView = (SearchView) findViewById(R.id.search_view);
        imageView = (ImageView) findViewById(R.id.image_voice);
        adapter = new MyAdapter();
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerViewDivider(this,LinearLayoutManager.VERTICAL));
//        searchView.setOnQueryTextListener(this);
        editText = (EditText) findViewById(R.id.tx_search);
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)){
                    System.out.println(s);
                    queryAddr(s.toString());
                }else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText.addTextChangedListener(watcher);

    }

    private void setupToolbar() {
        StatusBarCompat.compat(this, Color.rgb(0x2f,0x9d,0xce));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)){
            System.out.println(newText);
            queryAddr(newText);
        }else {

        }
        return false;
    }
    private String[] strings = {"武汉东湖宾馆","武汉东湖丽晶酒店","消防宾馆"};
    private String[] addr = {"东湖路142号","东湖路141号","东湖路143号"};

    private void queryAddr(String str) {
        datas.clear();
        for (int i=0;i<strings.length;i++){
            if (strings[i].contains(str)){
                Map<String,String> map1 = new HashMap<>();
                map1.put("name",strings[i]);
                map1.put("addr",addr[i]);
                datas.add(map1);
            }
        }
        adapter.notifyDataSetChanged();
    }


    class MyAdapter extends RecyclerView.Adapter implements Filterable{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType==0){
                RecyclerView.ViewHolder holder= new viewHolder1(LayoutInflater.from(Search_activity.this)
                        .inflate(R.layout.item_search_activity, parent,false));
                return holder;
            }else {
                RecyclerView.ViewHolder holder= new viewHolder2(LayoutInflater.from(Search_activity.this)
                        .inflate(R.layout.item_search_activity2, parent,false));
                return holder;
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof  viewHolder1)
            ((viewHolder1)holder).setData(position);
            else
                ((viewHolder2)holder).setData(position);
        }


        @Override
        public int getItemViewType(int position) {
            if (position==datas.size()){
                return 1;
            }
            return 0;
        }

        @Override
        public int getItemCount() {
            if (datas.size()!=0)
                return datas.size()+1;
            else
                return 0;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    //初始化过滤结果对象
                    FilterResults results = new FilterResults();
                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                }
            };
        }

        class viewHolder1 extends RecyclerView.ViewHolder{
            TextView textView ;
            LinearLayout linearLayout;
            public viewHolder1(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.name);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.ll);
            }

            public void setData(int position) {
                final String name = datas.get(position).get("name");
                textView.setText(name);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("name",name);
                        intent.setClass(Search_activity.this,CompanyInfo_activity.class);
                        startActivity(intent);
                    }
                });
            }
        }

        class viewHolder2 extends RecyclerView.ViewHolder{
            TextView textView ;
            LinearLayout linearLayout;
            public viewHolder2(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.name);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.ll);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            public void setData(int position) {
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Search_activity.this,Add_detail_activity.class));
                    }
                });
            }
        }
    }
}
