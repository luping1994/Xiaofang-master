package com.suntrans.xiaofang.activity.others;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.model.personal.CPasswordResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.StatusBarCompat;
import com.suntrans.xiaofang.utils.UiUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/1/5.
 */

public class ModifyPassword_Activity extends BasedActivity {

    private EditText oldPasswordEdit;
    private EditText newPasswordEdit;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyps);
        setupToolBar();
        init();

    }

    private void init() {
        oldPasswordEdit = (EditText) findViewById(R.id.old_password_edit);
        newPasswordEdit = (EditText) findViewById(R.id.password_edit);
    }
    private void setupToolBar() {
        StatusBarCompat.compat(this, Color.rgb(0x2f,0x9d,0xce));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("修改密码");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }
    public void modify(View view){
        String newPassword = newPasswordEdit.getText().toString();
        if (newPassword==null&&newPassword.equals("")){
            UiUtils.showToast("密码为空");
            return;
        }
        RetrofitHelper.getApi().changedPassword(newPassword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CPasswordResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UiUtils.showToast("修改失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CPasswordResult cPasswordResult) {
                        if (cPasswordResult==null){
                            UiUtils.showToast("修改失败");
                            return;
                        }
                        if (cPasswordResult.status.equals("1")){
                            new AlertDialog.Builder(ModifyPassword_Activity.this)
                                    .setMessage("修改成功")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            killAll();
                                            Intent intent = new Intent();
                                            intent.setClass(ModifyPassword_Activity.this, Login_Activity.class);
                                            startActivity(intent);
                                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                        }
                                    }).create().show();
                        }else {
                            UiUtils.showToast("修改失败");
                        }
                    }
                });

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
