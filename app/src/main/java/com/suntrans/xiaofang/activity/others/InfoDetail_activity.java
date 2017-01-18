package com.suntrans.xiaofang.activity.others;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.fragment.infodetail.Type1__info_fragment;
import com.suntrans.xiaofang.fragment.infodetail.Type2__info_fragment;
import com.suntrans.xiaofang.fragment.infodetail.Type3__info_fragment;
import com.suntrans.xiaofang.fragment.infodetail.Type4__info_fragment;
import com.suntrans.xiaofang.fragment.infodetail.Type5__info_fragment;
import com.suntrans.xiaofang.model.company.UnitInfo;
import com.suntrans.xiaofang.utils.StatusBarCompat;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Looney on 2016/12/1.
 */

public class InfoDetail_activity extends BasedActivity {
    public Toolbar toolbar;


    public String title;


    protected LinearLayoutManager manager;

    private ArrayList<Map<String,UnitInfo>> datas = new ArrayList<>();


    public String companyId;//单位的id,通过id查找详细信息
    public String companyType;//单位

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
        companyId = getIntent().getStringExtra("companyID").split("#")[0];
        companyType = getIntent().getStringExtra("companyID").split("#")[1];
        System.out.println("InfodetailActivity:"+"公司id="+companyId);
        switch (Integer.valueOf(companyType)){
            case 0:
                Type1__info_fragment type1__info_fragment = new Type1__info_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type1__info_fragment).commit();
                break;
            case 1:
                Type2__info_fragment type2__info_fragment = new Type2__info_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type2__info_fragment).commit();
                break;
            case 2:
                Type3__info_fragment type3__info_fragment = new Type3__info_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type3__info_fragment).commit();
                break;
            case 3:
                Type4__info_fragment type4__info_fragment = new Type4__info_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type4__info_fragment).commit();
                break;
            case 4:
                Type5__info_fragment type5__info_fragment = new Type5__info_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type5__info_fragment).commit();
                break;
        }

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



}
