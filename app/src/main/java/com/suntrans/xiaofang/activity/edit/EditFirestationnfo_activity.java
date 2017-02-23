package com.suntrans.xiaofang.activity.edit;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.fragment.addinfo.Type3_fragment;
import com.suntrans.xiaofang.model.firestation.FireStationDetailInfo;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.StatusBarCompat;

import butterknife.ButterKnife;

/**
 * Created by Looney on 2016/12/3.
 * 修改乡村小型站信息
 */

public class EditFirestationnfo_activity extends BasedActivity {

    private Toolbar toolbar;
    private EditText txName;
    private FireStationDetailInfo info;
    private Type3_fragment fragment;

    private int type;
    private boolean isInitData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfirestation_info);
        ButterKnife.bind(this);
        setupToolBar();
        initView();
    }

    private void initView() {
        info = (FireStationDetailInfo) getIntent().getSerializableExtra("info");
        fragment = Type3_fragment.newInstance(type);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (info != null) {
            if (!isInitData) {

                fragment.setData(info);
                isInitData = true;
            }
        }
    }

    private void setupToolBar() {
        type = getIntent().getIntExtra("type", MarkerHelper.FIREADMINSTATION);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        if (type == MarkerHelper.FIREADMINSTATION) {

            toolbar.setTitle("修改政府小型站信息");
        } else if (type == MarkerHelper.FIRESTATION) {
            toolbar.setTitle("修改乡镇消防队信息");

        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.tijiao:
                commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }


    public void commit() {
        AlertDialog dialog = new AlertDialog.Builder(EditFirestationnfo_activity.this)
                .setMessage("确认提交修改?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.updateFireStationInfo();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }


}
