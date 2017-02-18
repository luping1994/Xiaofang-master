package com.suntrans.xiaofang.activity.edit;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.fragment.addinfo.Type4_fragment;
import com.suntrans.xiaofang.fragment.addinfo.Type5_fragment;
import com.suntrans.xiaofang.model.license.AddLicenseResult;
import com.suntrans.xiaofang.model.license.LicenseDetailInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.StatusBarCompat;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/3.
 * 修改行政审批
 */

public class EditLicense_activity extends BasedActivity {


    private Toolbar toolbar;
    private EditText txName;
    private LicenseDetailInfo info;
    private Type5_fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editlicense_info);
        ButterKnife.bind(this);
        setupToolBar();
        initView();
        initData();
    }

    private void initView() {
        info = (LicenseDetailInfo) getIntent().getSerializableExtra("info");
        fragment = new Type5_fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isInitData) {
            if (info != null) {
                fragment.setData(info);
                isInitData = true;
            }
        }
    }

    private boolean isInitData = false;

    private void initData() {


    }

    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("修改审批项目信息");
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

    public void commit() {
        AlertDialog dialog = new AlertDialog.Builder(EditLicense_activity.this)
                .setMessage("确认提交修改?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.upDateLicense();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

}
