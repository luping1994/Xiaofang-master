package com.suntrans.xiaofang.activity.edit;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.suntrans.xiaofang.model.firestation.AddFireStationResult;
import com.suntrans.xiaofang.model.firestation.FireStationDetailInfo;
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

public class EditFirestationnfo_activity extends AppCompatActivity {

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.addr)
    EditText addr;
    @BindView(R.id.lng)
    EditText lng;
    @BindView(R.id.lat)
    EditText lat;
    @BindView(R.id.area)
    EditText area;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.servingnum)
    EditText servingnum;
    @BindView(R.id.fulltimenum)
    EditText fulltimenum;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.carnum)
    EditText carnum;
    @BindView(R.id.cardisp)
    EditText cardisp;
    @BindView(R.id.waterweight)
    EditText waterweight;
    @BindView(R.id.soapweight)
    EditText soapweight;
    @BindView(R.id.district)
    EditText district;

    @BindView(R.id.street)
    EditText street;
    @BindView(R.id.community)
    EditText community;
    @BindView(R.id.group)
    EditText group;
    @BindView(R.id.scroll)
    ScrollView scroll;
    private Toolbar toolbar;
    private EditText txName;
    private FireStationDetailInfo info;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfirestation_info);
        ButterKnife.bind(this);
        setupToolBar();
        initView();
        initData();
    }

    private void initView() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.suntrans.addr.RECEIVE");
        registerReceiver(broadcastReceiver, filter);

        info = (FireStationDetailInfo) getIntent().getSerializableExtra("info");
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

    Handler handler = new Handler();

    private void initData() {


        name.setText(info.name);
        addr.setText(info.addr);
        lng.setText(info.lng);
        lat.setText(info.lat);

        area.setText(info.area);
        phone.setText(info.phone);
        servingnum.setText(info.servingnum);
        fulltimenum.setText(info.fulltimenum);

        carnum.setText(info.carnum);
        cardisp.setText(info.cardisp);
        waterweight.setText(info.waterweight);
        soapweight.setText(info.soapweight);

        district.setText(info.district);
        street.setText(info.street);
        community.setText(info.community);
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


    Map<String, String> map;
    ProgressDialog dialog;

    public void commit(View view) {
        AlertDialog dialog = new AlertDialog.Builder(EditFirestationnfo_activity.this)
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
        map = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();

        String district1 = district.getText().toString();

        String area1 = area.getText().toString();
        String phone1 = phone.getText().toString();
        String servingnum1 = servingnum.getText().toString();
        String fulltimenum1 = fulltimenum.getText().toString();
        String carnum1 = carnum.getText().toString();
        String cardisp1 = cardisp.getText().toString();
        String waterweight1 = waterweight.getText().toString();
        String soapweight1 = soapweight.getText().toString();

        String street1 = street.getText().toString();
        String community1 = community.getText().toString();
        String group1 = group.getText().toString();
        builder.put("id", info.id);

        if (Utils.isVaild(name1)) {
            builder.put("name", name1.replace(" ", ""));
        }

        if (Utils.isVaild(addr1)) {
            builder.put("addr", addr1.replace(" ", ""));
        }
        if (Utils.isVaild(lng1)) {
            builder.put("lng", lng1.replace(" ", ""));
        }
        if (Utils.isVaild(lat1)) {
            builder.put("lat", lat1.replace(" ", ""));
        }
        if (Utils.isVaild(area1)) {
            builder.put("area", area1.replace(" ", ""));
        }

        if (Utils.isVaild(phone1)) {
            builder.put("phone", phone1.replace(" ", ""));
        }
        if (Utils.isVaild(servingnum1)) {
            builder.put("servingnum", servingnum1.replace(" ", ""));
        }

        if (Utils.isVaild(fulltimenum1)) {
            builder.put("fulltimenum", fulltimenum1.replace(" ", ""));
        }
        if (Utils.isVaild(carnum1)) {
            builder.put("carnum", carnum1.replace(" ", ""));
        }

        if (Utils.isVaild(cardisp1)) {
            builder.put("cardisp", cardisp1.replace(" ", ""));
        }
        if (Utils.isVaild(waterweight1)) {
            builder.put("waterweight", waterweight1.replace(" ", ""));
        }

        if (Utils.isVaild(soapweight1)) {
            builder.put("soapweight", soapweight1.replace(" ", ""));
        }
        if (Utils.isVaild(district1)) {
            builder.put("district", district1);
        }


        if (Utils.isVaild(street1)) {
            builder.put("street", street1.replace(" ", ""));
        }
        if (Utils.isVaild(community1)) {
            builder.put("community", community1.replace(" ", ""));
        }
        if (Utils.isVaild(group1)) {
            builder.put("group", group1.replace(" ", ""));
        }
        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
        RetrofitHelper.getApi().updateFireStation(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddFireStationResult>() {
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
                    public void onNext(AddFireStationResult result) {
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
