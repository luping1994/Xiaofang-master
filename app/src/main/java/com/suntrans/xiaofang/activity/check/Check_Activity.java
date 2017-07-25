package com.suntrans.xiaofang.activity.check;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.base.BaseActivity;
import com.suntrans.xiaofang.model.company.CompanyList;
import com.suntrans.xiaofang.model.company.CompanyListResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/12.
 */
public class Check_Activity extends BaseActivity {
    @BindView(R.id.loading_bar)
    ProgressBar loadingBar;
    @BindView(R.id.loading_failed)
    Button loadingFailed;
    @BindView(R.id.errorll)
    LinearLayout errorll;

    private void Loadding() {
        recyclerView.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.VISIBLE);
        errorll.setVisibility(View.INVISIBLE);
    }

    private void LoaddingError() {
        recyclerView.setVisibility(View.INVISIBLE);
        loadingBar.setVisibility(View.INVISIBLE);
        errorll.setVisibility(View.VISIBLE);
    }

    private void LoadingSuccess() {
        recyclerView.setVisibility(View.VISIBLE);
        loadingBar.setVisibility(View.INVISIBLE);
        errorll.setVisibility(View.INVISIBLE);
    }

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private ArrayList<Map<String, String>> datas = new ArrayList<>();


    private final int SUCCESS = 1;
    private final int ERROR = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    LoadingSuccess();
                    adapter.notifyDataSetChanged();
                    break;
                case ERROR:
                    LoaddingError();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void setupToolbar() {
        ButterKnife.bind(this);
        super.setupToolbar();
        toolbar.setTitle("待审核单位列表");
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
        recyclerView.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void setUpData() {
        datas.clear();
        getDataFromServer();

    }


    private void getDataFromServer() {
        Loadding();
        JSONArray array = new JSONArray();
        array.put("1");
        array.put("3");
        final String source_id = array.toString();
        RetrofitHelper.getApi().getCompanyCheck("0", source_id)
                .compose(this.<CompanyListResult>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CompanyListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.sendEmptyMessageDelayed(ERROR, 500);
                    }

                    @Override
                    public void onNext(CompanyListResult result) {
                        if (result != null) {
                            if (result.status.equals("1")) {
                                List<CompanyList> lists = result.results;
                                if (lists!=null){
                                    for (CompanyList info : lists) {
                                        if (info.id == null || info.name == null) {
                                            continue;
                                        }
                                        HashMap<String, String> map1 = new HashMap<String, String>();
                                        map1.put("state", "0");
                                        map1.put("name", info.name);
                                        map1.put("id", info.id);
                                        map1.put("source_id", info.source_id);
                                        map1.put("special", info.special);
                                        datas.add(map1);
                                    }
                                }

                            } else {

                            }
                        } else {
                            handler.sendEmptyMessageDelayed(ERROR, 500);
                        }
                        getCommcmyData(source_id);
                    }
                });
    }

    @OnClick(R.id.loading_failed)
    public void onClick() {
        getDataFromServer();
    }


    /**
     * CompanyListResult result = response.body();
     */

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView name;
        CardView cardView;
        TextView state;
        public ViewHolder1(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            state = (TextView) itemView.findViewById(R.id.state);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (datas.get(getAdapterPosition()).get("source_id").equals("4")){
                        Intent intent = new Intent(Check_Activity.this, CheckBindDetailActivity.class);
                        intent.putExtra("id", datas.get(getAdapterPosition()).get("id"));
                        intent.putExtra("source_id", datas.get(getAdapterPosition()).get("source_id"));
                        intent.putExtra("special", datas.get(getAdapterPosition()).get("special"));
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else {
                        Intent intent = new Intent(Check_Activity.this, Check_detail_Activity.class);
                        intent.putExtra("id", datas.get(getAdapterPosition()).get("id"));
                        intent.putExtra("source_id", datas.get(getAdapterPosition()).get("source_id"));
                        intent.putExtra("special", datas.get(getAdapterPosition()).get("special"));
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                }
            });
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
                String name = datas.get(position).get("name");
                if (datas.get(position).get("source_id").equals("3")) {
                    ((ViewHolder1) holder).state.setText("待审核信息");

                } else if (datas.get(position).get("source_id").equals("1")) {
                    ((ViewHolder1) holder).state.setText("待审核信息");

                }else if (datas.get(position).get("source_id").equals("4")){
                    ((ViewHolder1) holder).state.setText("待审核绑定");

                }
                ((ViewHolder1) holder).name.setText(name);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getCommcmyData(String source_id) {
        RetrofitHelper.getApi().getCommcmyCheck("0", source_id)
                .compose(this.<CompanyListResult>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CompanyListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.sendEmptyMessageDelayed(ERROR, 500);
                    }

                    @Override
                    public void onNext(CompanyListResult result) {

                        if (result != null) {
                            if (result.status.equals("1")) {
                                List<CompanyList> lists = result.results;
                                if (lists!=null)
                                {
                                    for (CompanyList info : lists) {
                                        if (info.id == null || info.name == null) {
                                            continue;
                                        }
                                        HashMap<String, String> map1 = new HashMap<String, String>();
                                        map1.put("state", "0");
                                        map1.put("name", info.name);
                                        map1.put("id", info.id);
                                        map1.put("source_id", info.source_id);
                                        map1.put("special", info.special);
                                        datas.add(map1);
                                        //                            handler.sendEmptyMessage(1);
                                    }
                                }
                            }
                        }
                        getCompanyBind();
                    }
                });
    }

    private void getCompanyBind() {
        RetrofitHelper.getApi().getCheckBind()
                .compose(this.<CompanyListResult>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CompanyListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.sendEmptyMessageDelayed(ERROR, 500);
                    }

                    @Override
                    public void onNext(CompanyListResult result) {

                        if (result != null) {
                            if (result.status.equals("0")) {
                                List<CompanyList> lists = result.results;
                                if (lists!=null){
                                    for (CompanyList info : lists) {
                                        if (info.id == null || info.name == null) {
                                            continue;
                                        }
                                        HashMap<String, String> map1 = new HashMap<String, String>();
                                        map1.put("state", "0");
                                        map1.put("name", info.name);
                                        map1.put("id", info.id);
                                        map1.put("source_id", "4");
//                                        System.out.println(info.source_id);
                                        map1.put("special", info.special);
                                        datas.add(map1);
                                        //                            handler.sendEmptyMessage(1);
                                    }
                                }
                            }
                        }
                        handler.sendEmptyMessageDelayed(SUCCESS, 500);
                    }
                });
    }

}
