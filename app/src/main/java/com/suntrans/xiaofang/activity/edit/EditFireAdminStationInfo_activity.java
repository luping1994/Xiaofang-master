package com.suntrans.xiaofang.activity.edit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.amap.api.services.core.PoiItem;
import com.google.common.collect.ImmutableMap;
import com.journeyapps.barcodescanner.Util;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.activity.others.MapChoose_Activity;
import com.suntrans.xiaofang.model.firestation.AddFireStationResult;
import com.suntrans.xiaofang.model.firestation.FireStationDetailInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.StatusBarCompat;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


import static com.suntrans.xiaofang.utils.UiUtils.getContext;

/**
 * Created by Looney on 2016/12/3.
 * 修改政府小型站信息
 */
@Deprecated
public class EditFireAdminStationInfo_activity extends BasedActivity implements View.OnClickListener {

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
//    @BindView(R.id.servingnum)
//    EditText servingnum;
//    @BindView(R.id.fulltimenum)
//    EditText fulltimenum;

    @BindView(R.id.carnum)
    EditText carnum;

    @BindView(R.id.waterweight)
    EditText waterweight;
    @BindView(R.id.soapweight)
    EditText soapweight;
//    @BindView(R.id.district)
//    EditText district;
//
//    @BindView(R.id.community)
//    EditText community;
//    @BindView(R.id.group)
//    EditText group;
    @BindView(R.id.scroll)
    ScrollView scroll;
//    @BindView(R.id.hetong)
//    EditText hetong;
    @BindView(R.id.ll_condition)
    LinearLayout llCondition;
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
        info = (FireStationDetailInfo) getIntent().getSerializableExtra("info");
        Button add = (Button) findViewById(R.id.add);
        Button sub = (Button) findViewById(R.id.sub);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                View item = LayoutInflater.from(this).inflate(R.layout.item_adddis, null, false);
                llCondition.addView(item);
                break;
            case R.id.sub:
                if (llCondition.getChildCount() == 1)
                    break;
                llCondition.removeViewAt(llCondition.getChildCount() - 1);
                break;
        }
    }

    public void getLocation(View view) {
        Intent intent = new Intent(this, MapChoose_Activity.class);
        startActivityForResult(intent, 601);
        overridePendingTransition(android.support.design.R.anim.abc_slide_in_bottom, android.support.design.R.anim.abc_slide_out_bottom);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 601) {
            if (resultCode == -1) {
                PoiItem poiItem = data.getParcelableExtra("addrinfo");
//                name.setText(poiItem.getTitle());
                lat.setText(poiItem.getLatLonPoint().getLatitude() + "");
                lng.setText(poiItem.getLatLonPoint().getLongitude() + "");
                addr.setText(poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet());
            }
        }

    }

    @Override
    protected void onDestroy() {
//        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }


    private void initData() {


        name.setText(info.name);
        addr.setText(info.addr);
        lng.setText(info.lng);
        lat.setText(info.lat);

        area.setText(info.area);
        phone.setText(info.phone);

        carnum.setText(info.carnum);

        waterweight.setText(info.waterweight);
        soapweight.setText(info.soapweight);

//        district.setText(info.district);
////        street.setText(info.street);
//        community.setText(info.community);
//        group.setText(info.group);

        Map<String, String> cardisMap = new HashMap<>();
        try {
            if (info.cardisp != null) {
                JSONObject jsonObject = new JSONObject(info.cardisp);
                JSONArray jsonArray = jsonObject.names();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String name = jsonArray.getString(i);
                    String value = jsonObject.getString(name);
                    cardisMap.put("name", name);
                    cardisMap.put("value", value);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < cardisMap.size(); i++) {
            View item = LayoutInflater.from(this).inflate(R.layout.item_adddis, null, false);
            ((EditText) item.findViewById(R.id.con_type)).setText(cardisMap.get("name"));
            ((EditText) item.findViewById(R.id.con_detail)).setText(cardisMap.get("value"));
            llCondition.addView(item);
        }

        String member = info.membernum;
        if (member!=null&&!member.equals("")){
            try {
                JSONObject object = new JSONObject(member);
                String xianyiganbu = object.getString("现役消防队员");
                String gonganganbu = object.getString("专职消防队员");
//                servingnum.setText(xianyiganbu);
//                fulltimenum.setText(gonganganbu);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void setupToolBar() {
//        StatusBarCompat.compat(this, Color.rgb(0x2f, 0x9d, 0xce));
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
            case R.id.tijiao:
                commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }


    Map<String, String> map;
    ProgressDialog dialog;

    public void commit() {
        AlertDialog dialog = new AlertDialog.Builder(EditFireAdminStationInfo_activity.this)
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

//        String district1 = district.getText().toString();

        String area1 = area.getText().toString();
        String phone1 = phone.getText().toString();
//        String servingnum1 = servingnum.getText().toString();
//        String fulltimenum1 = fulltimenum.getText().toString();
        String carnum1 = carnum.getText().toString();
//        String cardisp1 = cardisp.getText().toString();
        String waterweight1 = waterweight.getText().toString();
        String soapweight1 = soapweight.getText().toString();

//        String street1 = street.getText().toString();
//        String community1 = community.getText().toString();
//        String group1 = group.getText().toString();

//        String hetong1 = hetong.getText().toString();

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
//        if (Utils.isVaild(servingnum1)) {
//            builder.put("servingnum", servingnum1.replace(" ", ""));
//        }
//
//        if (Utils.isVaild(fulltimenum1)) {
//            builder.put("fulltimenum", fulltimenum1.replace(" ", ""));
//        }
        if (Utils.isVaild(carnum1)) {
            builder.put("carnum", carnum1.replace(" ", ""));
        }


        if (Utils.isVaild(waterweight1)) {
            builder.put("waterweight", waterweight1.replace(" ", ""));
        }

        if (Utils.isVaild(soapweight1)) {
            builder.put("soapweight", soapweight1.replace(" ", ""));
        }
//        if (Utils.isVaild(district1)) {
//            builder.put("district", district1);
//        }
//
//
////        if (Utils.isVaild(street1)) {
////            builder.put("street", street1.replace(" ", ""));
////        }
//        if (Utils.isVaild(community1)) {
//            builder.put("community", community1.replace(" ", ""));
//        }
//        if (Utils.isVaild(group1)) {
//            builder.put("group", group1.replace(" ", ""));
//        }



        String cardisp1 = "";
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < llCondition.getChildCount(); i++) {
            if (i==0)
                continue;
            String type = ((EditText) llCondition.getChildAt(i).findViewById(R.id.con_type)).getText().toString();
            String count = ((EditText) llCondition.getChildAt(i).findViewById(R.id.con_detail)).getText().toString();
            if (Utils.isVaild(type)) {
                try {
                    jsonObject.put("类型",type);
                    jsonObject.put("数量",count);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        cardisp1 = jsonObject.toString();
        if (Utils.isVaild(cardisp1)) {
            builder.put("cardisp", cardisp1);
        }
        map = builder.build();


        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
        RetrofitHelper.getApi().updateFireAdminStation(map)
                .compose(this.<AddFireStationResult>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddFireStationResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        UiUtils.showToast(getContext(), "服务器错误!");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AddFireStationResult result) {
                        dialog.dismiss();
                        try {
                            if (result == null) {
                                UiUtils.showToast(getContext(), "修改单位信息失败!");
                            } else {
                                UiUtils.showToast(getContext(), "提示:" + result.result);
                            }
                        } catch (Exception e) {

                        }
                    }
                });
    }


}
