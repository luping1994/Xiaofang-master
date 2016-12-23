package com.suntrans.xiaofang.activity.add;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.amap.api.maps.model.LatLng;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.fragment.editinfo.Type1_fragment;
import com.suntrans.xiaofang.fragment.editinfo.Type2_fragment;
import com.suntrans.xiaofang.fragment.editinfo.Type3_fragment;
import com.suntrans.xiaofang.fragment.editinfo.Type4_fragment;
import com.suntrans.xiaofang.fragment.editinfo.Type5_fragment;
import com.suntrans.xiaofang.utils.StatusBarCompat;

/**
 * Created by Looney on 2016/12/1.
 */

public class Add_detail_activity extends AppCompatActivity {
    private Toolbar toolbar;
    int type;
    private FrameLayout content;
    private Fragment[] fragments;
    private String[] title = {"添加社会单位","添加社区消防室","添加政府专职小型站","添加消防中队","添加行政许可"};
    public LatLng latLng;
    private Type2_fragment type2_fragment;
    private Type1_fragment type1_fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcompany);
        setupToolBar();
        initView();
    }

    private void setupToolBar() {
        type = getIntent().getIntExtra("type",1);
        latLng = getIntent().getParcelableExtra("location");
        StatusBarCompat.compat(this, Color.rgb(0x2f, 0x9d, 0xce));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(title[type]);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    private void initView() {
        content = (FrameLayout) findViewById(R.id.content);

        switch (type){
            case 0:
                type1_fragment = new Type1_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type1_fragment).commit();
                break;
            case 1:
                type2_fragment = new Type2_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type2_fragment).commit();
                break;
            case 2:
                Type3_fragment type3_fragment = new Type3_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type3_fragment).commit();
                break;
            case 3:
                Type4_fragment type4_fragment = new Type4_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type4_fragment).commit();
                break;
            case 4:
                Type5_fragment type5_fragment = new Type5_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type5_fragment).commit();
                break;
        }
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
