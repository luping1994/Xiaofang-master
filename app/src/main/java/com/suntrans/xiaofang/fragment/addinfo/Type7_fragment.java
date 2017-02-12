package com.suntrans.xiaofang.fragment.addinfo;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.amap.api.services.core.PoiItem;
import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.others.MapChoose_Activity;
import com.suntrans.xiaofang.model.firestation.AddFireStationResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/13.
 * 政府专职小型站添加fragment(已经与乡村消防队合并)
 */
@Deprecated
public class Type7_fragment extends RxFragment implements View.OnClickListener {
    Map<String, String> map;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.addr)
    EditText addr;
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


    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type3, container, false);
        ButterKnife.bind(this, view);

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                final View item = LayoutInflater.from(getContext()).inflate(R.layout.item_adddis, null, false);
                item.findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llCondition.removeView(item);
                    }
                });
                llCondition.addView(item);
                break;
            case R.id.sub:
                if (llCondition.getChildCount() == 1)
                    break;
                llCondition.removeViewAt(llCondition.getChildCount() - 1);
                break;
        }
    }

    private void initData() {
//        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                district1 = getResources().getStringArray(R.array.area)[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
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
                name.setText(poiItem.getTitle());
                lat.setText(poiItem.getLatLonPoint().getLatitude() + "");
                lng.setText(poiItem.getLatLonPoint().getLongitude() + "");
                addr.setText(poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet());
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
        map = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();


        String area1 = area.getText().toString();
        String phone1 = phone.getText().toString();
//        String servingnum1 = servingnum.getText().toString();
//        String fulltimenum1 = fulltimenum.getText().toString();
        String carnum1 = carnum.getText().toString();
//        String hetong1 = hetong.getText().toString();

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
                jsonObject2.put("类型",type);
                jsonObject2.put("数量",detail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            cardisp1 = new StringBuilder().append(cardisp1)
//                    .append("类型:")
//                    .append(type)
//                    .append(",")
//                    .append("详情:")
//                    .append(detail)
//                    .append(";")
//                    .toString();
        }
        cardisp1 = jsonObject2.toString();
        String waterweight1 = waterweight.getText().toString();
        String soapweight1 = soapweight.getText().toString();
//
//        String street1 = street.getText().toString();
//        String community1 = community.getText().toString();
//        String group1 = group.getText().toString();


        if (name1.equals("") || name1 == null) {
            UiUtils.showToast(UiUtils.getContext(), "公司名称不不能为空!");
            return;
        }
        if (addr1 == null || addr1.equals("")) {
            UiUtils.showToast(UiUtils.getContext(), "公司地址不不能为空!");
            return;
        }

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
//        JSONObject jsonObject = new JSONObject();
//        if (Utils.isVaild(servingnum1)) {
//            builder.put("servingnum", servingnum1.replace(" ", ""));
//            try {
//                jsonObject.put("现役消防队员", servingnum1);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (Utils.isVaild(fulltimenum1)) {
//            builder.put("fulltimenum", fulltimenum1.replace(" ", ""));
//            try {
//                jsonObject.put("专职消防队员", servingnum1);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

//        builder.put("membernum", jsonObject.toString());

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
//        builder.put("district", district1);

//        if (Utils.isVaild(street1)) {
//            builder.put("street", street1.replace(" ", ""));
//        }
//        if (Utils.isVaild(community1)) {
//            builder.put("community", community1.replace(" ", ""));
//        }
//        if (Utils.isVaild(group1)) {
//            builder.put("group", group1.replace(" ", ""));
//        }
        builder.put("admindivi_path", "0-1-141");
        builder.put("brigade_path", "0-1");


        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }

        RetrofitHelper.getApi().createFireAdminStation(map)
                .compose(this.<AddFireStationResult>bindUntilEvent(FragmentEvent.DESTROY_VIEW))

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddFireStationResult>() {
                    @Override
                    public void onCompleted() {
//                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast(UiUtils.getContext(), "添加失败!");
//                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(AddFireStationResult result) {
                        if (result != null) {
                            LogUtil.i(result.status);
                            if (TextUtils.equals("1", result.status)) {
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
                                        dialog.show();
                                    }
                                }, 500);
                            } else {
                                UiUtils.showToast(UiUtils.getContext(), result.msg);
                            }
                        } else {
                            UiUtils.showToast(UiUtils.getContext(), "添加失败");
                        }
                    }
                });

    }

    Handler handler = new Handler();


}
