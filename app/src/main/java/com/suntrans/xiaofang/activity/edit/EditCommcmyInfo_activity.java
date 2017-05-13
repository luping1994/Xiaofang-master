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
import android.view.View;
import android.widget.EditText;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.fragment.addinfo.Type1_fragment;
import com.suntrans.xiaofang.model.company.CompanyDetailnfo;
import com.suntrans.xiaofang.utils.MarkerHelper;

import butterknife.ButterKnife;

/**
 * Created by Looney on 2016/12/3.
 */

public class EditCommcmyInfo_activity extends BasedActivity {
    private Toolbar toolbar;
    private EditText txName;
    private CompanyDetailnfo info;
    private String id;
    private Type1_fragment fragment;
    private boolean isInitData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfiregroup_info);
        ButterKnife.bind(this);
        setupToolBar();
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();

        initData();
    }

    private void initView() {
        info = (CompanyDetailnfo) getIntent().getSerializableExtra("info");
        fragment = new Type1_fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }


    public void getLocation(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initData() {
        if (info != null) {
            if (!isInitData) {
                fragment.setData(info);
                isInitData = true;
            }
        }
    }

    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getResources().getString(R.string.title_modify_commcmy));
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
        AlertDialog dialog = new AlertDialog.Builder(EditCommcmyInfo_activity.this)
                .setMessage("确认提交修改?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment.updateCompany(MarkerHelper.COMMONCOMPANY);
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
