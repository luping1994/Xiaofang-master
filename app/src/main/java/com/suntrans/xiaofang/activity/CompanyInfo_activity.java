package com.suntrans.xiaofang.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.base.BaseAdapter;
import com.suntrans.xiaofang.base.BaseViewHolder;
import com.suntrans.xiaofang.fragment.Type1__info_fragment;
import com.suntrans.xiaofang.model.company.CompanyDetailnfo;
import com.suntrans.xiaofang.model.company.CompanyDetailnfoResult;
import com.suntrans.xiaofang.model.company.UnitInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.StatusBarCompat;
import com.suntrans.xiaofang.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Looney on 2016/12/1.
 */

public class CompanyInfo_activity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionMenu menuRed;
    private Toolbar toolbar;

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private String title;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    protected MyAdapter adapter;
    protected LinearLayoutManager manager;

    private ArrayList<Map<String,UnitInfo>> datas = new ArrayList<>();
    private BaseAdapter.OnItemClickListener<ViewHolder1> listener ;


    public String companyId;
    private String companyType;
    private Type1__info_fragment type1__info_fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companyinfo);
        initData();
        setupToolbar();
        initView();
    }

    private void initData() {

    }

    private void setupToolbar() {
        StatusBarCompat.compat(this, Color.rgb(0x2f,0x9d,0xce));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        title = getIntent().getStringExtra("name").split("#")[0];
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    private void initView() {
        menuRed = (FloatingActionMenu) findViewById(R.id.menu_red);
        companyId = getIntent().getStringExtra("companyID").split("#")[0];
        companyType = getIntent().getStringExtra("companyID").split("#")[1];
        menuRed.setClosedOnTouchOutside(true);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);

        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);

        manager = new LinearLayoutManager(this);
        listener = new BaseAdapter.OnItemClickListener<ViewHolder1>() {
            @Override
            public void onItemClick(ViewHolder1 holder) {

            }
        };
        adapter = new MyAdapter(datas, listener);
//
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(adapter);

        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        type1__info_fragment = new Type1__info_fragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type1__info_fragment).commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab1:
                final AlertDialog.Builder builder =new AlertDialog.Builder(CompanyInfo_activity.this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog =builder.create();
                dialog.setTitle("确定删除该单位?");
                dialog.show();
                break;
            case R.id.fab2:
                Intent intent = new Intent();
                intent.setClass(CompanyInfo_activity.this,EditInfo_activity.class);
                intent.putExtra("title",title);
                intent.putExtra("id",companyId);
                intent.putExtra("info",type1__info_fragment.myInfo);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.fab3:
                Intent intent1 = new Intent();
                intent1.setClass(CompanyInfo_activity.this,CalculateRoute_Activity.class);
                if (getIntent().getParcelableExtra("from")==null){
                    final AlertDialog.Builder builder1 =new AlertDialog.Builder(CompanyInfo_activity.this);
                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog dialog1 =builder1.create();
                    dialog1.setTitle("单位未添加地理坐标,无法导航!");
                    dialog1.show();
                    break;
                }
                intent1.putExtra("from",getIntent().getParcelableExtra("from"));
                intent1.putExtra("to",getIntent().getParcelableExtra("to"));
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }


    class MyAdapter extends BaseAdapter<Map<String,UnitInfo>,ViewHolder1>{
        /**
         * 设置数据,并设置点击回调接口
         *
         * @param list     数据集合
         * @param listener 回调接口
         */
        public MyAdapter(@Nullable List<Map<String, UnitInfo>> list, @Nullable BaseAdapter.OnItemClickListener<ViewHolder1> listener) {
            super(list, listener);
        }

        @Override
        public ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder1 holder1 = new ViewHolder1(parent,R.layout.item_cominfo);
            return holder1;
        }
    }

    class ViewHolder1 extends BaseViewHolder<Map<String,UnitInfo>> {
        TextView name;
        TextView value;
        public ViewHolder1(ViewGroup parent, @LayoutRes int resId) {
            super(parent, resId);
            name = getView(R.id.name);
            value = getView(R.id.value);
        }

        @Override
        public void setData(Map<String,UnitInfo> data) {

        }
    }




    private void getData() {
        RetrofitHelper.getApi().getCompanyDetail(companyId).enqueue(new Callback<CompanyDetailnfoResult>() {
            @Override
            public void onResponse(Call<CompanyDetailnfoResult> call, Response<CompanyDetailnfoResult> response) {
                CompanyDetailnfoResult result = response.body();
                if (result!=null){
                    if (!result.status.equals("0")){
                        CompanyDetailnfo info = result.info;
                    }
                }
            }
            @Override
            public void onFailure(Call<CompanyDetailnfoResult> call, Throwable t) {
                UiUtils.showToast(CompanyInfo_activity.this,"请求单位信息失败!");
            }
        });
    }
}
