package com.suntrans.xiaofang.activity.others;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.adapter.RecyclerViewDivider;
import com.suntrans.xiaofang.model.company.CompanyList;
import com.suntrans.xiaofang.model.company.CompanyListResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.StatusBarCompat;
import com.suntrans.xiaofang.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Looney on 2016/11/24.
 */

public class Search_activity extends BasedActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ImageView imageView;
    private MyAdapter adapter;
    private ListView listView;
    private ArrayList<SparseArray<String>> datas = new ArrayList<>();
    private LinearLayoutManager manager;
    private TextWatcher watcher;
    private EditText editText;
    private Toolbar toolbar;
    private ImageView imageView1;
    private ImageView search;

    private ProgressDialog dialog;
    private int type = 0;
    private Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupToolbar();
        spinner = (Spinner) findViewById(R.id.spinner);
//        type = getIntent().getIntExtra("type",0);
        editText = (EditText) findViewById(R.id.tx_search);
        imageView1 = (ImageView) findViewById(R.id.back);
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                search(text);
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        String text = editText.getText().toString();
                        search(text);
                        break;
                }
                return false;
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
//        searchView = (SearchView) findViewById(R.id.search_view);
        adapter = new MyAdapter();
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
//        searchView.setOnQueryTextListener(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("正在搜索...");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupToolbar() {
        StatusBarCompat.compat(this, Color.rgb(0x2f, 0x9d, 0xce));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }


    Handler handler = new Handler();


    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                RecyclerView.ViewHolder holder = new viewHolder1(LayoutInflater.from(Search_activity.this)
                        .inflate(R.layout.item_search_activity, parent, false));
                return holder;
            } else {
                RecyclerView.ViewHolder holder = new viewHolder2(LayoutInflater.from(Search_activity.this)
                        .inflate(R.layout.item_search_activity2, parent, false));
                return holder;
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof viewHolder1)
                ((viewHolder1) holder).setData(position);
            else
                ((viewHolder2) holder).setData(position);
        }


        @Override
        public int getItemViewType(int position) {
            if (position == datas.size()) {
                return 1;
            }
            return 0;
        }

        @Override
        public int getItemCount() {
            if (datas.size() == 0) {
                return 1;
            } else {
                return datas.size();
            }
        }

        class viewHolder1 extends RecyclerView.ViewHolder {
            TextView name;
            LinearLayout linearLayout;

            public viewHolder1(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.ll);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent();
                        intent.putExtra("companyID", datas.get(position).get(0) + "#" + type);
                        intent.putExtra("name", datas.get(position).get(1));
                        intent.putExtra("from", getIntent().getParcelableExtra("from"));
//                        companyType = getIntent().getStringExtra("companyID").split("#")[1];
//                        intent.putExtra("to",to);
                        intent.setClass(Search_activity.this, InfoDetail_activity.class);
                        startActivity(intent);
                    }
                });
            }

            public void setData(final int position) {
                final String name1 = datas.get(position).get(1);
//                double lat = Double.valueOf(datas.get(position).get(2));
//                double lng = Double.valueOf(datas.get(position).get(3));
//                final LatLng to = new LatLng(lat,lng);
                name.setText(name1);

            }
        }

        class viewHolder2 extends RecyclerView.ViewHolder {
            TextView textView;
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

            }
        }
    }

    private void search(String text) {
        dialog.show();
        switch (type) {
            case 1:
                searchRoom(text);
                break;
            case 2:
                searchStation(text);
                break;
            case 3:
                searchGroup(text);
                break;
            case 0:
                searchCompany(text);
                break;
            case 4:
                dialog.dismiss();
                searchLicense(text);
                break;
        }

    }


    private void searchCompany(String text) {
        RetrofitHelper.getApi().queryCompany(text).enqueue(new Callback<CompanyListResult>() {
            @Override
            public void onResponse(Call<CompanyListResult> call, Response<CompanyListResult> response) {
                CompanyListResult result = response.body();
//                System.out.println("和大嫂大嫂等等等等等等等等等等等等"+result.status);
                if (result != null) {
                    if (result.status.equals("1")) {
                        datas.clear();
                        List<CompanyList> lists = result.results;
                        for (CompanyList info : lists) {
                            SparseArray<String> map = new SparseArray<String>();
//                            System.out.println("我的id是:====>"+info.id);
                            map.put(0, info.id);
                            map.put(1, info.name);
//                            map.put(2,info.lat);
//                            map.put(3,info.lng);
                            datas.add(map);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        UiUtils.showToast(App.getApplication(), result.msg);
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }
                }, 800);
            }

            @Override
            public void onFailure(Call<CompanyListResult> call, Throwable t) {
                LogUtil.e(t.toString());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }
                }, 800);
            }
        });
    }


    private void searchRoom(String text) {
        RetrofitHelper.getApi().queryFireroom(text).enqueue(new Callback<CompanyListResult>() {
            @Override
            public void onResponse(Call<CompanyListResult> call, Response<CompanyListResult> response) {
                CompanyListResult result = response.body();
//                System.out.println("和大嫂大嫂等等等等等等等等等等等等"+result.status);
                if (result != null) {
                    if (result.status.equals("1")) {
                        datas.clear();
                        List<CompanyList> lists = result.results;
                        for (CompanyList info : lists) {
                            SparseArray<String> map = new SparseArray<String>();
//                            System.out.println("我的id是:====>"+info.id);
                            map.put(0, info.id);
                            map.put(1, info.name);
//                            map.put(2,info.lat);
//                            map.put(3,info.lng);
                            datas.add(map);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        UiUtils.showToast(App.getApplication(), result.msg);
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }
                }, 800);
            }

            @Override
            public void onFailure(Call<CompanyListResult> call, Throwable t) {
                LogUtil.e(t.toString());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }
                }, 800);
            }
        });
    }


    private void searchStation(String text) {
        RetrofitHelper.getApi().queryFirestation(text).enqueue(new Callback<CompanyListResult>() {
            @Override
            public void onResponse(Call<CompanyListResult> call, Response<CompanyListResult> response) {
                CompanyListResult result = response.body();
//                System.out.println("和大嫂大嫂等等等等等等等等等等等等"+result.status);
                if (result != null) {
                    if (result.status.equals("1")) {
                        datas.clear();
                        List<CompanyList> lists = result.results;
                        for (CompanyList info : lists) {
                            SparseArray<String> map = new SparseArray<String>();
//                            System.out.println("我的id是:====>"+info.id);
                            map.put(0, info.id);
                            map.put(1, info.name);
//                            map.put(2,info.lat);
//                            map.put(3,info.lng);
                            datas.add(map);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        UiUtils.showToast(App.getApplication(), result.msg);
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }
                }, 800);
            }

            @Override
            public void onFailure(Call<CompanyListResult> call, Throwable t) {
                LogUtil.e(t.toString());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }
                }, 800);
            }
        });
    }

    private void searchGroup(String text) {
        RetrofitHelper.getApi().queryFiregroup(text).enqueue(new Callback<CompanyListResult>() {
            @Override
            public void onResponse(Call<CompanyListResult> call, Response<CompanyListResult> response) {
                CompanyListResult result = response.body();
//                System.out.println("和大嫂大嫂等等等等等等等等等等等等"+result.status);
                if (result != null) {
                    if (result.status.equals("1")) {
                        datas.clear();
                        List<CompanyList> lists = result.results;
                        for (CompanyList info : lists) {
                            SparseArray<String> map = new SparseArray<String>();
//                            System.out.println("我的id是:====>"+info.id);
                            map.put(0, info.id);
                            map.put(1, info.name);
//                            map.put(2,info.lat);
//                            map.put(3,info.lng);
                            datas.add(map);
                        }
                    } else {
                        UiUtils.showToast(App.getApplication(), result.msg);
                    }
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing())
                            adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }, 800);
            }

            @Override
            public void onFailure(Call<CompanyListResult> call, Throwable t) {
                LogUtil.e(t.toString());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }
                }, 800);
            }
        });
    }


    private void searchLicense(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("行政许可搜索接口暂未实现");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
