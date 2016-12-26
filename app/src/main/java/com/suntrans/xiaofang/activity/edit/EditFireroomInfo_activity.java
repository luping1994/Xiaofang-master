package com.suntrans.xiaofang.activity.edit;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.model.fireroom.AddFireRoomResult;
import com.suntrans.xiaofang.model.fireroom.FireRoomDetailInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.StatusBarCompat;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/3.
 * 修改消防室信息
 */

public class EditFireroomInfo_activity extends AppCompatActivity {

    @BindView(R.id.addr)
    EditText addr;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.membernum)
    EditText membernum;
    @BindView(R.id.cardisp)
    EditText cardisp;
    @BindView(R.id.equipdisp)
    EditText equipdisp;
    @BindView(R.id.district)
    EditText district;
    @BindView(R.id.group)
    EditText group;
    @BindView(R.id.name1)
    EditText name;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.lng)
    EditText lng;
    @BindView(R.id.lat)
    EditText lat;
    @BindView(R.id.textView2)
    TextView textView2;
    private Toolbar toolbar;
    private EditText txName;
    private FireRoomDetailInfo info;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfireroom_info);
        ButterKnife.bind(this);
        setupToolBar();
        initView();
        initData();
    }

    private void initView() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.suntrans.addr.RECEIVE");
        registerReceiver(broadcastReceiver,filter);

        info = (FireRoomDetailInfo) getIntent().getSerializableExtra("info");

    }


    public void getLocation(View view) {
//        if (myLocation == null) {
//            Snackbar.make(scroll, "获取当前地址失败", Snackbar.LENGTH_SHORT).show();
//            return;
//        }
//        lat.setText(myLocation.latitude + "");
//        lng.setText(myLocation.longitude + "");
//        addr.setText(myaddr);
        String lat2 = App.getSharedPreferences().getString("lat", "-1");
        String lng2 = App.getSharedPreferences().getString("lng", "-1");
        String addr2 = App.getSharedPreferences().getString("addr", "-1");
        if (lat2.equals("-1") || lng2.equals("-1") || addr2.equals("-1")) {
            UiUtils.showToast(App.getApplication(), "获取地址失败");
            return;
        }
        lng.setText(lng2);
        lat.setText(lat2);
        addr.setText(addr2);

    }

    public LatLng myLocation;//我当前的位置
    public String myaddr;//我的位置描述
    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            myLocation = intent.getParcelableExtra("myLocation");
            myaddr = intent.getStringExtra("addrdes");
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
    private void initData() {
        name.setText(info.name);
        addr.setText(info.addr);
        contact.setText(info.contact);
        phone.setText(info.phone);
        membernum.setText(info.membernum);
        cardisp.setText(info.cardisp);
        equipdisp.setText(info.equipdisp);
        district.setText(info.district);
        group.setText(info.group);

    }

    private void setupToolBar() {
        StatusBarCompat.compat(this, Color.rgb(0x2f, 0x9d, 0xce));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("修改单位信息");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在修改请稍候");
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


    Map<String, String> map1;
    ProgressDialog dialog;

    public void commit(View view) {
        AlertDialog dialog = new AlertDialog.Builder(EditFireroomInfo_activity.this)
                .setMessage("确认提交修改?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commitData();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    private void commitData() {
        dialog.show();
        map1 = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        if (name.getText().equals("") || addr.getText().equals("")) {
            UiUtils.showToast(UiUtils.getContext(), "带*号的项目不不能为空!");
            return;
        }
        String name1 = name.getText().toString().replace(" ", "");
        String addr1 = addr.getText().toString();
        String contact1 = contact.getText().toString();
        String phone1 = phone.getText().toString();
        String membernum1 = membernum.getText().toString();
        String cardisp1 = cardisp.getText().toString();
        String equipdisp1 = equipdisp.getText().toString();
        String district1 = district.getText().toString();
        String group1 = group.getText().toString();
        builder.put("name", name1.replace(" ", ""));
        builder.put("addr", addr1.replace(" ", ""));
        builder.put("id", info.id);

        if (Utils.isVaild(contact1)) {
            builder.put("contact", contact1.replace(" ", ""));
        }

        if (Utils.isVaild(phone1)) {
            builder.put("phone", phone1.replace(" ", ""));
        }


        if (Utils.isVaild(membernum1)) {
            builder.put("membernum", membernum1.replace(" ", ""));
        }

        if (Utils.isVaild(cardisp1)) {
            builder.put("cardisp", cardisp1.replace(" ", ""));
        }

        if (Utils.isVaild(equipdisp1)) {
            builder.put("equipdisp", equipdisp1.replace(" ", ""));
        }

        if (Utils.isVaild(district1)) {
            builder.put("district", district1.replace(" ", ""));
        }

        if (Utils.isVaild(group1)) {
            builder.put("group", group1.replace(" ", ""));
        }


        map1 = builder.build();
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
        RetrofitHelper.getApi().updateFireRoom(map1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddFireRoomResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        UiUtils.showToast(UiUtils.getContext(), "服务器错误!");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AddFireRoomResult result) {
                        dialog.dismiss();
                        try {
                            if (result == null) {
                                UiUtils.showToast(UiUtils.getContext(), "修改单位信息失败!");
                            } else {
                                UiUtils.showToast(UiUtils.getContext(), "提示:" + result.result);
                            }
                        } catch (Exception e) {

                        }
                    }
                });
    }
}
