package com.suntrans.xiaofang.activity.others;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.model.personal.UserInfo;
import com.suntrans.xiaofang.model.personal.UserInfoResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/11/24.
 */

public class Personal_activity extends BasedActivity {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.shiyongshuoming)
    TextView shiyongshuoming;
    @BindView(R.id.xiugaimima)
    TextView xiugaimima;
    @BindView(R.id.about)
    TextView about;
    @BindView(R.id.fankui)
    TextView fankui;
    @BindView(R.id.signout)
    TextView signout;
    private Toolbar toolbar;
    private ArrayList<SparseArray<String>> datas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        setUpToolbar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        RetrofitHelper.getUserInfoApi()
                .getUserInfo()
                .compose(this.<UserInfoResult>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .filter(new Func1<UserInfoResult, Boolean>() {
                    @Override
                    public Boolean call(UserInfoResult userInfoResult) {
                        return userInfoResult.status == 1 ? true : false;
                    }
                })
                .map(new Func1<UserInfoResult, UserInfo>() {
                    @Override
                    public UserInfo call(UserInfoResult userInfoResult) {
                        return userInfoResult.lists;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo userInfo) {
                        if (userInfo != null) {
                            if (userInfo.getTruename() != null)
                                name.setText(userInfo.getTruename());
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();

                    }
                });
    }


    Handler handler = new Handler();

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("个人中心");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }


    private void initView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @OnClick({R.id.name, R.id.shiyongshuoming, R.id.xiugaimima, R.id.about, R.id.fankui, R.id.signout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.name:
                break;
            case R.id.shiyongshuoming:
                startActivity(new Intent(this, Introduce_Activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.xiugaimima:
                Intent intent = new Intent();
                intent.setClass(Personal_activity.this, ModifyPassword_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.about:
                startActivity(new Intent(this, About_activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.fankui:
                startActivity(new Intent(this, Help_activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.signout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("退出登录?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = App.getSharedPreferences().edit();
                                editor.putString("expires_in", "-1");
                                editor.putString("access_token", "-1");
                                editor.putString("refresh_token", "-1");
                                editor.putLong("firsttime", 1l);
                                editor.commit();
                                killAll();
                                Intent intent = new Intent();
                                intent.setClass(Personal_activity.this, Login_Activity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                break;
        }
    }
}
