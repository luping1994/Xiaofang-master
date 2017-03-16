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
import com.suntrans.xiaofang.model.company.CompanyLicenseInfo;
import com.suntrans.xiaofang.model.company.CompanyList;
import com.suntrans.xiaofang.model.company.CompanyListResult;
import com.suntrans.xiaofang.model.license.LicenseSearchResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.StatusBarCompat;
import com.suntrans.xiaofang.utils.UiUtils;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.suntrans.xiaofang.R.id.marker;

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
                switch (position) {
                    case 0:
                        type=MarkerHelper.S0CIETY;
                        break;
                    case 1:
                        type=MarkerHelper.COMMONCOMPANY;
                        break;
                    case 2:
                        type=MarkerHelper.FIREBRIGADE;
                        break;
                    case 3:
                        type=MarkerHelper.FIREGROUP;
                        break;
                    case 4:
                        type=MarkerHelper.FIREADMINSTATION;
                        break;
                    case 5:
                        type=MarkerHelper.FIRESTATION;
                        break;
                    case 6:
                        type=MarkerHelper.FIREROOM;
                        break;
                    case 7:
                        type=MarkerHelper.LICENSE;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupToolbar() {
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
            case MarkerHelper.S0CIETY:
                searchCompany(text);
                break;
            case MarkerHelper.FIREROOM:
                searchRoom(text);
                break;
            case MarkerHelper.FIRESTATION:
                searchStation(text);//
                break;
            case MarkerHelper.FIREGROUP:
                searchGroup(text);
                break;
            case MarkerHelper.LICENSE:
//                dialog.dismiss();
                searchLicense(text);
                break;
            case MarkerHelper.FIREBRIGADE:
                searchBrigade(text);
                break;
            case MarkerHelper.FIREADMINSTATION:
                searchAdminStation(text);
                break;
            case MarkerHelper.COMMONCOMPANY:
                searchCommcmy(text);
                break;
        }

    }


    private void searchCompany(String text) {
        RetrofitHelper.getApi().queryCompany(text)
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
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    dialog.dismiss();
                            }
                        }, 500);
                    }

                    @Override
                    public void onNext(CompanyListResult result) {
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
                        }, 500);
                    }
                });

    }

    private void searchCommcmy(String text) {
        RetrofitHelper.getApi().queryCommcmy(text)
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
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    dialog.dismiss();
                            }
                        }, 500);
                    }

                    @Override
                    public void onNext(CompanyListResult result) {
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
                        }, 500);
                    }
                });

    }

    private void searchRoom(String text) {
        RetrofitHelper.getApi().queryFireroom(text)
                .compose(this.<CompanyListResult>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CompanyListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    dialog.dismiss();
                            }
                        }, 500);
                    }

                    @Override
                    public void onNext(CompanyListResult result) {
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
                        }, 500);
                    }
                });
    }


    private void searchStation(String text) {
        RetrofitHelper.getApi().queryFirestation(text)
                .compose(this.<CompanyListResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CompanyListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    dialog.dismiss();
                            }
                        }, 800);
                    }

                    @Override
                    public void onNext(CompanyListResult result) {
                        if (result != null) {
                            if (result.status.equals("1")) {
                                datas.clear();
                                List<CompanyList> lists = result.results;
                                for (CompanyList info : lists) {
                                    SparseArray<String> map = new SparseArray<String>();
                                    map.put(0, info.id);
                                    map.put(1, info.name);
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
                });


    }


    private void searchAdminStation(String text) {
        RetrofitHelper.getApi().queryFireadminstation(text)
                .compose(this.<CompanyListResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CompanyListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    dialog.dismiss();
                            }
                        }, 800);
                    }

                    @Override
                    public void onNext(CompanyListResult result) {
                        if (result != null) {
                            if (result.status.equals("1")) {
                                datas.clear();
                                List<CompanyList> lists = result.results;
                                for (CompanyList info : lists) {
                                    SparseArray<String> map = new SparseArray<String>();
                                    map.put(0, info.id);
                                    map.put(1, info.name);
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
                });


    }

    private void searchGroup(String text) {
        RetrofitHelper.getApi().queryFiregroup(text)
                .compose(this.<CompanyListResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CompanyListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    dialog.dismiss();
                            }
                        }, 800);
                    }

                    @Override
                    public void onNext(CompanyListResult result) {
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
                });


    }


    public void searchLicense(String text) {
        RetrofitHelper.getApi().searchLicense(text)
                .compose(this.<LicenseSearchResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LicenseSearchResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }, 700);
                    }

                    @Override
                    public void onNext(LicenseSearchResult result) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }, 700);
                        if (result != null) {
                            if (result.status.equals("1")) {
                                datas.clear();
                                List<CompanyLicenseInfo.License> lists = result.result;
                                if (lists != null) {
                                    for (CompanyLicenseInfo.License info :
                                            lists) {
                                        SparseArray<String> map = new SparseArray<String>();
                                        map.put(0, info.id);
                                        map.put(1, info.name);
                                        map.put(2, "建设单位:" + info.cmyname);
                                        map.put(3, "地址:" + info.addr);
                                        datas.add(map);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                });
    }


    private void searchBrigade(String text) {
        RetrofitHelper.getApi().queryFireBrigade(text)
                .compose(this.<CompanyListResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CompanyListResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog.isShowing())
                                    dialog.dismiss();
                            }
                        }, 800);
                    }

                    @Override
                    public void onNext(CompanyListResult result) {
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
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
