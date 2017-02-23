package com.suntrans.xiaofang.activity.check;

import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.iflytek.thirdparty.E;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.base.BaseActivity;
import com.suntrans.xiaofang.model.company.CompanyList;
import com.suntrans.xiaofang.model.company.CompanyListResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
                    dismissDailog();
                    break;
                case 0:
//                    if (msg.obj != null)
//                        UiUtils.showToast((String) msg.obj);
                    break;
            }
        }
    };
    private ProgressDialog dialog;

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
        dialog = new ProgressDialog(Check_Activity.this);
        dialog.setCancelable(false);
        dialog.setMessage("加载数据中,请稍后..");
    }

    @Override
    protected void setUpData() {
        datas.clear();
        getDataFromServer();

    }


    private void dismissDailog() {
        if (dialog != null) {
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private void getDataFromServer() {
        dialog.show();
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
                        dismissDailog();
                    }

                    @Override
                    public void onNext(CompanyListResult result) {
                        if (result != null) {
                            if (result.status.equals("1")) {
                                List<CompanyList> lists = result.results;
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
                            } else {
                                try {
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = result.msg;
                                    handler.sendMessageDelayed(msg, 500);

                                } catch (Exception e) {

                                }
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = "获取审核单位失败!";
                            handler.sendMessageDelayed(msg, 500);
                        }
                        new Thread() {
                            @Override
                            public void run() {
                                getCommcmyData(source_id);

                            }
                        }.start();
                    }
                });
    }


    /**
     * CompanyListResult result = response.body();
     */

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView name;
        CardView cardView;
        public ViewHolder1(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Check_Activity.this, Check_detail_Activity.class);
                    intent.putExtra("id", datas.get(getAdapterPosition()).get("id"));
                    intent.putExtra("source_id", datas.get(getAdapterPosition()).get("source_id"));
                    intent.putExtra("special", datas.get(getAdapterPosition()).get("special"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                ((ViewHolder1) holder).name.setText("单位名称:" + datas.get(position).get("name"));

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
                        dismissDailog();
                    }

                    @Override
                    public void onNext(CompanyListResult result) {

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
                                    map1.put("source_id", info.source_id);
                                    map1.put("special", info.special);
                                    datas.add(map1);
                                    //                            handler.sendEmptyMessage(1);
                                }
                            } else {
                                try {
//                                    Message msg = new Message();
//                                    msg.what=0;
//                                    msg.obj = result.msg;
//                                    handler.sendMessageDelayed(msg,500);

                                } catch (Exception e) {

                                }
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = "获取审核单位失败!";
                            handler.sendMessageDelayed(msg, 500);
                        }
                        handler.sendEmptyMessageDelayed(1, 500);
                    }
                });
    }

}
