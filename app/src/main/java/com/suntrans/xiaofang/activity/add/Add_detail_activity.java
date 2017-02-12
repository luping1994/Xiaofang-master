package com.suntrans.xiaofang.activity.add;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.amap.api.maps.model.LatLng;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.fragment.addinfo.Type1_fragment;
import com.suntrans.xiaofang.fragment.addinfo.Type2_fragment;
import com.suntrans.xiaofang.fragment.addinfo.Type3_fragment;
import com.suntrans.xiaofang.fragment.addinfo.Type4_fragment;
import com.suntrans.xiaofang.fragment.addinfo.Type5_fragment;
import com.suntrans.xiaofang.fragment.addinfo.Type6_fragment;
import com.suntrans.xiaofang.fragment.addinfo.Type7_fragment;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.StatusBarCompat;

/**
 * Created by Looney on 2016/12/1.
 */

public class Add_detail_activity extends BasedActivity {
    private Toolbar toolbar;
    int type;
    private FrameLayout content;
    private Fragment[] fragments;
    private String[] title = {"添加社会单位", "添加社区消防室", "添加乡村专职消防队", "添加消防中队", "添加行政审批项目","添加消防大队","添加政府专职小型站"};
    public LatLng latLng;
    private Type1_fragment type1_fragment;
    private Type2_fragment type2_fragment;
    private Type3_fragment type3_fragment;
    private Type4_fragment type4_fragment;
    private Type5_fragment type5_fragment;
    private Type6_fragment type6_fragment;
    private Type3_fragment type3_fragment_admin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcompany);
        setupToolBar();
        initView();
    }

    private void setupToolBar() {
        type = getIntent().getIntExtra("type", 1);
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

        switch (type) {
            case MarkerHelper.S0CIETY:
                type1_fragment = new Type1_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type1_fragment).commit();
                break;
            case MarkerHelper.FIREROOM:
                type2_fragment = new Type2_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type2_fragment).commit();
                break;
            case MarkerHelper.FIRESTATION:
                type3_fragment = Type3_fragment.newInstance(MarkerHelper.FIRESTATION);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type3_fragment).commit();
                break;
            case MarkerHelper.FIREGROUP:
                type4_fragment = new Type4_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type4_fragment).commit();
                break;
            case MarkerHelper.LICENSE:
                type5_fragment = new Type5_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type5_fragment).commit();
                break;
            case MarkerHelper.FIREBRIGADE:
                type6_fragment = new Type6_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type6_fragment).commit();
                break;
            case MarkerHelper.FIREADMINSTATION:
                type3_fragment_admin = Type3_fragment.newInstance(MarkerHelper.FIREADMINSTATION);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, type3_fragment_admin).commit();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.tijiao:
                switch (type) {
                    case 0:
                        new AlertDialog.Builder(this).setMessage("确定添加社会单位?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        type1_fragment.addCommit();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                        break;
                    case 1:
                        new AlertDialog.Builder(this).setMessage("确定添加社区消防室?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        type2_fragment.addCommit();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                        break;
                    case 2:
                        new AlertDialog.Builder(this).setMessage("确定添加乡村小型站?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        type3_fragment.addCommit();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                        break;
                    case 3:
                        new AlertDialog.Builder(this).setMessage("确定添加消防中队?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        type4_fragment.addCommit();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                        break;
                    case 4:
                        new AlertDialog.Builder(this).setMessage("确定添加行政审批项目?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        type5_fragment.addCommit();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                        break;
                    case 5:
                        new AlertDialog.Builder(this).setMessage("确定添加消防大队?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        type6_fragment.addCommit();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                        break;
                    case 6:
                        new AlertDialog.Builder(this).setMessage("确定添加政府专职小型站?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        type3_fragment_admin.addCommit();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                        break;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
