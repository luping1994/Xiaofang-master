package com.suntrans.xiaofang.fragment.addinfo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.model.company.AddCompanyResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.suntrans.xiaofang.views.dialog.AttrSelector;
import com.suntrans.xiaofang.views.dialog.BottomDialog;
import com.suntrans.xiaofang.views.dialog.DefaultProvider;
import com.suntrans.xiaofang.views.dialog.GeneralProvider;
import com.suntrans.xiaofang.views.dialog.MainAttr;
import com.suntrans.xiaofang.views.dialog.SubAttr;
import com.suntrans.xiaofang.views.dialog.onAttrSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Looney on 2016/12/13.
 * 增加社会单位fragment
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
//


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
    TextView foundtime;
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

    @BindView(R.id.content1)
    LinearLayout content1;
    @BindView(R.id.marker)
    EditText marker;
    @BindView(R.id.lng)
    EditText lng;
    @BindView(R.id.lat)
    EditText lat;


    //    private ArrayList<HashMap<String, String>> datas = new ArrayList<>();
    ProgressDialog progressDialog = new ProgressDialog(UiUtils.getContext());
    @BindView(R.id.one)
    CheckBox one;
    @BindView(R.id.two)
    CheckBox two;
    @BindView(R.id.three)
    CheckBox three;
    @BindView(R.id.four)
    CheckBox four;
    @BindView(R.id.five)
    CheckBox five;
    @BindView(R.id.six)
    CheckBox six;
    @BindView(R.id.senven)
    CheckBox senven;
    @BindView(R.id.eight)
    CheckBox eight;
    @BindView(R.id.nine)
    CheckBox nine;

    @BindView(R.id.lan_1)
    CheckBox lan1;
    @BindView(R.id.lan_2)
    CheckBox lan2;
    @BindView(R.id.lan_3)
    CheckBox lan3;
    @BindView(R.id.lan_4)
    CheckBox lan4;

    @BindView(R.id.attribute_ll)
    LinearLayout attributeLl;


    private int mYear;
    private int mMonth;
    private int mDay;
    private CheckBox[] lane;
    private CheckBox[] autofire;
    private String dangerlevels;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type1, container, false);
        ButterKnife.bind(this, view);
        lane = new CheckBox[]{lan1, lan2, lan3, lan4};
        autofire = new CheckBox[]{one, two, three, four, five, six, senven, eight, nine};
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dialog = new ProgressDialog(UiUtils.getContext());
        dialog.setMessage("添加中");
        progressDialog.setMessage("获取中");
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        attributeLl.setOnClickListener(attrlistener);
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

        foundtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth, mDay);
                pickerDialog.show();
            }
        });
        dangerlevels = "";
        dangerlevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.dangerlevel1) {
                    attributeLl.setVisibility(View.VISIBLE);
                    dangerlevels = "1";
                } else if (checkedId == R.id.dangerlevel2) {
                    attributeLl.setVisibility(View.VISIBLE);
                    dangerlevels = "2";
                } else if (checkedId == R.id.dangerlevel3) {
                    attributeLl.setVisibility(View.VISIBLE);
                    dangerlevels = "3";
                } else if (checkedId == R.id.dangerlevel4) {
                    dangerlevels = "4";
                    attributeLl.setVisibility(View.GONE);
                }
            }
        });

    }

    String mainAttrId = "";
    String subAttrId = "";
    BottomDialog bottomDialog;
    private View.OnClickListener attrlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bottomDialog = null;
            if (dangerlevels.equals("1")||dangerlevels.equals("2")){
                AttrSelector selector = new AttrSelector(getActivity());
                DefaultProvider provider = new DefaultProvider(UiUtils.getContext());
                selector.setProvider(provider);
                bottomDialog = new BottomDialog(getActivity(),selector);
            }else if (dangerlevels.equals("3")){
                AttrSelector selector = new AttrSelector(getActivity());
                GeneralProvider provider = new GeneralProvider(UiUtils.getContext());
                selector.setProvider(provider);
                bottomDialog = new BottomDialog(getActivity(),selector);
            }else if (dangerlevels.equals("4")){
                //隐藏单位属性
                attributeLl.setVisibility(View.GONE);
            }else{
                UiUtils.showToast("请先选择火灾危险等级");
                return;
            }
            bottomDialog.setSelectorListener(new onAttrSelectedListener() {
                @Override
                public void onSelectSuccess(MainAttr mainAttr, ArrayList<SubAttr> subData, int type) {
                    if (type == 1) {
                        attribute.setText(mainAttr.name);
                        String mainid1 = mainAttr.id + "";
                        mainAttrId = mainid1;
                    } else {
                        String mainid = mainAttr.id + "";
                        String subid = "";
                        String subidname = "";
                        for (SubAttr attr :
                                subData) {
                            subid = subid + "#" + attr.id;
                            subidname = subidname + "#" + attr.name;
                        }
                        mainAttrId = mainid;
                        subAttrId = subid;
                        attribute.setText(mainAttr.name);
                        System.out.println(mainAttr.name + "fushixing->" + subidname);
                    }
                    bottomDialog.dismiss();
                }

                @Override
                public void onSelectFailed(String msg) {
                    if (msg.equals("取消")) {
                        bottomDialog.dismiss();
                    } else
                        UiUtils.showToast(UiUtils.getContext(), msg);
                }
            });
            bottomDialog.show();
        }
    };
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    foundtime.setText(
                            new StringBuilder()
                                    .append(mYear).append("-")
                                    .append(pad(mMonth + 1)).append("-")
                                    .append(pad(mDay))
                    );
                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    Map<String, String> map;
    ProgressDialog dialog;

    @OnClick(R.id.commit)
    public void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("确定添加单位吗")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UiUtils.showToast("正在添加请稍后..。");
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
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        map = null;

        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();
        if (name1.equals("") || name1 == null) {
            UiUtils.showToast(UiUtils.getContext(), "公司名称不不能为空!");
            return;
        }
        if (addr1 == null || addr1.equals("")) {
            UiUtils.showToast(UiUtils.getContext(), "公司地址不不能为空!");
            return;
        }

        String incharge1 = incharge.getText().toString();

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


        String facility1 = "";
        for (int i = 0; i < autofire.length; i++) {
            if (autofire[i].isChecked()) {
                facility1 += App.getApplication().getResources().getStringArray(R.array.autofire)[i] + "#";
            }
        }
        facility1 = facility1.replace(" ", "");
        if (!TextUtils.equals("", facility1))
            builder.put("facility", facility1);//备注单位属性未添加


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
        String lanpos1 = "";
        for (int i = 0; i < lane.length; i++) {
            if (lane[i].isChecked()) {
                lanpos1 += App.getApplication().getResources().getStringArray(R.array.lane)[i] + "#";
            }
        }
        if (Utils.isVaild(lanpos1)) {
            builder.put("lanepos", lanpos1);//消防车道类型为添加
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
        String lng1 = lng.getText().toString();
        String lat1 = lat.getText().toString();
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


        if (Utils.isVaild(mainAttrId)) {
            builder.put("mainattribute", mainAttrId);
        }

        if (Utils.isVaild(subAttrId)) {
            builder.put("subattribute", subAttrId);
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
                AddCompanyResult result = response.body();
                try {
                    if (result != null) {
                        if (result.status.equals("1")) {
                            final AlertDialog dialog1;

                            dialog1 = new AlertDialog.Builder(getActivity())
                                    .setMessage(result.result)
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
                                    dialog1.show();
                                }
                            }, 500);
                        } else {

                            UiUtils.showToast(UiUtils.getContext(), result.msg);
                        }
                    } else {
                        UiUtils.showToast(UiUtils.getContext(), "添加失败");
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

    Handler handler = new Handler();

}