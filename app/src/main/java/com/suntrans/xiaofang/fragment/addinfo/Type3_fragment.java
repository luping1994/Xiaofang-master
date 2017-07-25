package com.suntrans.xiaofang.fragment.addinfo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.suntrans.xiaofang.activity.others.MapChoose_Activity;
import com.suntrans.xiaofang.model.company.InchargeInfo;
import com.suntrans.xiaofang.model.firestation.AddFireStationResult;
import com.suntrans.xiaofang.model.firestation.FireStationDetailInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

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
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/13.
 * 乡村专职消防队添加fragment
 */

public class Type3_fragment extends RxFragment implements View.OnClickListener {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.addr)
    EditText addr;
    @BindView(R.id.area)
    EditText area;
    @BindView(R.id.phone)
    EditText phone;


    @BindView(R.id.carnum)
    EditText carnum;

    @BindView(R.id.waterweight)
    EditText waterweight;
    @BindView(R.id.soapweight)
    EditText soapweight;

    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.commit_station)
    Button commitStation;

    @BindView(R.id.lng)
    EditText lng;
    @BindView(R.id.lat)
    EditText lat;
    @BindView(R.id.getposition)
    Button getposition;


    @BindView(R.id.content1)
    LinearLayout content1;

    @BindView(R.id.ll_condition)
    LinearLayout llCondition;


    @BindView(R.id.ganbu)
    EditText ganbu;
    @BindView(R.id.shibing)
    EditText shibing;
    @BindView(R.id.zhuanzhi)
    EditText zhuanzhi;


    @BindView(R.id.dadui)
    Spinner dadui;
    @BindView(R.id.liandongzhongdui)
    Spinner liandongzhongdui;

    private AlertDialog dialog;


    private ArrayAdapter<String> adduiAdapter;
    private ArrayAdapter<String> zhongduiAdapter;


    List<String> daduiName;
    List<String> daduiId;
    List<String> daduiIdPath;

    List<String> zhongduiName;
    List<String> zhongduiId;
    List<String> zhongduiPath;


    private String dadui_id_path;
    private String zhongdui_id_path;

    int flag = 0;
    private int type;

    public static Type3_fragment newInstance(int stationType) {
        Type3_fragment fragment = new Type3_fragment();
        Bundle args = new Bundle();
        args.putInt("type", stationType);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type3, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initData();
    }

    private void initView(View view) {
        Button add = (Button) view.findViewById(R.id.add);
        add.setOnClickListener(this);
        daduiName = new ArrayList<>();
        daduiId = new ArrayList<>();
        daduiIdPath = new ArrayList<>();

        zhongduiName = new ArrayList<>();
        zhongduiId = new ArrayList<>();
        zhongduiPath = new ArrayList<>();


        adduiAdapter = new ArrayAdapter(getActivity(), R.layout.item_spinner, R.id.tv_spinner, daduiName);
        zhongduiAdapter = new ArrayAdapter(getActivity(), R.layout.item_spinner, R.id.tv_spinner, zhongduiName);

        dadui.setAdapter(adduiAdapter);
        liandongzhongdui.setAdapter(zhongduiAdapter);

        dadui.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (flag == 1) {
                    if (position == 0) {
                        dadui_id_path = null;
                        zhongduiName.clear();
                        zhongduiName.add("请选择");
                        zhongduiAdapter.notifyDataSetChanged();
                        liandongzhongdui.setSelection(0);
                        return;
                    }
                    dadui_id_path = daduiIdPath.get(position - 1);
                    String nextId = daduiId.get(position - 1);
                    zhongduiName.clear();
                    zhongduiName.add("请选择");
                    zhongduiAdapter.notifyDataSetChanged();
                    liandongzhongdui.setSelection(0);
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        getIncharge("0", 0, "0");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                final View item = LayoutInflater.from(getContext()).inflate(R.layout.item_adddis, null, false);
                item.findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getActivity()).setMessage("是否删除")
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
        }
    }

    private void initData() {
        getposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapChoose_Activity.class);
                startActivityForResult(intent, 601);
                getActivity().overridePendingTransition(android.support.design.R.anim.abc_slide_in_bottom, android.support.design.R.anim.abc_slide_out_bottom);
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

    @OnClick(R.id.commit_station)
    public void onClick() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("确定添加单位吗")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        addCommit();
//
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//        builder.create().show();
    }

    public void addCommit() {
        Map<String, String> map = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();


        String area1 = area.getText().toString();
        String phone1 = phone.getText().toString();

        String carnum1 = carnum.getText().toString();

        String cardisp1 = "";
        JSONObject jsonObject2 = new JSONObject();

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
                    jsonObject2.put(type, detail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cardisp1 = jsonObject2.toString();


        String waterweight1 = waterweight.getText().toString();
        String soapweight1 = soapweight.getText().toString();


        String membernum1 = "";
        String ganbunum = ganbu.getText().toString();
        String shibingnum = shibing.getText().toString();
        String zhuanzhinum = zhuanzhi.getText().toString();


        if (!Utils.isVaild(name1)) {
            UiUtils.showToast(UiUtils.getContext(), "名称不能为空!");
            return;
        }
        if (!Utils.isVaild(addr1)) {
            UiUtils.showToast(UiUtils.getContext(), "地址不能为空!");
            return;
        }
        if (!Utils.isVaild(area1)) {
            UiUtils.showToast("请输入辖区面积!");
            return;
        }
        if (!Utils.isVaild(phone1)) {
            UiUtils.showToast("请输入联系电话!");
            return;
        }
        if (!Utils.isVaild(ganbunum) || !Utils.isVaild(shibingnum) || !Utils.isVaild(zhuanzhinum)) {
            UiUtils.showToast("请输入人员组成");
            return;
        }
        if (!Utils.isVaild(carnum1)) {
            UiUtils.showToast("请输入消防车总数");
            return;
        }

        if (jsonObject2.length() == 0) {
            UiUtils.showToast("请至少添加一条车辆配置情况");
            return;
        }

        if (!Utils.isVaild(waterweight1)) {
            UiUtils.showToast("请输入车载水总量!");
            return;
        }
        if (!Utils.isVaild(soapweight1)) {
            UiUtils.showToast("请输入车载泡沫总量!");
            return;
        }


        if (!Utils.isVaild(dadui_id_path)) {
            UiUtils.showToast("请选择消防大队");
            return;
        }

        if (zhongduiName.size() >1) {
            if (!Utils.isVaild(zhongdui_id_path)) {
                UiUtils.showToast("请选择联动中队");
                return;
            }
        }


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("干部", ganbunum);
            jsonObject.put("士兵", shibingnum);
            jsonObject.put("专职", zhuanzhinum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        membernum1 = jsonObject.toString();


        builder.put("name", name1.replace(" ", ""));

        builder.put("addr", addr1.replace(" ", ""));

        builder.put("membernum", membernum1);

        if (Utils.isVaild(lng1)) {
            builder.put("lng", lng1.replace(" ", ""));
        }
        if (Utils.isVaild(lat1)) {
            builder.put("lat", lat1.replace(" ", ""));
        }
        builder.put("area", area1.replace(" ", ""));

        builder.put("phone", phone1.replace(" ", ""));


        builder.put("carnum", carnum1.replace(" ", ""));

        builder.put("cardisp", cardisp1.replace(" ", ""));
        builder.put("waterweight", waterweight1.replace(" ", ""));

        builder.put("soapweight", soapweight1.replace(" ", ""));


        builder.put("brigade_path", dadui_id_path);

        if (Utils.isVaild(zhongdui_id_path)) {
            builder.put("group_path", zhongdui_id_path);
        }else {
            builder.put("group_path", dadui_id_path+"-0");

        }


        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            LogUtil.i(key + "," + value);
        }
        final ProgressDialog dialog1 = new ProgressDialog(getActivity());
        dialog1.setCancelable(false);
        dialog1.setMessage("正在添加,请稍后...");
        dialog1.show();
        if (type == MarkerHelper.FIRESTATION) {
            RetrofitHelper.getApi().createStation(map)
                    .compose(this.<AddFireStationResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))

                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<AddFireStationResult>() {
                        @Override
                        public void onCompleted() {
                            dialog1.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            UiUtils.showToast(UiUtils.getContext(), "添加失败!");
                            dialog1.dismiss();
                        }

                        @Override
                        public void onNext(AddFireStationResult result) {
                            if (result != null) {
                                LogUtil.i(result.status);
                                if (result.status.equals("1")) {
                                    sendBroadcast(CREATE);
                                    String result1 = result.result;
                                    dialog = new AlertDialog.Builder(getActivity())
                                            .setMessage(result1)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    getActivity().finish();
                                                }
                                            })
                                            .create();

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog1.dismiss();
                                            dialog.show();
                                        }
                                    }, 500);
                                } else if (result.status.equals("0")) {
                                    dialog1.dismiss();
                                    UiUtils.showToast(result.msg);
                                } else if (result.status.equals("-1")) {
                                    UiUtils.showToast("无添加权限");
                                    dialog1.dismiss();
                                } else {
                                    dialog1.dismiss();
                                }
                            } else {
                                dialog1.dismiss();
                                UiUtils.showToast(UiUtils.getContext(), "添加失败");
                            }
                        }
                    });
        } else if (type == MarkerHelper.FIREADMINSTATION) {
            RetrofitHelper.getApi().createFireAdminStation(map)
                    .compose(this.<AddFireStationResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))

                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<AddFireStationResult>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            UiUtils.showToast(UiUtils.getContext(), "添加失败!");
                            dialog1.dismiss();
                        }

                        @Override
                        public void onNext(AddFireStationResult result) {
                            if (result != null) {
                                LogUtil.i(result.status);
                                if (result.status.equals("1")) {
                                    sendBroadcast(CREATE);
                                    String result1 = result.result;
                                    dialog = new AlertDialog.Builder(getActivity())
                                            .setMessage(result1)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    getActivity().finish();
                                                }
                                            })
                                            .create();

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog1.dismiss();
                                            dialog.show();
                                        }
                                    }, 500);
                                } else if (result.status.equals("0")) {
                                    dialog1.dismiss();
                                    UiUtils.showToast(result.msg);
                                } else if (result.status.equals("-1")) {
                                    UiUtils.showToast("无添加权限");
                                    dialog1.dismiss();
                                } else {
                                    dialog1.dismiss();
                                }
                            } else {
                                dialog1.dismiss();
                                UiUtils.showToast(UiUtils.getContext(), "添加失败");
                            }
                        }
                    });
        }


    }

    Handler handler = new Handler();
    int pos = -1;
    int pos_zhongdui = -1;

    private void getIncharge(String pid, final int type, String vtype) {
        RetrofitHelper.getApi().getFireChargeArea(pid, vtype)
                .compose(this.<List<InchargeInfo>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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
                                for (InchargeInfo info :
                                        inchargeInfos) {
                                    daduiName.add(info.name);
                                    daduiId.add(info.id);
                                    daduiIdPath.add(info.parent_id_path);
                                }
                                adduiAdapter.notifyDataSetChanged();
                                if (info != null) {
                                    for (int i = 0; i < daduiName.size(); i++) {
                                        if (daduiName.get(i).equals(info.brigade_name)) {
                                            pos = i;
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
                                }
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
                                if (info != null) {
                                    for (int i = 0; i < zhongduiName.size(); i++) {
                                        if (zhongduiName.get(i).equals(info.group_name)) {
                                            pos_zhongdui = i;
                                            break;
                                        }
                                    }
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (pos_zhongdui != -1)
                                                liandongzhongdui.setSelection(pos_zhongdui);
                                            else {
                                                liandongzhongdui.setSelection(0);
                                            }
                                        }
                                    }, 300);
                                }
                            }

                        }
                    }
                });

    }

    public void updateFireStationInfo() {
        if (info == null) {
            UiUtils.showToast("修改失败");
            return;
        }
        if (!Utils.isVaild(info.id)) {
            UiUtils.showToast("无法获取当前单位信息");
            return;
        }
        Map<String, String> map = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();


        String area1 = area.getText().toString();
        String phone1 = phone.getText().toString();

        String carnum1 = carnum.getText().toString();

        String cardisp1 = "";
        JSONObject jsonObject2 = new JSONObject();

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
                    jsonObject2.put(type, detail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cardisp1 = jsonObject2.toString();


        String waterweight1 = waterweight.getText().toString();
        String soapweight1 = soapweight.getText().toString();


        String membernum1 = "";
        String ganbunum = ganbu.getText().toString();
        String shibingnum = shibing.getText().toString();
        String zhuanzhinum = zhuanzhi.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("干部", ganbunum);
            jsonObject.put("士兵", shibingnum);
            jsonObject.put("专职", zhuanzhinum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        membernum1 = jsonObject.toString();


        if (!Utils.isVaild(name1)) {
            UiUtils.showToast(UiUtils.getContext(), "名称不能为空!");
            return;
        }
        if (!Utils.isVaild(addr1)) {
            UiUtils.showToast(UiUtils.getContext(), "地址不能为空!");
            return;
        }
        if (!Utils.isVaild(area1)) {
            UiUtils.showToast("请输入辖区面积!");
            return;
        }
        if (!Utils.isVaild(phone1)) {
            UiUtils.showToast("请输入联系电话!");
            return;
        }
        if (!Utils.isVaild(ganbunum) || !Utils.isVaild(shibingnum) || !Utils.isVaild(zhuanzhinum)) {
            UiUtils.showToast("请输入人员组成");
            return;
        }
        if (!Utils.isVaild(carnum1)) {
            UiUtils.showToast("请输入消防车总数");
            return;
        }

        if (jsonObject2.length() == 0) {
            UiUtils.showToast("请至少添加一条车辆配置情况");
            return;
        }

        if (!Utils.isVaild(waterweight1)) {
            UiUtils.showToast("请输入车载水总量!");
            return;
        }
        if (!Utils.isVaild(soapweight1)) {
            UiUtils.showToast("请输入车载泡沫总量!");
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

        builder.put("brigade_path", dadui_id_path);

        if (Utils.isVaild(zhongdui_id_path)) {
            builder.put("group_path", zhongdui_id_path);
        }else {
            builder.put("group_path", dadui_id_path+"-0");

        }

        builder.put("name", name1.replace(" ", ""));

        builder.put("addr", addr1.replace(" ", ""));

        builder.put("membernum", membernum1);

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


        builder.put("id", info.id);

        map = builder.build();

//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            String key = entry.getKey().toString();
//            String value = entry.getValue().toString();
//            System.out.println(key + "," + value);
//        }
        if (type == MarkerHelper.FIRESTATION) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("正在修改,请稍后...");
            progressDialog.show();
            RetrofitHelper.getApi().updateFireStation(map)
                    .compose(this.<AddFireStationResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddFireStationResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            UiUtils.showToast(getContext(), "服务器错误!");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(AddFireStationResult result) {
                            progressDialog.dismiss();
                            try {
                                if (result == null) {
                                    UiUtils.showToast(getContext(), "修改信息失败!");
                                } else if (result.status.equals("1")) {
                                    sendBroadcast(UPDATE);
                                    new AlertDialog.Builder(getContext()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().finish();
                                        }
                                    }).setMessage(result.result).create().show();
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }
                    });
        } else if (type == MarkerHelper.FIREADMINSTATION) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("正在修改,请稍后...");
            progressDialog.show();
            RetrofitHelper.getApi().updateFireAdminStation(map)
                    .compose(this.<AddFireStationResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<AddFireStationResult>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            UiUtils.showToast(getContext(), "服务器错误!");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(AddFireStationResult result) {
                            progressDialog.dismiss();
                            try {
                                if (result == null) {
                                    UiUtils.showToast(getContext(), "修改信息失败!");
                                } else if (result.status.equals("1")) {
                                    sendBroadcast(UPDATE);
                                    new AlertDialog.Builder(getContext()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().finish();
                                        }
                                    }).setMessage(result.result).create().show();
                                } else {
                                    UiUtils.showToast(result.msg);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            }
                        }
                    });
        }

    }


    private FireStationDetailInfo info;

    public void setData(FireStationDetailInfo info) {
        if (info == null)
            return;
        getIncharge("0", 0, "0");
//        daduiName.add(info.brigade_path == null ? "请选择" : info.brigade_path+"(当前)");
//        zhongduiName.add(info.group_name == null ? "请选择" : "已选择(" + info.group_name + ")");
//        zhongduiAdapter.notifyDataSetChanged();
        this.info = info;


        name.setText(info.name == null ? "--" : info.name);
        addr.setText(info.addr == null ? "--" : info.addr);
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
            final View item = LayoutInflater.from(getActivity()).inflate(R.layout.item_adddis, null, false);
            ((EditText) item.findViewById(R.id.con_type)).setText(key);
            ((EditText) item.findViewById(R.id.con_detail)).setText(value);
            item.findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity()).setMessage("是否删除")
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


        String member = info.membernum;
        if (member != null && !member.equals("")) {
            try {
                JSONObject object = new JSONObject(member);
                String ganbu1 = object.optString("干部");
                String shibin1 = object.optString("士兵");
                String zhuanzhi1 = object.optString("专职");

                ganbu.setText(!Utils.isVaild(ganbu1) ? "" : ganbu1);
                shibing.setText(!Utils.isVaild(shibin1) ? "" : shibin1);
                zhuanzhi.setText(!Utils.isVaild(zhuanzhi1) ? "" : zhuanzhi1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private final int UPDATE = 2;
    private final int CREATE = 1;

    private void sendBroadcast(int contype) {
        if (contype == CREATE) {
            Intent intent = new Intent();
            intent.setAction("net.suntrans.xiaofang.lp");
            intent.putExtra("type", type);
            getActivity().sendBroadcast(intent);
        } else if (contype == UPDATE) {
            if (info != null&&info.lat!=null&&info.lng!=null) {
                if (!info.lat.equals(lat.getText().toString()) || !info.lng.equals(lng.getText().toString())) {
                    Intent intent = new Intent();
                    intent.setAction("net.suntrans.xiaofang.lp");
                    intent.putExtra("type", type);
                    getActivity().sendBroadcast(intent);
                }
            }
        }

    }


    @Override
    public void onDestroyView() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }
}
