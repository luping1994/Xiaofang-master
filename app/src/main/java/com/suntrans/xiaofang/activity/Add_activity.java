package com.suntrans.xiaofang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.utils.StatusBarCompat;

/**
 * Created by Looney on 2016/12/13.
 */
public class Add_activity  extends AppCompatActivity{
    private Toolbar toolbar;
    RadioGroup group;
    RadioButton button;
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
        StatusBarCompat.compat(this, Color.rgb(0x2f,0x9d,0xce));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("选择你要添加的单位类型");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    public void next(View view){
        int selectId = group.getCheckedRadioButtonId();
        button = (RadioButton) findViewById(selectId);
        Intent intent = new Intent();
        switch (selectId){
            case R.id.type1:
                intent.putExtra("type",0);
                break;
            case R.id.type2:
                intent.putExtra("type",1);
                break;
            case R.id.type3:
                intent.putExtra("type",2);
                break;
            case R.id.type4:
                intent.putExtra("type",3);
                break;
        }
        intent.setClass(this,Add_detail_activity.class);
        intent.putExtra("location",getIntent().getParcelableExtra("location"));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
