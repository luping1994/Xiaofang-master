package com.suntrans.xiaofang.fragment.addinfo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.model.firegroup.AddFireGroupResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;

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

public class Type4_fragment extends Fragment {
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
    @BindView(R.id.membernum)
    EditText membernum;
    @BindView(R.id.carnum)
    EditText carnum;
    @BindView(R.id.cardisp)
    EditText cardisp;
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
    private String district1;
    Map<String, String> map;
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type4, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
//                if (((Add_detail_activity)getActivity()).myLocation!=null){
//                    lng.setText(((Add_detail_activity)getActivity()).myLocation.longitude+"");
//                    lat.setText(((Add_detail_activity)getActivity()).myLocation.latitude+"");
//                    addr.setText(((Add_detail_activity)getActivity()).myaddr);
//                }else {
//                    Snackbar.make(scroll,"获取当前地址失败",Snackbar.LENGTH_SHORT).show();
//                }
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


    private void addCommit() {
        map = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();


        String area1 = area.getText().toString();
        String phone1 = phone.getText().toString();
        String membernum1 = membernum.getText().toString();
        String carnum1 = carnum.getText().toString();
        String cardisp1 = cardisp.getText().toString();
        String waterweight1 = waterweight.getText().toString();
        String soapweight1 = soapweight.getText().toString();

        String group1 = group.getText().toString();

        if (name1.equals("") || name1 == null) {
            UiUtils.showToast(UiUtils.getContext(), "公司名称不不能为空!");
            return;
        }
        if (addr1== null || addr1.equals("")) {
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
        if (Utils.isVaild(membernum1)) {
            builder.put("membernum", membernum1.replace(" ", ""));
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
        builder.put("district", district1);

        if (Utils.isVaild(group1)) {
            builder.put("group", group1.replace(" ", ""));
        }
        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }

        RetrofitHelper.getApi().createGroup(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddFireGroupResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        UiUtils.showToast(App.getApplication(),"服务器内部错误");
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
                            }else {
                                UiUtils.showToast(result.msg);
                            }
                        } else {
                            UiUtils.showToast("添加失败");
                        }
                    }
                });

    }
}
