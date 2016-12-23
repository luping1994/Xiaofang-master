package com.suntrans.xiaofang.fragment.editinfo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.model.firestation.AddFireStationResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
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
 * 政府专职小型站添加fragment
 */

public class Type3_fragment extends Fragment {
    Map<String, String> map;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.addr)
    EditText addr;
    @BindView(R.id.area)
    EditText area;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.servingnum)
    EditText servingnum;
    @BindView(R.id.fulltimenum)
    EditText fulltimenum;

    @BindView(R.id.carnum)
    EditText carnum;
    @BindView(R.id.cardisp)
    EditText cardisp;
    @BindView(R.id.waterweight)
    EditText waterweight;
    @BindView(R.id.soapweight)
    EditText soapweight;

    @BindView(R.id.street)
    EditText street;
    @BindView(R.id.community)
    EditText community;
    @BindView(R.id.group)
    EditText group;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.commit_station)
    Button commitStation;
    @BindView(R.id.district)
    Spinner district;
    @BindView(R.id.lng)
    EditText lng;
    @BindView(R.id.lat)
    EditText lat;
    private AlertDialog dialog;

    private String district1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type3, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();

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
    }


    @OnClick(R.id.commit_station)
    public void onClick() {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity()).setMessage("确定添加单位吗")
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
        String servingnum1 = servingnum.getText().toString();
        String fulltimenum1 = fulltimenum.getText().toString();
        String carnum1 = carnum.getText().toString();
        String cardisp1 = cardisp.getText().toString();
        String waterweight1 = waterweight.getText().toString();
        String soapweight1 = soapweight.getText().toString();

        String street1 = street.getText().toString();
        String community1 = community.getText().toString();
        String group1 = group.getText().toString();

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
        if (Utils.isVaild(servingnum1)) {
            builder.put("servingnum", servingnum1.replace(" ", ""));
        }

        if (Utils.isVaild(fulltimenum1)) {
            builder.put("fulltimenum", fulltimenum1.replace(" ", ""));
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

        if (Utils.isVaild(street1)) {
            builder.put("street", street1.replace(" ", ""));
        }
        if (Utils.isVaild(community1)) {
            builder.put("community", community1.replace(" ", ""));
        }
        if (Utils.isVaild(group1)) {
            builder.put("group", group1.replace(" ", ""));
        }
        map = builder.build();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
        RetrofitHelper.getApi().createStation(map)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddFireStationResult>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(AddFireStationResult result) {
                        if (result != null) {
                            LogUtil.i(result.status);
                            if (TextUtils.equals("1",result.status)) {
                                String result1 = result.result;
                                LogUtil.i("resultweikong添加失败");

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
                                },500);
                            }else {
                            }
                        }else {
                            LogUtil.i("添加失败");
                        }
                    }
                });

    }

    Handler handler = new Handler();
}
