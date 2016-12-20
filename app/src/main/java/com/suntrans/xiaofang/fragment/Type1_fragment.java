package com.suntrans.xiaofang.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.Add_detail_activity;
import com.suntrans.xiaofang.model.company.AddCompanyResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Looney on 2016/12/13.
 */

public class Type1_fragment extends Fragment {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.addr)
    EditText addr;
    @BindView(R.id.incharge)
    EditText incharge;
    @BindView(R.id.fire_dan)
    TextView fireDan;
    @BindView(R.id.dangerlevel)
    RadioGroup dangerlevel;
    @BindView(R.id.buildarea)
    EditText buildarea;
    @BindView(R.id.exitnum)
    EditText exitnum;
    @BindView(R.id.stairnum)
    EditText stairnum;
    @BindView(R.id.hasfacility)
    RadioGroup hasfacility;
    @BindView(R.id.attribute)
    TextView attribute;
    @BindView(R.id.artiname)
    EditText artiname;
    @BindView(R.id.artiid)
    EditText artiid;
    @BindView(R.id.artiphone)
    EditText artiphone;
    @BindView(R.id.managername)
    EditText managername;
    @BindView(R.id.managerid)
    EditText managerid;
    @BindView(R.id.managerphone)
    EditText managerphone;
    @BindView(R.id.responname)
    EditText responname;
    @BindView(R.id.responid)
    EditText responid;
    @BindView(R.id.responphone)
    EditText responphone;
    @BindView(R.id.orgid)
    EditText orgid;
    @BindView(R.id.leaderdepart)
    EditText leaderdepart;
    @BindView(R.id.foundtime)
    EditText foundtime;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.straffnum)
    EditText straffnum;
    @BindView(R.id.area)
    EditText area;
    @BindView(R.id.lanenum)
    EditText lanenum;
    @BindView(R.id.elevatornum)
    EditText elevatornum;
    @BindView(R.id.fire_type)
    TextView fireType;
    @BindView(R.id.refugenum)
    EditText refugenum;
    @BindView(R.id.refugepos)
    EditText refugepos;
    @BindView(R.id.east)
    EditText east;
    @BindView(R.id.south)
    EditText south;
    @BindView(R.id.west)
    EditText west;
    @BindView(R.id.north)
    EditText north;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.commit)
    Button commit;
    @BindView(R.id.getposition)
    Button getposition;
    @BindView(R.id.have)
    RadioButton have;
    @BindView(R.id.no)
    RadioButton no;
    @BindView(R.id.content1)
    LinearLayout content1;
    @BindView(R.id.marker)
    EditText marker;
    private ArrayList<HashMap<String, String>> datas = new ArrayList<>();
    ProgressDialog progressDialog = new ProgressDialog(UiUtils.getContext());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type1, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dialog = new ProgressDialog(UiUtils.getContext());
        dialog.setMessage("添加中");
        progressDialog.setMessage("获取中");
        getposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                GeocodeSearch geocodeSearch = new GeocodeSearch(getActivity());
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(((Add_detail_activity) getActivity()).latLng.latitude, ((Add_detail_activity) getActivity()).latLng.longitude), 200, GeocodeSearch.AMAP);
                geocodeSearch.getFromLocationAsyn(query);
                geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                    @Override
                    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                        progressDialog.dismiss();
                        if (rCode == 1000) {
                            if (result != null && result.getRegeocodeAddress() != null
                                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                                String addressName = result.getRegeocodeAddress().getFormatAddress();
                                addr.setText(addressName);
                            } else {

                            }
                        } else {
                        }
                    }

                    @Override
                    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    Map<String, String> map;
    ProgressDialog dialog;

    @OnClick(R.id.commit)
    public void onClick() {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        map = null;
        if (name.getText().equals("") || addr.getText().equals("")) {
            UiUtils.showToast(UiUtils.getContext(), "带*号的项目不不能为空!");
            return;
        }
        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();

        int checkedid = dangerlevel.getCheckedRadioButtonId();
        String incharge1 = incharge.getText().toString();
        String dangerlevels = "";
        if (checkedid == R.id.dangerlevel1) {
            dangerlevels = "1";
        } else if (checkedid == R.id.dangerlevel2) {
            dangerlevels = "2";
        }

        String buildarea1 = buildarea.getText().toString();
        String exitnum1 = exitnum.getText().toString();
        String stairnum1 = stairnum.getText().toString();

        String artiname1 = artiname.getText().toString();
        String artiid1 = artiid.getText().toString();
        String artiphone1 = artiphone.getText().toString();
        String managername1 = managername.getText().toString();
        String managerid1 = managerid.getText().toString();
        String managerphone1 = managerphone.getText().toString();
        String responname1 = responname.getText().toString();
        String responid1 = responid.getText().toString();
        String responphone1 = responphone.getText().toString();
        String orgid1 = orgid.getText().toString();
        String leaderdepart1 = leaderdepart.getText().toString();
        String foundtime1 = foundtime.getText().toString();
        String phone1 = phone.getText().toString();
        String staffnum1 = straffnum.getText().toString();
        String area1 = area.getText().toString();
        String lanenum1 = lanenum.getText().toString();
        String elevatornum1 = elevatornum.getText().toString();
        String refugenum1 = refugenum.getText().toString();
        String refugepos1 = refugepos.getText().toString();
        String east1 = east.getText().toString();
        String south1 = south.getText().toString();
        String north1 = north.getText().toString();
        String west1 = west.getText().toString();
        String remark1 = marker.getText().toString();

        if (name1 != null) {
            name1 = name1.replace(" ", "");
            if (!TextUtils.equals(" ", name1))
                builder.put("name", name1);
        }
        if (addr1 != null) {
            addr1 = addr1.replace(" ", "");
            if (!TextUtils.equals(" ", addr1))
                builder.put("addr", addr1);
        }

        builder.put("cmystate", "0");

        if (incharge1 != null) {
            incharge1 = incharge1.replace(" ", "");
            if (!TextUtils.equals("", incharge1))
                builder.put("incharge", incharge1);
        }

        if (dangerlevels != null) {
            dangerlevels = dangerlevels.replace(" ", "");
            if (!TextUtils.equals("", dangerlevels))
                builder.put("dangerlevel", dangerlevels);
        }

        if (buildarea1 != null) {
            buildarea1 = buildarea1.replace(" ", "");
            if (!TextUtils.equals("", buildarea1))
                builder.put("buildarea", buildarea1);
        }

        if (exitnum1 != null) {
            exitnum1 = exitnum1.replace(" ", "");
            if (!TextUtils.equals("", exitnum1))
                builder.put("exitnum", exitnum1);
        }

        if (stairnum1 != null) {
            stairnum1 = stairnum1.replace(" ", "");
            if (!TextUtils.equals("", stairnum1))
                builder.put("stairnum", stairnum.getText().toString());
        }

        int id = hasfacility.getCheckedRadioButtonId();
        String hasfacility1 = "";
        if (id == R.id.have) {
            hasfacility1 = "1";
        } else if (id == R.id.no) {
            hasfacility1 = "0";
        }
        hasfacility1 = hasfacility1.replace(" ", "");
        if (!TextUtils.equals("", hasfacility1))
            builder.put("hasfacility", hasfacility1);//备注单位属性未添加


        if (artiname1 != null) {
            artiname1 = artiname1.replace(" ", "");
            if (!TextUtils.equals("", artiname1))
                builder.put("artiname", artiname1);

        }
        if (artiid1 != null) {
            artiid1 = artiid1.replace(" ", "");
            if (!TextUtils.equals("", artiid1))
                builder.put("artiid", artiid1);
        }
        if (artiphone1 != null) {
            artiphone1 = artiphone1.replace(" ", "");
            if (!TextUtils.equals("", artiphone1))
                builder.put("artiphone", artiphone1);

        }
        if (managername1 != null) {
            managername1 = managername1.replace(" ", "");
            if (!TextUtils.equals("", managername1))
                builder.put("managername", managername1);
        }

        if (managerid1 != null) {
            managerid1 = managerid1.replace(" ", "");
            if (!TextUtils.equals("", managerid1))
                builder.put("managerid", managerid1);
        }

        if (managerphone1 != null) {
            managerphone1 = managerphone1.replace(" ", "");
            if (!TextUtils.equals("", managerphone1))
                builder.put("managerphone", managerphone1);
        }
        if (responname1 != null) {
            responname1 = responname1.replace(" ", "");
            if (!TextUtils.equals("", responname1))
                builder.put("responname", responname1);
        }

        if (responid1 != null) {
            responid1 = responid1.replace(" ", "");
            if (!TextUtils.equals("", responid1))
                builder.put("responid", responid1);
        }

        if (responphone1 != null) {
            responphone1 = responphone1.replace(" ", "");
            if (!TextUtils.equals("", responphone1))
                builder.put("responphone", responphone1);
        }
        if (orgid1 != null) {
            orgid1 = orgid1.replace(" ", "");
            if (!TextUtils.equals("", orgid1))
                builder.put("orgid", orgid1);
        }

        if (leaderdepart1 != null) {
            leaderdepart1 = leaderdepart1.replace(" ", "");
            if (!TextUtils.equals("", leaderdepart1))
                builder.put("leaderdepart", leaderdepart1);

        }
        if (foundtime1 != null) {
            foundtime1 = foundtime1.replace(" ", "");
            if (!TextUtils.equals("", foundtime1))
                builder.put("foundtime", foundtime1);
        }
        if (phone1 != null) {
            phone1 = phone1.replace(" ", "");
            if (!TextUtils.equals("", phone1))
                builder.put("phone", phone1);

        }

        if (staffnum1 != null) {
            staffnum1 = staffnum1.replace(" ", "");
            if (!TextUtils.equals("", staffnum1))
                builder.put("staffnum", staffnum1);
        }

        if (area1 != null) {
            area1 = area1.replace(" ", "");
            if (!TextUtils.equals("", area1))
                builder.put("area", area1);
        }

        if (lanenum1 != null) {
            lanenum1 = lanenum1.replace(" ", "");
            if (!TextUtils.equals("", lanenum1))
                builder.put("lanenum", lanenum1);

        }
        if (elevatornum1 != null) {
            elevatornum1 = elevatornum1.replace(" ", "");
            if (!TextUtils.equals("", elevatornum1))
                builder.put("elevatornum", elevatornum1);//消防车道类型为添加
        }

        if (refugenum1 != null) {
            refugenum1 = refugenum1.replace(" ", "");
            if (!TextUtils.equals("", refugenum1))
                builder.put("refugenum", refugenum1);
        }

        if (refugepos1 != null) {
            refugepos1 = refugepos1.replace(" ", "");
            if (!TextUtils.equals("", refugepos1))
                builder.put("refugepos", refugepos1);

        }

        if (east1 != null) {
            east1 = east1.replace(" ", "");
            if (!TextUtils.equals("", east1))
                builder.put("east", east1);

        }

        if (south1 != null) {
            south1 = south1.replace(" ", "");
            if (!TextUtils.equals("", south1))
                builder.put("south", south1);

        }

        if (west1 != null) {
            west1 = west1.replace(" ", "");
            if (!TextUtils.equals("", west1))
                builder.put("west", west1);

        }
        if (north1 != null) {
            north1 = north1.replace(" ", "");
            if (!TextUtils.equals("", north1))
                builder.put("north", north1);

        }

        if (remark1 != null) {
            remark1 = remark1.replace(" ", "");
            if (!TextUtils.equals("", remark1))
                builder.put("remark", remark1);

        }


        map = builder.build();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
        RetrofitHelper.getApi().createCompany(map).enqueue(new Callback<AddCompanyResult>() {
            @Override
            public void onResponse(Call<AddCompanyResult> call, Response<AddCompanyResult> response) {
                System.out.println("message" + response.message());
                System.out.println("body" + response.body());
                System.out.println("raw" + response.raw());
                AddCompanyResult result = response.body();
                try {
                    if (result != null) {
                        UiUtils.showToast(UiUtils.getContext(), "提示:" + result.result);
                    } else {
                        UiUtils.showToast(UiUtils.getContext(), result.msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<AddCompanyResult> call, Throwable t) {
                UiUtils.showToast(UiUtils.getContext(), "添加单位失败!");
                LogUtil.e(t.toString());
            }
        });

    }

    private boolean isValid(String str) {
        if (str != null) {
            str = str.replace(" ", "");
            if (!TextUtils.equals("", str))
                return true;
        }
        return false;
    }
}
