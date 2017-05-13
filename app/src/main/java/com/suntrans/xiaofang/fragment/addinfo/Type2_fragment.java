package com.suntrans.xiaofang.fragment.addinfo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.amap.api.services.core.PoiItem;
import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.others.MapChoose_Activity;
import com.suntrans.xiaofang.model.company.InchargeInfo;
import com.suntrans.xiaofang.model.fireroom.AddFireRoomResult;
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
 */

public class Type2_fragment extends RxFragment implements View.OnClickListener {
    Map<String, String> map;

    @BindView(R.id.add_fireroom)
    Button addFireroom;

    @BindView(R.id.name)
    EditText name;

    @BindView(R.id.addr)
    EditText addr;

    @BindView(R.id.contact)
    EditText contact;

    @BindView(R.id.phone)
    EditText phone;


    @BindView(R.id.district)
    Spinner district;


    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.content1)
    LinearLayout content1;
    @BindView(R.id.lng)
    EditText lng;
    @BindView(R.id.lat)
    EditText lat;
    @BindView(R.id.getposition)
    Button getposition;
    @BindView(R.id.ll_condition)
    LinearLayout llCondition;


    @BindView(R.id.add_eq)
    Button addEq;
    @BindView(R.id.sub_eq)
    Button subEq;
    @BindView(R.id.ll_condition_eq)
    LinearLayout llConditionEq;
    @BindView(R.id.dadui)
    Spinner dadui;
    @BindView(R.id.liandongzhongdui)
    Spinner liandongzhongdui;
    //    @BindView(R.id.ganbu)
//    EditText ganbu;
//    @BindView(R.id.shibing)
//    EditText shibing;
//    @BindView(R.id.hetong)
//    EditText hetong;
    @BindView(R.id.membernum)
    EditText membernum;

    private ImmutableMap.Builder<String, String> builder;
    private String district1;
    private AlertDialog dialog;


    private ArrayAdapter<String> adduiAdapter;
    private ArrayAdapter<String> zhongduiAdapter;


    List<String> daduiName;
    List<String> daduiId;
    List<String> daduiIdPath;

    List<String> zhongduiName;
    List<String> zhongduiId;
    List<String> zhongduiPath;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        initData();
        getIncharge("0", 0, "0");
    }

    private String dadui_id_path;
    private String zhongdui_id_path;

    private void initView(View view) {
        Button add = (Button) view.findViewById(R.id.add);
        Button sub = (Button) view.findViewById(R.id.sub);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        addEq.setOnClickListener(this);
        subEq.setOnClickListener(this);


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
                        liandongzhongdui.setSelection(0);
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
                    System.out.println("中队" + zhongduiName.get(position - 1) + "==>" + zhongdui_id_path);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

            case R.id.add_eq:
                final View item2 = LayoutInflater.from(getContext()).inflate(R.layout.item_adddis, null, false);
                item2.findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getActivity()).setMessage("是否删除")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        llConditionEq.removeView(item2);
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
                    }
                });
                llConditionEq.addView(item2);
                break;

        }
    }

    private void initData() {
        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                district1 = getResources().getStringArray(R.array.area)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 601) {
            if (resultCode == -1) {
                PoiItem poiItem = data.getParcelableExtra("addrinfo");
//                name.setText(poiItem.getTitle());
                LogUtil.i("lat=" + poiItem.getLatLonPoint().getLatitude() + "lng=" + poiItem.getLatLonPoint().getLongitude());
                lat.setText(poiItem.getLatLonPoint().getLatitude() + "");
                lng.setText(poiItem.getLatLonPoint().getLongitude() + "");
//                addr.setText(poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet());
                addr.setText(poiItem.getSnippet());
            }
        }

    }

    @OnClick(R.id.add_fireroom)
    public void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("确定添加单位吗")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addCommit();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.create().show();
    }


    public void addCommit() {
        map = null;
        builder = null;
        builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String contact1 = contact.getText().toString();
        String phone1 = phone.getText().toString();
        String membernum1 = membernum.getText().toString();

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


        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();

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
            UiUtils.showToast("请选择消防大队");
            return;
        }
        if (zhongduiName.size() >1) {
            if (!Utils.isVaild(zhongdui_id_path)) {
                UiUtils.showToast("请选择联动中队");
                return;
            }
        }


        builder.put("name", name1);

        builder.put("addr", addr1);

        builder.put("contact", contact1);
        builder.put("phone", phone1);
        if (membernum1 != null) {
            membernum1 = membernum1.replace(" ", "");
            if (!TextUtils.equals("", membernum1))
                builder.put("membernum", membernum1);
        }

        if (jsonObject1.length() != 0) {

            builder.put("cardisp", cardisp1);
        }

        if (jsonObject2.length() != 0) {
            builder.put("equipdisp", equipdisp1);
        }
//        builder.put("district", district1);


        if (lng1 != null) {
            lng1 = lng1.replace(" ", "");
            if (!TextUtils.equals("", lng1))
                builder.put("lng", lng1);
        }

        if (lat1 != null) {
            lat1 = lat1.replace(" ", "");
            if (!TextUtils.equals("", lat1))
                builder.put("lat", lat1);
        }
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
        dialog1.setMessage("正在添加,请稍后...");
        dialog1.setCancelable(false);
        dialog1.show();
        RetrofitHelper.getApi().createFireRoom(map)
                .compose(this.<AddFireRoomResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddFireRoomResult>() {
                    @Override
                    public void onCompleted() {
//                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast(UiUtils.getContext(), "添加失败!");
                        dialog1.dismiss();
                    }

                    @Override
                    public void onNext(AddFireRoomResult result) {
                        if (result != null) {
                            if (result.status.equals("1")) {
                                String result1 = result.result;
                                sendBroadcast();
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
                            } else {
                                dialog1.dismiss();
                                UiUtils.showToast(result.msg);
                            }
                        } else {
                            dialog1.dismiss();
                            UiUtils.showToast("添加失败");
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

    int flag = 0;
    Handler handler = new Handler();

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
                                daduiName.add("请选择");
                                for (InchargeInfo info :
                                        inchargeInfos) {
                                    daduiName.add(info.name);
                                    daduiId.add(info.id);
                                    daduiIdPath.add(info.parent_id_path);
                                }
                                adduiAdapter.notifyDataSetChanged();
                                flag = 1;
                            } else if (type == 1) {
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
                            }

                        }
                    }
                });

    }


    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction("net.suntrans.xiaofang.lp");
        intent.putExtra("type", MarkerHelper.FIREROOM);
        getActivity().sendBroadcast(intent);
    }
}
