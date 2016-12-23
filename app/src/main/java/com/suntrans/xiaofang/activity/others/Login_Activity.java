package com.suntrans.xiaofang.activity.others;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.suntrans.xiaofang.BaseApplication;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.Main_Activity;
import com.suntrans.xiaofang.model.login.LoginInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.views.EditView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button login ;
    private EditView ed_account;
    private EditView ed_password;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
        ed_account = (EditView) findViewById(R.id.account);
        ed_password = (EditView) findViewById(R.id.password);
//        ed_account.setText(BaseApplication.getSharedPreferences().getString("username",""));
//        ed_password.setText(BaseApplication.getSharedPreferences().getString("password",""));
        login.setOnClickListener(this);
        dialog= new ProgressDialog(this,R.style.MyDialogTheme);
        dialog.setMessage("登陆中..");
        dialog.setCancelable(false);

    }

    ProgressDialog dialog;
    String username="";
    String password="";
    @Override
    public void onClick(View v) {

        username=ed_account.getText().toString();
        password = ed_password.getText().toString();
        switch (v.getId()){
            case R.id.login:{
                if (username.equals("")){
                    UiUtils.showToast(Login_Activity.this,"帐号不能为空!");
                    return;
                }
                if (password.equals("")){
                    UiUtils.showToast(Login_Activity.this,"密码不能为空!");
                    return;
                }
                dialog.show();
                RetrofitHelper.Login().login("password","6","test",username,password).enqueue(new Callback<LoginInfo>() {
                    @Override
                    public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                        dialog.dismiss();
                        LoginInfo info = response.body();
                        if (info==null){
                            UiUtils.showToast(Login_Activity.this,"登录失败");
                            return;
                        }
                        if (info.error.equals("invalid_client")||info.error.equals("invalid_credentials")){
                            UiUtils.showToast(Login_Activity.this,"帐号或密码错误le!");
                            return;
                        }
                        LogUtil.i(info.access_token);
                        SharedPreferences.Editor editor= BaseApplication.getSharedPreferences().edit();
                        editor.putString("expires_in",info.expires_in);
                        editor.putString("access_token",info.access_token);
                        editor.putString("refresh_token",info.refresh_token);
                        editor.commit();
                        startActivity(new Intent(Login_Activity.this, Main_Activity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Call<LoginInfo> call, Throwable t) {
                        dialog.dismiss();
                        UiUtils.showToast(Login_Activity.this,"帐号或密码错误!");
                        System.out.println(t);
                    }
                });
//
            }
        }
    }
}
