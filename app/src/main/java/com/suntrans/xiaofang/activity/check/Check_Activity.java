package com.suntrans.xiaofang.activity.check;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.base.BaseActivity;
import com.suntrans.xiaofang.model.company.CompanyList;
import com.suntrans.xiaofang.model.company.CompanyListResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Looney on 2016/12/12.
 */
public class Check_Activity extends BaseActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private ArrayList<Map<String, String>> datas = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter.notifyDataSetChanged();
                    break;
                case 0:
                    if (msg.obj!=null)
                    UiUtils.showToast(App.getApplication(), (String) msg.obj);
                    break;
            }
        }
    };

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
        adapter = new MyAdapter();
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void setUpData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                datas.clear();
                getDataFromServer();
            }
        }).start();

    }

    private void getDataFromServer() {
        RetrofitHelper.getApi().getCompanyCheck("1").enqueue(new Callback<CompanyListResult>() {
            @Override
            public void onResponse(Call<CompanyListResult> call, Response<CompanyListResult> response) {
                CompanyListResult result = response.body();
                System.out.println("raw:" + response.raw());
                System.out.println("message" + response.message());
                if (result != null) {
                    if (!result.status.equals("0") && result != null) {
                        List<CompanyList> lists = result.results;
                        for (CompanyList info : lists) {
                            if (info.id == null || info.name == null) {
                                continue;
                            }
                            HashMap<String, String> map1 = new HashMap<String, String>();
                            map1.put("state", "0");
                            map1.put("name", info.name);
                            map1.put("id", info.id);
                            datas.add(map1);
                            handler.sendEmptyMessageDelayed(1, 500);
//                            handler.sendEmptyMessage(1);
                        }
                    } else {
                        try {
                            Message msg = new Message();
                            msg.what=0;
                            msg.obj = result.msg;
                            handler.sendMessageDelayed(msg,500);

                        } catch (Exception e) {

                        }
                    }
                } else {
                    Message msg = new Message();
                    msg.what=0;
                    msg.obj = "获取审核单位失败!";
                    handler.sendMessageDelayed(msg,500);
                }
            }

            @Override
            public void onFailure(Call<CompanyListResult> call, Throwable t) {
                t.printStackTrace();
                UiUtils.showToast(App.getApplication(), "获取审核单位失败,可能权限不足");
            }
        });
    }


    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolder1(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView state;
        TextView name;

        public ViewHolder2(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.value);
        }
    }

    class MyAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (viewType == 0) {
                v = LayoutInflater.from(Check_Activity.this).inflate(R.layout.item_checked, parent, false);
                ViewHolder1 holder1 = new ViewHolder1(v);
                return holder1;
            } else {
                v = LayoutInflater.from(Check_Activity.this).inflate(R.layout.item_search_activity2, parent, false);
                ViewHolder2 holder2 = new ViewHolder2(v);
                return holder2;
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ViewHolder1) {
                ((ViewHolder1) holder).name.setText("单位名称:" + datas.get(position).get("name"));
                ((ViewHolder1) holder).name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Check_Activity.this, Check_detail_Activity.class);
                        intent.putExtra("id", datas.get(position).get("id"));
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
            } else {
                ((ViewHolder2) holder).name.setText("无待审核单位");
            }

        }


        @Override
        public int getItemViewType(int position) {
            if (datas.size() == 0)
                return 1;
            return 0;
        }

        @Override
        public int getItemCount() {
            return datas.size() == 0 ? 1 : datas.size();
        }
    }

    {

    }
}
