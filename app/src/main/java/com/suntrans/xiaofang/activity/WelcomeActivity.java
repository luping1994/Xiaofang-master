package com.suntrans.xiaofang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.suntrans.xiaofang.BaseApplication;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.others.Login_Activity;
import com.suntrans.xiaofang.model.personal.UserInfo;
import com.suntrans.xiaofang.model.personal.UserInfoResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/15.
 */

public class WelcomeActivity extends AppCompatActivity {

    private String username;
    private String password;
    private String access_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initApp();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startActivity(new Intent(WelcomeActivity.this, Main_Activity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    break;
                case 1:
                    startActivity(new Intent(WelcomeActivity.this, Login_Activity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    break;

            }
        }
    };

    private void initApp() {
        //检查token是否过期或者有无登录的操作
        username = BaseApplication.getSharedPreferences().getString("username", "");
        password = BaseApplication.getSharedPreferences().getString("password", "");
        access_token = BaseApplication.getSharedPreferences().getString("access_token", "-1");
        if (!access_token.equals("-1")) {
            getUserinfo();

        } else {
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessageDelayed(msg, 1800);
        }

    }

    private void getUserinfo() {
        RetrofitHelper.getUserInfoApi()
                .getUserInfo()
                .filter(new Func1<UserInfoResult, Boolean>() {
                    @Override
                    public Boolean call(UserInfoResult userInfoResult) {
                        return userInfoResult.status==1?true:false;
                    }
                })
                .map(new Func1<UserInfoResult, UserInfo>() {
                    @Override
                    public UserInfo call(UserInfoResult userInfoResult) {
                        return userInfoResult.lists;
                    }
                })
                .doOnNext(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo userInfo) {
                        LogUtil.i("用户ID为:"+userInfo.getId());//将用户id保存
                        BaseApplication.getSharedPreferences().edit().putString("userID",userInfo.getId()).commit();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo userInfo) {
                        if (userInfo!=null){
                            Message msg = new Message();
                            msg.what = 0;
                            handler.sendMessageDelayed(msg, 1800);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessageDelayed(msg, 1800);
                    }
                });
    }
}
