package com.suntrans.xiaofang.fragment.addinfo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.others.MapChoose_Activity;
import com.suntrans.xiaofang.model.company.InchargeInfo;
import com.suntrans.xiaofang.model.firegroup.AddFireGroupResult;
import com.suntrans.xiaofang.model.firegroup.FireGroupDetailInfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.suntrans.xiaofang.R.id.incharge_dadui;
import static com.suntrans.xiaofang.R.id.map;

/**
 * Created by Looney on 2016/12/13.
 * <p>
 * 消防中队
 */

public class Type4_fragment extends RxFragment {
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

    @BindView(R.id.carnum)
    EditText carnum;

    @BindView(R.id.waterweight)
    EditText waterweight;
    @BindView(R.id.soapweight)
    EditText soapweight;
    @BindView(R.id.dadui)
    Spinner dadui;
    //    @BindView(R.id.group)
//    EditText group;
    @BindView(R.id.scroll)
    ScrollView scroll;

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

    private String district1;
    private AlertDialog dialog;

    private ArrayAdapter<String> daduiAdapter;

    List<String> daduiName;
    List<String> daduiId;
    List<String> daduiIdPath;
    int flag = 0;
    private String pid;//需要提交的daduipath

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type4, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);

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

    private void initView(View view) {
        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View item = LayoutInflater.from(getContext()).inflate(R.layout.item_adddis, null, false);
                item.findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llCondition.removeView(item);
                    }
                });
                llCondition.addView(item);
            }
        });

        getposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapChoose_Activity.class);
                startActivityForResult(intent, 601);
                getActivity().overridePendingTransition(android.support.design.R.anim.abc_slide_in_bottom, android.support.design.R.anim.abc_slide_out_bottom);
            }
        });

        daduiName = new ArrayList<>();
        daduiId = new ArrayList<>();
        daduiIdPath = new ArrayList<>();

        daduiAdapter = new ArrayAdapter(getActivity(), R.layout.item_spinner, R.id.tv_spinner, daduiName);

        dadui.setAdapter(daduiAdapter);

        dadui.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (flag == 1) {
                    if (position == 0) {
                        pid = null;
                        return;
                    }
                    pid = daduiId.get(position - 1);
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
        LogUtil.i("fragmentOnResume");
    }

    public void addCommit() {
        Map<String, String> map;
        map = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();


        String area1 = area.getText().toString();
        String phone1 = phone.getText().toString();
        String carnum1 = carnum.getText().toString();

        String membernum1 = "";
        String ganbunum = ganbu.getText().toString();
        String shibingnum = shibing.getText().toString();
        String zhuanzhinum = zhuanzhi.getText().toString();

        String waterweight1 = waterweight.getText().toString();
        String soapweight1 = soapweight.getText().toString();
        String cardisp1 = "";
        if (!Utils.isVaild(name1)) {
            UiUtils.showToast("名称不不能为空!");
            return;
        }
        if (!Utils.isVaild(addr1)) {
            UiUtils.showToast("地址不不能为空!");
            return;
        }

        if (!Utils.isVaild(pid)) {
            UiUtils.showToast("请选择消防大队!");
            return;
        }
        if (!Utils.isVaild(ganbunum)) {
            UiUtils.showToast("请输入干部消防员人数");
            return;
        }

        if (!Utils.isVaild(shibingnum)) {
            UiUtils.showToast("请输入士兵人数");
            return;
        }
        if (!Utils.isVaild(zhuanzhinum)) {
            UiUtils.showToast("请输入专职消防员人数");
            return;
        }

        if (!Utils.isVaild(waterweight1)) {
            UiUtils.showToast("请输入车载水总量");
            return;
        }

        if (!Utils.isVaild(soapweight1)) {
            UiUtils.showToast("请输入车载泡沫总量");
            return;
        }
        if (!Utils.isVaild(carnum1)) {
            UiUtils.showToast("请输入消防车辆总数");
            return;
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
                jsonObject2.put(type, detail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        cardisp1 = jsonObject2.toString();


        builder.put("name", name1 + "中队".replace(" ", ""));
        builder.put("addr", addr1.replace(" ", ""));
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
        builder.put("membernum", membernum1);

        if (Utils.isVaild(carnum1)) {
            builder.put("carnum", carnum1.replace(" ", ""));
        }

        if (Utils.isVaild(cardisp1)) {
            builder.put("cardisp", cardisp1);
        }
        if (Utils.isVaild(waterweight1)) {
            builder.put("waterweight", waterweight1.replace(" ", ""));
        }

        if (Utils.isVaild(soapweight1)) {
            builder.put("soapweight", soapweight1.replace(" ", ""));
        }
        builder.put("pid", pid);
        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }

        RetrofitHelper.getApi().createGroup(map)
                .compose(this.<AddFireGroupResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddFireGroupResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast(App.getApplication(), "服务器内部错误");
                    }

                    @Override
                    public void onNext(AddFireGroupResult result) {
                        if (result != null) {
                            if (result.status.equals("1")) {
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
                                dialog.show();
                            } else {
                                UiUtils.showToast(result.msg);
                            }
                        } else {
                            UiUtils.showToast("添加失败");
                        }
                    }
                });

    }

    public void updateFireGroup() {
        Map<String, String> map1;

        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();


        String area1 = area.getText().toString();
        String phone1 = phone.getText().toString();
        String carnum1 = carnum.getText().toString();

        String membernum1 = "";
        String ganbunum = ganbu.getText().toString();
        String shibingnum = shibing.getText().toString();
        String zhuanzhinum = zhuanzhi.getText().toString();


        String cardisp1 = "";
        if (!Utils.isVaild(name1)) {
            UiUtils.showToast("公司名称不不能为空!");
            return;
        }
        if (!Utils.isVaild(addr1)) {
            UiUtils.showToast("公司地址不不能为空!");
            return;
        }

//        if (!Utils.isVaild(pid)) {
//            UiUtils.showToast("请选择消防中队!");
//            return;
//        }
        if (!Utils.isVaild(ganbunum)) {
            UiUtils.showToast("请输入干部消防员人数");
            return;
        }

        if (!Utils.isVaild(shibingnum)) {
            UiUtils.showToast("请输入士兵人数");
            return;
        }
        if (!Utils.isVaild(zhuanzhinum)) {
            UiUtils.showToast("请输入专职消防员人数");
            return;
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
                jsonObject2.put(type, detail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        cardisp1 = jsonObject2.toString();
        String waterweight1 = waterweight.getText().toString();
        String soapweight1 = soapweight.getText().toString();


        builder.put("name", name1.replace(" ", ""));
        builder.put("addr", addr1.replace(" ", ""));
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
        builder.put("membernum", membernum1);

        if (Utils.isVaild(carnum1)) {
            builder.put("carnum", carnum1.replace(" ", ""));
        }

        if (Utils.isVaild(cardisp1)) {
            builder.put("cardisp", cardisp1);
        }
        if (Utils.isVaild(waterweight1)) {
            builder.put("waterweight", waterweight1.replace(" ", ""));
        }

        if (Utils.isVaild(soapweight1)) {
            builder.put("soapweight", soapweight1.replace(" ", ""));
        }
        if (Utils.isVaild(pid)) {
            builder.put("pid", pid.replace(" ", ""));
        }
        builder.put("id", info.id);

        map1 = builder.build();

        for (Map.Entry<String, String> entry : map1.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("正在修改请稍后");
        dialog.show();
        RetrofitHelper.getApi().updateFireGroup(map1)
                .compose(this.<AddFireGroupResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddFireGroupResult>() {
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
                    public void onNext(AddFireGroupResult result) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        try {
                            if (result == null) {
                                UiUtils.showToast("修改单位信息失败!");
                            } else {
                                UiUtils.showToast("提示:" + result.result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

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
                                daduiName.clear();
                                daduiId.clear();
                                daduiIdPath.clear();
                                if (info != null) {
                                    if (Utils.isVaild(info.brigade_name))
                                        daduiName.add("已选择(" + info.brigade_name + ")");
                                    else
                                        daduiName.add("请选择");
                                } else {
                                    daduiName.add("请选择");
                                }
                                for (InchargeInfo info :
                                        inchargeInfos) {
                                    daduiName.add(info.name);
                                    daduiId.add(info.id);
                                    daduiIdPath.add(info.parent_id_path);
                                }
                                daduiAdapter.notifyDataSetChanged();
                                flag = 1;
                            } else if (type == 1) {

                            }

                        }
                    }
                });

    }

    boolean isAdd = true;
    private FireGroupDetailInfo info;

    public void setData(FireGroupDetailInfo info) {
        this.info = info;
        isAdd = false;
        name.setText(info.name == null ? "--" : info.name);
        addr.setText(info.addr == null ? "--" : info.addr);
        lng.setText(info.lng == null ? "--" : info.lng);
        lat.setText(info.lat == null ? "--" : info.lat);

        area.setText(info.area == null ? "--" : info.area);
        phone.setText(info.phone == null ? "--" : info.phone);

        carnum.setText(info.carnum == null ? "--" : info.carnum);
        waterweight.setText(info.waterweight == null ? "--" : info.waterweight);
        soapweight.setText(info.soapweight == null ? "--" : info.soapweight);

        String membernum = info.membernum;
        if (membernum != null) {
            try {
                membernum = membernum.replace(" ", "");
                JSONObject obj = new JSONObject(membernum);
                String ganbunum = obj.optString("干部");
                String shibingnum = obj.optString("士兵");
                String zhuanzhinum = obj.optString("专职");

                ganbu.setText(ganbunum == null ? "" : ganbunum);
                shibing.setText(shibingnum == null ? "" : shibingnum);
                zhuanzhi.setText(zhuanzhinum == null ? "" : zhuanzhinum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


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
                    llCondition.removeView(item);
                }
            });
            llCondition.addView(item);
        }
    }


}
