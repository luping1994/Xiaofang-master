package com.suntrans.xiaofang.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.views.EditView;


public class Login_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button login ;
    private EditView ed_account;
    private EditView ed_password;
    private String account;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
        ed_account = (EditView) findViewById(R.id.account);
        ed_password = (EditView) findViewById(R.id.password);
        String zhanghao = getSharedPreferences("config", Context.MODE_PRIVATE).getString("account","");
        String mima = getSharedPreferences("config", Context.MODE_PRIVATE).getString("password","");
        ed_account.setText(zhanghao);
        ed_password.setText(mima);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:{
                startActivity(new Intent(Login_Activity.this, Main_Activity.class));
                finish();
            }
        }
    }
}
