package com.suntrans.xiaofang.activity.add;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.StatusBarCompat;

/**
 * Created by Looney on 2016/12/13.
 */
public class Add_activity  extends BasedActivity {
    private Toolbar toolbar;
    RadioGroup group;
    AppCompatRadioButton button;
    Button nextButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setupToolBar();
        initView();
    }

    private void initView() {
        group = (RadioGroup) findViewById(R.id.radioGroup);
    }

    private void setupToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("选择添加的消防信息类型");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    public void next(View view){
        int selectId = group.getCheckedRadioButtonId();
        button = (AppCompatRadioButton) findViewById(selectId);
        Intent intent = new Intent();
        switch (selectId){
            case R.id.type1:
                intent.putExtra("type", MarkerHelper.S0CIETY);
                break;
            case R.id.type2:
                intent.putExtra("type",MarkerHelper.FIREROOM);
                break;
            case R.id.type3:
                intent.putExtra("type",MarkerHelper.FIRESTATION);
                break;
            case R.id.type4:
                intent.putExtra("type",MarkerHelper.FIREGROUP);
                break;
            case R.id.type5:
                intent.putExtra("type",MarkerHelper.LICENSE);
                break;
            case R.id.type6:
                intent.putExtra("type",MarkerHelper.FIREBRIGADE);
                break;
            case R.id.type7:
                intent.putExtra("type", MarkerHelper.FIREADMINSTATION);
                break;
        }
        intent.setClass(this,Add_detail_activity.class);
        intent.putExtra("location",getIntent().getParcelableExtra("location"));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
