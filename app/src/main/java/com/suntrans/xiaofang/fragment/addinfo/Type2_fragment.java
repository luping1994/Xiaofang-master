package com.suntrans.xiaofang.fragment.addinfo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.model.fireroom.AddFireRoomResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

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

    @BindView(R.id.membernum)
    EditText membernum;


    @BindView(R.id.district)
    Spinner district;

    @BindView(R.id.group)
    EditText group;

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
    private ImmutableMap.Builder<String, String> builder;
    private String district1;
    private AlertDialog dialog;

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
    }

    private void initView(View view) {
        Button add = (Button) view.findViewById(R.id.add);
        Button sub = (Button) view.findViewById(R.id.sub);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);
        addEq.setOnClickListener(this);
        subEq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                View item = LayoutInflater.from(getContext()).inflate(R.layout.item_adddis, null, false);
                llCondition.addView(item);
                break;
            case R.id.sub:
                if (llCondition.getChildCount()==1)
                    break;
                llCondition.removeViewAt(llCondition.getChildCount() - 1);
                break;
            case R.id.add_eq:
                View item2 = LayoutInflater.from(getContext()).inflate(R.layout.item_adddis, null, false);
                llConditionEq.addView(item2);
                break;
            case R.id.sub_eq:
                if (llConditionEq.getChildCount()==1)
                    break;
                llConditionEq.removeViewAt(llConditionEq.getChildCount() - 1);
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
        });
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


    private void addCommit() {
        map = null;
        builder = null;
        builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String contact1 = contact.getText().toString();
        String phone1 = phone.getText().toString();
        String membernum1 = membernum.getText().toString();

        String cardisp1 ="";
        for (int i =0;i<llCondition.getChildCount();i++){
            if (i==0)
                continue;
            View view = llCondition.getChildAt(i);
            EditText conType = (EditText) view.findViewById(R.id.con_type);
            EditText conDetail = (EditText) view.findViewById(R.id.con_detail);
            String type=conType.getText().toString();
            String detail=conDetail.getText().toString();
            cardisp1=new StringBuilder().append(cardisp1)
//                    .append("类型:")
                    .append(type)
                    .append(",")
//                    .append("详情:")
                    .append(detail)
                    .append(";")
                    .toString();
        }


        String equipdisp1 ="";
        for (int i =0;i<llConditionEq.getChildCount();i++){
            if (i==0)
                continue;
            View view = llConditionEq.getChildAt(i);
            EditText conType = (EditText) view.findViewById(R.id.con_type);
            EditText conDetail = (EditText) view.findViewById(R.id.con_detail);
            String type=conType.getText().toString();
            String detail=conDetail.getText().toString();
            equipdisp1=new StringBuilder().append(equipdisp1)
//                    .append("类型:")
                    .append(type)
                    .append(",")
//                    .append("详情:")
                    .append(detail)
                    .append(";")
                    .toString();
        }


        String group1 = group.getText().toString();
        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();

        if (name1.equals("") || name1 == null) {
            UiUtils.showToast(UiUtils.getContext(), "公司名称不不能为空!");
            return;
        }
        if (addr1 == null || addr1.equals("")) {
            UiUtils.showToast(UiUtils.getContext(), "公司地址不不能为空!");
            return;
        }

        if (name1 != null) {
            name1 = name1.replace(" ", "");
            if (!TextUtils.equals("", name1))
                builder.put("name", name1);
        }
        if (addr1 != null) {
            addr1 = addr1.replace(" ", "");
            if (!TextUtils.equals("", addr1))
                builder.put("addr", addr1);
        }
        if (contact1 != null) {
            contact1 = contact1.replace(" ", "");
            if (!TextUtils.equals("", contact1))
                builder.put("contact", contact1);
        }

        if (phone1 != null) {
            phone1 = contact1.replace(" ", "");
            if (!TextUtils.equals("", phone1)) {
                builder.put("phone", phone1);
            }
        }
        if (membernum1 != null) {
            membernum1 = membernum1.replace(" ", "");
            if (!TextUtils.equals("", membernum1))
                builder.put("membernum", membernum1);
        }

        if (cardisp1 != null) {
            cardisp1 = cardisp1.replace(" ", "");
            if (!TextUtils.equals("", cardisp1))
                builder.put("cardisp", cardisp1);
        }

        if (equipdisp1 != null) {
            equipdisp1 = equipdisp1.replace(" ", "");
            if (!TextUtils.equals("", equipdisp1))
                builder.put("equipdisp", equipdisp1);
        }
        builder.put("district", district1);

        if (group1 != null) {
            group1 = group1.replace(" ", "");
            if (!TextUtils.equals("", group1))
                builder.put("group", group1);
        }

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

        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
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
//                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(AddFireRoomResult result) {
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
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.show();
                                    }
                                }, 500);
                            } else {
                                UiUtils.showToast(result.msg);
                            }
                        } else {
                            UiUtils.showToast("添加失败");

                        }
                    }
                });
    }

    Handler handler = new Handler();


}
