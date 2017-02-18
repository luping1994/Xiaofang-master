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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.suntrans.xiaofang.model.company.CompanyLicenseResult;
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

import static com.suntrans.xiaofang.R.id.addr;
import static com.suntrans.xiaofang.R.id.cmyname;
import static com.suntrans.xiaofang.R.id.textView;

/**
 * Created by Looney on 2016/11/24.
 */

public class Search_license_activity extends BasedActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ImageView imageView;
    private MyAdapter adapter;
    private ListView listView;
    private ArrayList<SparseArray<String>> datas = new ArrayList<>();
    private LinearLayoutManager manager;
    private EditText editText;
    private Toolbar toolbar;
    private ImageView imageView1;
    private ImageView search;

    private ProgressDialog dialog;
    private int type = MarkerHelper.LICENSE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_license);
        setupToolbar();
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

        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
//        searchView = (SearchView) findViewById(R.id.search_view);
        adapter = new MyAdapter();
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
//        searchView.setOnQueryTextListener(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("正在搜索...");
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("搜索行政审批项目");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }



    Handler handler = new Handler();


    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                RecyclerView.ViewHolder holder = new viewHolder1(LayoutInflater.from(Search_license_activity.this)
                        .inflate(R.layout.item_license_list, parent, false));
                return holder;
            } else {
                RecyclerView.ViewHolder holder = new viewHolder2(LayoutInflater.from(Search_license_activity.this)
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
            TextView addr;
            TextView cmyname;
            CardView linearLayout;

            public viewHolder1(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                addr = (TextView) itemView.findViewById(R.id.addr);
                cmyname = (TextView) itemView.findViewById(R.id.cmyname);
                name = (TextView) itemView.findViewById(R.id.name);
                linearLayout = (CardView) itemView.findViewById(R.id.ll);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent();
                        intent.putExtra("licenseID", datas.get(position).get(0));
                        intent.putExtra("companyID", getIntent().getStringExtra("companyID"));
//                        companyType = getIntent().getStringExtra("companyID").split("#")[1];
//                        intent.putExtra("to",to);
                        intent.setClass(Search_license_activity.this, Attachlicense_activity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            public void setData(final int position) {
                cmyname.setText(datas.get(position).get(1));
                name.setText(datas.get(position).get(2));
                addr.setText(datas.get(position).get(3));

            }
        }

        class viewHolder2 extends RecyclerView.ViewHolder {

            LinearLayout linearLayout;

            public viewHolder2(View itemView) {
                super(itemView);

            }

            public void setData(int position) {

            }
        }
    }

    public void search(String text) {
        searchLicense(text);
    }


    public void searchLicense(String text) {
        dialog.show();
        RetrofitHelper.getApi().searchLicense(text)
                .compose(this.<LicenseSearchResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LicenseSearchResult>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(LicenseSearchResult result) {
                        dialog.dismiss();
                        if (result != null) {
                            if (result.status.equals("1")) {
                                datas.clear();
                                List<CompanyLicenseInfo.License> lists = result.result;
                                if (lists != null) {
                                    for (CompanyLicenseInfo.License info :
                                            lists) {
                                        SparseArray<String> map = new SparseArray<String>();
                                        map.put(0, info.id);
                                        map.put(1, "建设单位:"+info.cmyname);
                                        map.put(2, "名称:"+info.name);
                                        map.put(3, "地址:"+info.addr);
                                        datas.add(map);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
