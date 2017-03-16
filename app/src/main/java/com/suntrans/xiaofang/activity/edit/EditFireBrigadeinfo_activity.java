package com.suntrans.xiaofang.activity.edit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.services.core.PoiItem;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.Util;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.activity.others.MapChoose_Activity;
import com.suntrans.xiaofang.model.firebrigade.FireBrigadeDetailInfo;
import com.suntrans.xiaofang.model.firegroup.AddFireGroupResult;
import com.suntrans.xiaofang.model.firegroup.FireGroupDetailInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.suntrans.xiaofang.R.id.district;
import static com.suntrans.xiaofang.R.id.membernum;
import static com.suntrans.xiaofang.R.id.number;

/**
 * Created by Looney on 2016/12/3.
 * 修改消防中队信息
 */

public class EditFireBrigadeinfo_activity extends BasedActivity {


    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.addr)
    EditText addr;
    @BindView(R.id.getposition)
    Button getposition;
    @BindView(R.id.lng)
    EditText lng;
    @BindView(R.id.lat)
    EditText lat;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.phone1)
    EditText phone1;
    @BindView(R.id.phone2)
    EditText phone2;
    @BindView(R.id.area)
    EditText area;
    @BindView(R.id.xianyiganbu)
    EditText xianyiganbu;
    @BindView(R.id.gonganganbu)
    EditText gonganganbu;
    @BindView(R.id.zhuanzhi)
    EditText zhuanzhi;
    @BindView(R.id.wenyuan)
    EditText wenyuan;
    private Toolbar toolbar;
    private EditText txName;
    private FireBrigadeDetailInfo info;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfirebrigade_info);
        ButterKnife.bind(this);
        setupToolBar();
        initView();
        initData();
    }

    private void initView() {

        info = (FireBrigadeDetailInfo) getIntent().getSerializableExtra("info");

    }


    public void getLocation(View view) {
        Intent intent = new Intent(this, MapChoose_Activity.class);
        startActivityForResult(intent, 601);
        overridePendingTransition(android.support.design.R.anim.abc_slide_in_bottom, android.support.design.R.anim.abc_slide_out_bottom);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


    private void initData() {

        name.setText(info.name);
        addr.setText(info.addr);
        lng.setText(info.lng);
        lat.setText(info.lat);

        area.setText(info.area);

        String a = "";
        String member = info.membernum;
        if (member != null && !member.equals("")) {
            try {
                JSONObject object = new JSONObject(member);
                String xianyiganbu1 = object.getString("现役干部");
                String gonganganbu1 = object.getString("公安干部");
                String zhuanzhi1 = object.getString("专职消防员");
                String wenyuan1 = object.getString("消防文员");

                xianyiganbu.setText(xianyiganbu1);
                gonganganbu.setText(gonganganbu1);
                zhuanzhi.setText(zhuanzhi1);
                wenyuan.setText(wenyuan1);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String aa = info.phone;
        if (aa != null && !aa.equals("")) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(info.phone);
                String phone11 = jsonArray.getString(0);
                phone.setText(phone11);

                String phone22 = jsonArray.getString(1);
                phone1.setText(phone22);

                String phone33 = jsonArray.getString(2);
                phone2.setText(phone33);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    private void setupToolBar() {
//        StatusBarCompat.compat(this, Color.rgb(0x2f, 0x9d, 0xce));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("修改大队信息");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
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
        AlertDialog dialog = new AlertDialog.Builder(EditFireBrigadeinfo_activity.this)
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
        map = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();

        String area1 = area.getText().toString();

        String xianyiganbu1 = xianyiganbu.getText().toString();
        String gonganganbu1 = gonganganbu.getText().toString();
        String zhuanzhi1 = zhuanzhi.getText().toString();
        String wenyuan1 = wenyuan.getText().toString();


        String phone11 = phone.getText().toString();
        String phone22 = phone1.getText().toString();
        String phone33 = phone2.getText().toString();

        if (!Utils.isVaild(name1)) {
            UiUtils.showToast("名称不能为空");
            return;
        }

        if (!Utils.isVaild(addr1)) {
            UiUtils.showToast("地址不能为空");
            return;
        }

        if (!Utils.isVaild(phone11) && !Utils.isVaild(phone22) && !Utils.isVaild(phone33)) {
            UiUtils.showToast("请至少输入一个联系电话");
            return;
        }
        if (!Utils.isVaild(area1)){
            UiUtils.showToast("请输入辖区面积");
            return;
        }
        if (!Utils.isVaild(xianyiganbu1) || !Utils.isVaild(xianyiganbu1) || !Utils.isVaild(xianyiganbu1) || !Utils.isVaild(zhuanzhi1)) {
            UiUtils.showToast("输入人员组成");
            return;
        }
        String membernum1 = "";
        JsonObject jsonObject = new JsonObject();


        jsonObject.addProperty("现役干部", xianyiganbu1);
        jsonObject.addProperty("公安干部", gonganganbu1);
        jsonObject.addProperty("消防文员", wenyuan1);
        jsonObject.addProperty("专职消防员", zhuanzhi1);


        membernum1 = jsonObject.toString();
        builder.put("membernum", membernum1);


        JSONArray array = new JSONArray();
        if (Utils.isVaild(phone11))
            array.put(phone11);
        if (Utils.isVaild(phone22))
            array.put(phone22);
        if (Utils.isVaild(phone33))
            array.put(phone33);


        builder.put("id", info.id);

        builder.put("name", name1.replace(" ", ""));
        builder.put("addr", addr1.replace(" ", ""));
        builder.put("phone", array.toString());
        if (Utils.isVaild(area1)) {
            builder.put("area", area1.replace(" ", ""));
        }
        if (Utils.isVaild(lng1)) {
            builder.put("lng", lng1.replace(" ", ""));
        }
        if (Utils.isVaild(lat1)) {
            builder.put("lat", lat1.replace(" ", ""));
        }

        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            LogUtil.i(key + "," + value);
        }
        dialog.show();

        RetrofitHelper.getApi().updateFireBrigade(map)
                .compose(this.<AddFireGroupResult>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddFireGroupResult>() {
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
                    public void onNext(AddFireGroupResult result) {
                        dialog.dismiss();

                        try {
                            if (result == null) {
                                UiUtils.showToast(UiUtils.getContext(), "修改大队信息失败!");
                            } else {
                                if (result.status.equals("1")) {
                                    sendBroadcast();
                                    final AlertDialog dialog1;
                                    dialog1 = new AlertDialog.Builder(EditFireBrigadeinfo_activity.this)
                                            .setMessage(result.result)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .create();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog1.show();
                                        }
                                    }, 500);
                                } else if (result.status.equals("0")) {
                                    UiUtils.showToast(result.msg);
                                } else if (result.status.equals("-1")) {
                                    UiUtils.showToast("无修改权限");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                addr.setText(poiItem.getSnippet());
            }
        }

    }

    Handler handler = new Handler();
    private void sendBroadcast() {
        if (!info.lng.equals(lng.getText().toString())
                || !info.lat.equals(lat.getText().toString())) {

            Intent intent = new Intent();
            intent.putExtra("type", MarkerHelper.FIREBRIGADE);
            intent.setAction("net.suntrans.xiaofang.lp");
            sendBroadcast(intent);
        }
    }
}
