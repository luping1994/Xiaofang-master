package com.suntrans.xiaofang.fragment.addinfo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.amap.api.services.core.PoiItem;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.others.MapChoose_Activity;
import com.suntrans.xiaofang.model.firegroup.AddFireGroupResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import org.json.JSONArray;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tencent.bugly.beta.ui.h.u;

/**
 * Created by Looney on 2016/12/13.
 * <p>
 * 新增消防大队
 */

public class Type6_fragment extends RxFragment {
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
    @BindView(R.id.district)
    Spinner district;
    @BindView(R.id.group)
    EditText group;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.commit_group)
    Button commitGroup;
    @BindView(R.id.getposition)
    Button getposition;
    @BindView(R.id.content1)
    LinearLayout content1;

    @BindView(R.id.ll_condition)
    LinearLayout llCondition;
    @BindView(R.id.phone1)
    EditText phoneOne;
    @BindView(R.id.phone2)
    EditText phoneTwo;
    @BindView(R.id.xianyiganbu)
    EditText xianyiganbu;
    @BindView(R.id.gonganganbu)
    EditText gonganganbu;
    @BindView(R.id.zhuanzhi)
    EditText zhuanzhi;
    @BindView(R.id.wenyuan)
    EditText wenyuan;

    private String district1;
    Map<String, String> map;
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type6, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);

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
                lat.setText(poiItem.getLatLonPoint().getLatitude() + "");
                lng.setText(poiItem.getLatLonPoint().getLongitude() + "");
                addr.setText(poiItem.getSnippet());
            }
        }

    }

    private void initView(View view) {
        Button add = (Button) view.findViewById(R.id.add);
        Button sub = (Button) view.findViewById(R.id.sub);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View item = LayoutInflater.from(getContext()).inflate(R.layout.item_adddis, null, false);
                llCondition.addView(item);
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llCondition.getChildCount() == 1)
                    llCondition.removeViewAt(llCondition.getChildCount() - 1);
            }
        });

    }

    @OnClick(R.id.commit_group)
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
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();


        String area1 = area.getText().toString();
        String phone1 = phone.getText().toString();
        String phone2 = phoneOne.getText().toString();
        String phone3 = phoneTwo.getText().toString();
//        String membernum1 = membernum.getText().toString();
        String carnum1 = carnum.getText().toString();


        String xianyiganbu1 = xianyiganbu.getText().toString();
        String gonganganbu1 = gonganganbu.getText().toString();
        String zhuanzhi1 = zhuanzhi.getText().toString();
        String wenyuan1 = wenyuan.getText().toString();


        String cardisp1 = "";
        for (int i = 0; i < llCondition.getChildCount(); i++) {
            if (i == 0)
                continue;
            View view = llCondition.getChildAt(i);
            EditText conType = (EditText) view.findViewById(R.id.con_type);
            EditText conDetail = (EditText) view.findViewById(R.id.con_detail);
            String type = conType.getText().toString();
            String detail = conDetail.getText().toString();
            cardisp1 = new StringBuilder().append(cardisp1)
                    .append("类型:")
                    .append(type)
                    .append(",")
                    .append("详情:")
                    .append(detail)
                    .append(";")
                    .toString();
        }


        String waterweight1 = waterweight.getText().toString();
        String soapweight1 = soapweight.getText().toString();

        String group1 = group.getText().toString();

        if (!Utils.isVaild(name1)) {
            UiUtils.showToast(UiUtils.getContext(), "名称不不能为空!");
            return;
        }
        if (addr1 == null || addr1.equals("")) {
            UiUtils.showToast( "地址不不能为空!");
            return;
        }

        if (!Utils.isVaild(phone2)&&!Utils.isVaild(phone2)&&!Utils.isVaild(phone3)) {
            UiUtils.showToast("请输入联系电话");
            return;
        }

        if (!Utils.isVaild(area1)){
            UiUtils.showToast("请输入辖区面积");
            return;
        }

        String membernum1 = "";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("现役干部", xianyiganbu1);
        jsonObject.addProperty("公安干部", gonganganbu1);
        jsonObject.addProperty("专职消防员", zhuanzhi1);
        jsonObject.addProperty("消防文员", wenyuan1);
        membernum1 = jsonObject.toString();


        if (!Utils.isVaild(membernum1)){
            UiUtils.showToast("请输入人员组成");
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

        JSONArray array = new JSONArray();
        array.put(phone1);
        array.put(phone2);
        array.put(phone3);
        builder.put("phone",array.toString());


        if (Utils.isVaild(membernum1)) {
            builder.put("membernum", membernum1);
        }


        if (Utils.isVaild(carnum1)) {
            builder.put("carnum", carnum1.replace(" ", ""));
        }

//        if (Utils.isVaild(cardisp1)) {
//            builder.put("cardisp", cardisp1.replace(" ", ""));
//        }
        if (Utils.isVaild(waterweight1)) {
            builder.put("waterweight", waterweight1.replace(" ", ""));
        }

        if (Utils.isVaild(soapweight1)) {
            builder.put("soapweight", soapweight1.replace(" ", ""));
        }
//        builder.put("district", district1);

        if (Utils.isVaild(group1)) {
            builder.put("group", group1.replace(" ", ""));
        }
        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }

        RetrofitHelper.getApi().createFirebrigade(map)
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


}
