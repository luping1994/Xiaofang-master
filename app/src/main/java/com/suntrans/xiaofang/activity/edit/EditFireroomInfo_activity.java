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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.amap.api.services.core.PoiItem;
import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.activity.others.MapChoose_Activity;
import com.suntrans.xiaofang.model.company.InchargeInfo;
import com.suntrans.xiaofang.model.fireroom.AddFireRoomResult;
import com.suntrans.xiaofang.model.fireroom.FireRoomDetailInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.ActivityEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

public class EditFireroomInfo_activity extends BasedActivity implements View.OnClickListener {

    @BindView(R.id.addr)
    EditText addr;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.phone)
    EditText phone;

    //    @BindView(R.id.cardisp)
//    EditText cardisp;
//    @BindView(R.id.equipdisp)
//    EditText equipdisp;
//    @BindView(R.id.district)
//    EditText district;
//    @BindView(R.id.group)
//    EditText group;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.lng)
    EditText lng;
    @BindView(R.id.lat)
    EditText lat;


    @BindView(R.id.ll_condition)
    LinearLayout llCondition;


    @BindView(R.id.add_eq)
    Button addEq;
    @BindView(R.id.sub_eq)
    Button subEq;

    @BindView(R.id.ll_condition_eq)
    LinearLayout llConditionEq;
    //    @BindView(R.id.ganbu)
//    EditText ganbu;
//    @BindView(R.id.shibing)
//    EditText shibing;
    @BindView(R.id.membernum)
    EditText membernum;

    @BindView(R.id.getposition)
    Button getPosition;

    @BindView(R.id.dadui)
    Spinner dadui;
    @BindView(R.id.liandongzhongdui)
    Spinner liandongzhongdui;

    private Toolbar toolbar;
    private EditText txName;
    private FireRoomDetailInfo info;
    private String id;


    private ArrayAdapter<String> adduiAdapter;
    private ArrayAdapter<String> zhongduiAdapter;


    List<String> daduiName;
    List<String> daduiId;
    List<String> daduiIdPath;

    List<String> zhongduiName;
    List<String> zhongduiId;
    List<String> zhongduiPath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editfireroom_info);
        ButterKnife.bind(this);
        setupToolBar();
        initView();
        initData();
    }

    public String dadui_id_path;
    private String zhongdui_id_path;

    int flag = 0;

    private void initView() {


        info = (FireRoomDetailInfo) getIntent().getSerializableExtra("info");

        Button add = (Button) findViewById(R.id.add);
        Button sub = (Button) findViewById(R.id.sub);

        daduiName = new ArrayList<>();
        daduiId = new ArrayList<>();
        daduiIdPath = new ArrayList<>();

        zhongduiName = new ArrayList<>();
        zhongduiId = new ArrayList<>();
        zhongduiPath = new ArrayList<>();

//        daduiName.add(info.brigade_path == null ? "请选择" : "已选择(" + info.brigade_name + ")");
//        zhongduiName.add(info.group_path == null ? "请选择" : "已选择(" + info.group_name + ")");
        daduiName.add("请选择");
        zhongduiName.add("请选择");

        adduiAdapter = new ArrayAdapter(this, R.layout.item_spinner, R.id.tv_spinner, daduiName);
        zhongduiAdapter = new ArrayAdapter(this, R.layout.item_spinner, R.id.tv_spinner, zhongduiName);


        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        addEq.setOnClickListener(this);
        subEq.setOnClickListener(this);
        getPosition.setOnClickListener(this);


        dadui.setAdapter(adduiAdapter);
        liandongzhongdui.setAdapter(zhongduiAdapter);

        dadui.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (flag == 1) {
                    if (position == 0) {
                        liandongzhongdui.setSelection(0);
                        dadui_id_path = null;
                        return;
                    }
                    dadui_id_path = daduiIdPath.get(position - 1);
                    zhongduiName.clear();
                    zhongduiName.add("请选择");
                    zhongduiAdapter.notifyDataSetChanged();
                    liandongzhongdui.setSelection(0);
                    String nextId = daduiId.get(position - 1);
                    getIncharge(nextId, 1, "2");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        liandongzhongdui.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (flag == 1) {
                    if (position == 0) {
                        zhongdui_id_path = null;
                        return;
                    }
                    zhongdui_id_path = zhongduiPath.get(position - 1);
//                    System.out.println("中队" + zhongduiName.get(position - 1) + "==>" + zhongdui_id_path);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    String briadepath;
    String grouppath;

    private void initData() {
        briadepath = info.brigade_path;
        grouppath = info.group_name;
        name.setText(info.name);
        addr.setText(info.addr);
        lat.setText(info.lat);
        lng.setText(info.lng);
        contact.setText(info.contact);
        phone.setText(info.phone);
        membernum.setText(info.membernum);

        Map<String, String> cardisMap = new HashMap<>();
        try {
            if (info.cardisp != null) {
                JSONObject jsonObject = new JSONObject(info.cardisp);
                JSONArray jsonArray = jsonObject.names();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String name = jsonArray.getString(i);
                    String value = jsonObject.getString(name);
                    cardisMap.put(name, value);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator iter = cardisMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            final View item = LayoutInflater.from(this).inflate(R.layout.item_adddis, null, false);
            ((EditText) item.findViewById(R.id.con_type)).setText(key);
            ((EditText) item.findViewById(R.id.con_detail)).setText(value);
            item.findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(EditFireroomInfo_activity.this).setMessage("是否删除")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    llCondition.removeView(item);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
                }
            });
            llCondition.addView(item);
        }


        Map<String, String> cardisMap1 = new HashMap<>();
        try {
            if (info.equipdisp != null) {
                JSONObject jsonObject = new JSONObject(info.equipdisp);
                JSONArray jsonArray = jsonObject.names();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String name = jsonArray.getString(i);
                    String value = jsonObject.getString(name);
                    cardisMap1.put(name, value);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator iter1 = cardisMap1.entrySet().iterator();
        while (iter1.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iter1.next();
            String key = entry.getKey();
            String value = entry.getValue();
            final View item = LayoutInflater.from(this).inflate(R.layout.item_adddis, null, false);
            ((EditText) item.findViewById(R.id.con_type)).setText(key);
            ((EditText) item.findViewById(R.id.con_detail)).setText(value);
            item.findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(EditFireroomInfo_activity.this).setMessage("是否删除")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    llConditionEq.removeView(item);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
                }
            });
            llConditionEq.addView(item);
        }


        getIncharge("0", 0, "0");
    }

    private void setupToolBar() {
//        StatusBarCompat.compat(this, Color.rgb(0x2f, 0x9d, 0xce));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("修改社区消防室信息");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

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


    Map<String, String> map1;

    public void commit() {
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

        String name1 = name.getText().toString().replace(" ", "");
        String addr1 = addr.getText().toString();
        String contact1 = contact.getText().toString();
        String phone1 = phone.getText().toString();
        String membernum1 = membernum.getText().toString();

        String lat1 = lat.getText().toString();
        String lng1 = lng.getText().toString();


        String cardisp1 = "";
        JSONObject jsonObject1 = new JSONObject();

        for (int i = 0; i < llCondition.getChildCount(); i++) {
            if (i == 0)
                continue;
            View view = llCondition.getChildAt(i);
            EditText conType = (EditText) view.findViewById(R.id.con_type);
            EditText conDetail = (EditText) view.findViewById(R.id.con_detail);
            String type = conType.getText().toString();
            String detail = conDetail.getText().toString();
            try {
                if (Utils.isVaild(type) && Utils.isVaild(detail))
                    jsonObject1.put(type, detail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cardisp1 = jsonObject1.toString();


        String equipdisp1 = "";
        JSONObject jsonObject2 = new JSONObject();
        for (int i = 0; i < llConditionEq.getChildCount(); i++) {
            if (i == 0)
                continue;
            View view = llConditionEq.getChildAt(i);
            EditText conType = (EditText) view.findViewById(R.id.con_type);
            EditText conDetail = (EditText) view.findViewById(R.id.con_detail);
            String type = conType.getText().toString();
            String detail = conDetail.getText().toString();

            try {
                if (Utils.isVaild(type) && Utils.isVaild(detail))
                    jsonObject2.put(type, detail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        equipdisp1 = jsonObject2.toString();

        if (name1.equals("") || name1 == null) {
            UiUtils.showToast("名称不能为空!");
            return;
        }
        if (addr1 == null || addr1.equals("")) {
            UiUtils.showToast("地址不能为空!");
            return;
        }
        if (!Utils.isVaild(contact1)) {
            UiUtils.showToast("联系人不能为空!");
            return;
        }
        if (!Utils.isVaild(phone1)) {
            UiUtils.showToast("联系电话不能为空!");
            return;
        }
        if (!Utils.isVaild(dadui_id_path)) {
            UiUtils.showToast("请选择所属大队");
            return;
        }
        if (zhongduiName.size() >1) {
            if (!Utils.isVaild(zhongdui_id_path)) {
                UiUtils.showToast("请选择联动中队");
                return;
            }
        }


        map1 = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();


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

        builder.put("brigade_path", dadui_id_path);

        if (Utils.isVaild(zhongdui_id_path)) {
            builder.put("group_path", zhongdui_id_path);
        }else {
            builder.put("group_path", dadui_id_path+"-0");

        }


        if (Utils.isVaild(lat1) && Utils.isVaild(lng1)) {
            builder.put("lat", lat1.replace(" ", ""));
            builder.put("lng", lng1.replace(" ", ""));
        }

        map1 = builder.build();
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            LogUtil.i(key + "," + value);
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在修改请稍候");
        dialog.show();
        RetrofitHelper.getApi().updateFireRoom(map1)
                .compose(this.<AddFireRoomResult>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddFireRoomResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        UiUtils.showToast(UiUtils.getContext(), "服务器错误!");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(AddFireRoomResult result) {
                        dialog.dismiss();
                        try {
                            if (result == null) {
                                UiUtils.showToast("修改单位信息失败!");
                            } else if (result.status.equals("1")) {
                                sendBroadCast();
                                new AlertDialog.Builder(EditFireroomInfo_activity.this)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        }).setMessage(result.result)
                                        .create().show();
                            } else {
                                UiUtils.showToast(result.msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                final View item = LayoutInflater.from(this).inflate(R.layout.item_adddis, null, false);
                item.findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(EditFireroomInfo_activity.this).setMessage("是否删除")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        llCondition.removeView(item);
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                    }
                });
                llCondition.addView(item);
                break;

            case R.id.add_eq:
                final View item1 = LayoutInflater.from(this).inflate(R.layout.item_adddis, null, false);
                item1.findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(EditFireroomInfo_activity.this).setMessage("是否删除")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        llConditionEq.removeView(item1);
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                    }
                });
                llConditionEq.addView(item1);
                break;

            case R.id.getposition:
                Intent intent = new Intent(this, MapChoose_Activity.class);
                startActivityForResult(intent, 601);
                overridePendingTransition(android.support.design.R.anim.abc_slide_in_bottom, android.support.design.R.anim.abc_slide_out_bottom);
                break;
        }
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

    int pos = -1;
    int pos_zhongdui = -1;

    private void getIncharge(String pid, final int type, String vtype) {
        RetrofitHelper.getApi().getFireChargeArea(pid, vtype)
                .compose(this.<List<InchargeInfo>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<InchargeInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<InchargeInfo> inchargeInfos) {

                        if (inchargeInfos != null) {
                            if (type == 0) {
                                pos = -1;
                                daduiName.clear();
                                daduiId.clear();
                                daduiIdPath.clear();
                                daduiName.add("请选择");
//                                daduiName.add(info.brigade_path == null ? "请选择" : "已选择(" + info.brigade_name + ")");
                                for (InchargeInfo info :
                                        inchargeInfos) {
                                    daduiName.add(info.name);
                                    daduiId.add(info.id);
                                    daduiIdPath.add(info.parent_id_path);
                                }
                                adduiAdapter.notifyDataSetChanged();
                                for (int i = 0; i < daduiName.size(); i++) {
                                    if (info.brigade_name.equals(daduiName.get(i))) {
                                        pos = i;
                                        LogUtil.i("已经选择的大队名称为:"+daduiName.get(i)+"pos="+i);
                                        break;
                                    }
                                }
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (pos != -1) {
                                            dadui.setSelection(pos);
                                        }
                                    }
                                }, 300);
                                flag = 1;
                            } else if (type == 1) {
                                pos_zhongdui = -1;
                                zhongduiName.clear();
                                zhongduiId.clear();
                                zhongduiPath.clear();
                                zhongduiName.add("请选择");
                                for (InchargeInfo info :
                                        inchargeInfos) {
                                    zhongduiName.add(info.name);
                                    zhongduiPath.add(info.parent_id_path);
                                    zhongduiId.add(info.id);
                                }
                                zhongduiAdapter.notifyDataSetChanged();

                                for (int j = 0; j < zhongduiName.size(); j++) {
                                    if (info.group_name.equals(zhongduiName.get(j))) {
                                        pos_zhongdui = j;
                                        LogUtil.i("已经选择的中队队名称为:"+zhongduiName.get(j)+"pos_zhongdui="+j);
                                        break;
                                    }
                                }
                                LogUtil.i("zhongduipos="+pos_zhongdui);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (pos_zhongdui != -1) {
                                            liandongzhongdui.setSelection(pos_zhongdui);
                                        }else {
                                            liandongzhongdui.setSelection(0);
                                        }
                                    }
                                }, 200);
                            }

                        }
                    }
                });
    }


    private void sendBroadCast() {
        if (info != null) {
            if (!info.lat.equals(lat.getText().toString()) || !info.lng.equals(lng.getText().toString())) {
                Intent intent = new Intent();
                intent.setAction("net.suntrans.xiaofang.lp");
                intent.putExtra("type", MarkerHelper.FIREROOM);
                sendBroadcast(intent);
            }
        }
    }

    Handler handler = new Handler();

}
