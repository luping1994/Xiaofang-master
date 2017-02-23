package com.suntrans.xiaofang.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;


/**
 * Created by Stay on 2/2/16.
 * Powered by www.stay4it.com
 */
public abstract class BaseActivity extends BasedActivity {
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setupToolbar();
        setUpView();
    }

    protected void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpData();
    }

    protected abstract int getLayoutId();

    protected abstract void setUpView();

    protected abstract void setUpData();

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
