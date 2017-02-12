package com.suntrans.xiaofang.activity.edit;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.journeyapps.barcodescanner.Util;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.model.company.AddCompanyResult;
import com.suntrans.xiaofang.model.company.CompanyDetailnfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.DbHelper;
import com.suntrans.xiaofang.utils.StatusBarCompat;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.suntrans.xiaofang.views.dialog.AttrSelector;
import com.suntrans.xiaofang.views.dialog.BottomDialog;
import com.suntrans.xiaofang.views.dialog.DefaultProvider;
import com.suntrans.xiaofang.views.dialog.GeneralProvider;
import com.suntrans.xiaofang.views.dialog.MainAttr;
import com.suntrans.xiaofang.views.dialog.SubAttr;
import com.suntrans.xiaofang.views.dialog.onAttrSelectedListener;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2016/12/3.
 */

public class EditCompanyInfo_activity extends BasedActivity {
    @BindView(R.id.addr)
    EditText addr;

    @BindView(R.id.fire_dan)
    TextView fireDan;
    @BindView(R.id.dangerlevel1)
    RadioButton dangerlevel1;
    @BindView(R.id.dangerlevel2)
    RadioButton dangerlevel2;
    @BindView(R.id.dangerlevel)
    RadioGroup dangerlevel;
    @BindView(R.id.buildarea)
    EditText buildarea;
    @BindView(R.id.exitnum)
    EditText exitnum;
    @BindView(R.id.stairnum)
    EditText stairnum;

    //    @BindView(R.id.hasfacility)
//    RadioGroup hasfacility;

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
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.marker)
    EditText marker;
    @BindView(R.id.lng)
    EditText lng;
    @BindView(R.id.lat)
    EditText lat;
    @BindView(R.id.dangerlevel3)
    RadioButton dangerlevel3;
    @BindView(R.id.dangerlevel4)
    RadioButton dangerlevel4;
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
    @BindView(R.id.facility)
    LinearLayout facility;
    @BindView(R.id.lan_1)
    CheckBox lan1;
    @BindView(R.id.lan_2)
    CheckBox lan2;
    @BindView(R.id.lan_3)
    CheckBox lan3;
    @BindView(R.id.lan_4)
    CheckBox lan4;
    @BindView(R.id.attribute)
    TextView attribute;
    @BindView(R.id.attribute_ll)
    LinearLayout attributeLl;


    @BindView(R.id.incharge_dadui)
    Spinner inchargeDadui;

    @BindView(R.id.incharge_paichusuo)
    Spinner inchargePaichusuo;


    @BindView(R.id.companyid)
    EditText companyid;
    @BindView(R.id.otherdisp)
    EditText otherdisp;
    @BindView(R.id.firemannum)
    EditText firemannum;
    @BindView(R.id.nearby)
    EditText nearby;

    private Toolbar toolbar;
    private EditText txName;
    private CompanyDetailnfo info;
    private int mYear;
    private int mMonth;
    private int mDay;

    private CheckBox[] autofire;
    private String[] hasFire;

    private CheckBox[] laneCheckBox;
    private String[] laneArray;
    private String level;
    String dangerlevels = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editcompanyinfo);
        ButterKnife.bind(this);
        setupToolBar();
        initView();
        initData();
    }

    private void initView() {

        laneCheckBox = new CheckBox[]{lan1, lan2, lan3, lan4};
        autofire = new CheckBox[]{one, two, three, four, five, six, senven, eight, nine};
        hasFire = App.getApplication().getResources().getStringArray(R.array.autofire);
        laneArray = App.getApplication().getResources().getStringArray(R.array.lane);

        info = (CompanyDetailnfo) getIntent().getSerializableExtra("info");
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        foundtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(EditCompanyInfo_activity.this, mDateSetListener, mYear, mMonth, mDay);
                pickerDialog.show();
            }
        });
        attributeLl.setOnClickListener(attrlistener);
        dangerlevels = "";
        dangerlevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.dangerlevel1) {
                    dangerlevels = "1";
                    attributeLl.setVisibility(View.VISIBLE);
                    inchargeDadui.setVisibility(View.VISIBLE);
                    inchargePaichusuo.setVisibility(View.GONE);

                } else if (checkedId == R.id.dangerlevel2) {
                    dangerlevels = "2";
                    attributeLl.setVisibility(View.VISIBLE);
                    inchargeDadui.setVisibility(View.VISIBLE);
                    inchargePaichusuo.setVisibility(View.GONE);

                } else if (checkedId == R.id.dangerlevel3) {
                    dangerlevels = "3";
                    attributeLl.setVisibility(View.VISIBLE);
                    inchargeDadui.setVisibility(View.VISIBLE);
                    inchargePaichusuo.setVisibility(View.VISIBLE);

                } else if (checkedId == R.id.dangerlevel4) {
                    attributeLl.setVisibility(View.GONE);
                    inchargeDadui.setVisibility(View.VISIBLE);
                    inchargePaichusuo.setVisibility(View.VISIBLE);
                    dangerlevels = "4";
                    mainAttrId = "";
                    subAttrId = "";
                }
            }
        });
    }

    String mainAttrId = "";
    String subAttrId = "";
    private BottomDialog bottomDialog;

    private View.OnClickListener attrlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bottomDialog = null;
            if (dangerlevels.equals("1") || dangerlevels.equals("2")) {
                AttrSelector selector = new AttrSelector(EditCompanyInfo_activity.this);
                DefaultProvider provider = new DefaultProvider(EditCompanyInfo_activity.this);
                selector.setProvider(provider);
                bottomDialog = new BottomDialog(EditCompanyInfo_activity.this, selector);
            } else if (dangerlevels.equals("3")) {
                AttrSelector selector = new AttrSelector(EditCompanyInfo_activity.this);
                GeneralProvider provider = new GeneralProvider(EditCompanyInfo_activity.this);
                selector.setProvider(provider);
                bottomDialog = new BottomDialog(EditCompanyInfo_activity.this, selector);
            } else if (dangerlevels.equals("4")) {
                //隐藏单位属性
                attributeLl.setVisibility(View.GONE);
            } else {
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
                        attribute.setText(mainAttr.name);
                        String mainid = mainAttr.id + "";
                        String subid = "";
                        for (SubAttr attr :
                                subData) {
                            subid = subid + "#" + attr.id;
                        }
                        mainAttrId = mainid;
                        subAttrId = subid;
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

    public void getLocation(View view) {
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
//        if (myLocation==null){
//            Snackbar.make(scroll,"获取当前地址失败",Snackbar.LENGTH_SHORT).show();
//            return;
//        }
//        lat.setText(myLocation.latitude + "");
//        lng.setText(myLocation.longitude + "");
//        addr.setText(myaddr);
    }

    @Override
    protected void onDestroy() {
//        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void initData() {

        name.setText(info.name);
        addr.setText(info.addr);

        lat.setText(info.lat);
        lng.setText(info.lng);

        if (info.dangerlevel != null) {
            level = info.dangerlevel;
            if (level.equals("1"))
                dangerlevel1.setChecked(true);
            else if (level.equals("2"))
                dangerlevel2.setChecked(true);
            else if (level.equals("3")) {
                dangerlevel3.setChecked(true);
            } else if (level.equals("4")) {
                dangerlevel4.setChecked(true);
            }
        } else {
            dangerlevel1.setChecked(false);
            dangerlevel2.setChecked(false);
            dangerlevel3.setChecked(false);
            dangerlevel4.setChecked(false);
        }
        buildarea.setText(info.buildarea);
        exitnum.setText(info.exitnum);
        stairnum.setText(info.stairnum);

        if (info.facility != null) {
            String[] fire = info.facility.split("#");
            for (int i = 0; i < fire.length; i++) {
                for (int j = 0; j < hasFire.length; j++) {
                    if (fire[i].equals(hasFire[j]))
                        autofire[j].setChecked(true);
                }
            }
        }


        if (info.lanepos != null) {
            String[] lanpos1 = info.lanepos.split("#");
            for (int i = 0; i < lanpos1.length; i++) {
                for (int j = 0; j < laneArray.length; j++) {
                    if (lanpos1[i].equals(laneArray[j]))
                        laneCheckBox[j].setChecked(true);
                }
            }
        }

        artiname.setText(info.artiname);
        artiid.setText(info.artiid);
        artiphone.setText(info.artiphone);

        managername.setText(info.managername);
        managerid.setText(info.managerid);
        managerphone.setText(info.managerphone);

        responname.setText(info.responname);
        responid.setText(info.responid);
        responphone.setText(info.responphone);

        orgid.setText(info.orgid);
        leaderdepart.setText(info.leaderdepart);
        foundtime.setText(info.foundtime);
        phone.setText(info.phone);

        straffnum.setText(info.staffnum);
        area.setText(info.area);
        lanenum.setText(info.lanenum);
        elevatornum.setText(info.elevatornum);
        refugenum.setText(info.refugenum);
        refugepos.setText(info.refugepos);
        east.setText(info.east);
        south.setText(info.south);
        west.setText(info.west);
        north.setText(info.north);
        marker.setText(info.remark);

        String mainId = info.mainattribute;
        if (mainId != null) {
            DbHelper helper = new DbHelper(this, "Fire", null, 1);
            SQLiteDatabase db = helper.getReadableDatabase();
            db.beginTransaction();
            Cursor cursor = db.rawQuery("select Name from attr_main where Id=?", new String[]{mainId});
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    attribute.setText(cursor.getString(0));
                }
            }
            cursor.close();
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    private void setupToolBar() {
        StatusBarCompat.compat(this, Color.rgb(0x2f, 0x9d, 0xce));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("修改单位信息");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在修改请稍候");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.tijiao:
                commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    Map<String, String> map1;
    ProgressDialog dialog;
    String incharge1 = "";

    private void commit() {

        AlertDialog dialog = new AlertDialog.Builder(EditCompanyInfo_activity.this)
                .setMessage("确认提交修改?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commitData();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    private void commitData() {
        map1 = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();


        String name1 = name.getText().toString();
        String addr1 = addr.getText().toString();

        String buildarea1 = buildarea.getText().toString();
        String exitnum1 = exitnum.getText().toString();
        String stairnum1 = stairnum.getText().toString();

        String artiname1 = artiname.getText().toString();
//        String artiid1 = artiid.getText().toString();
        String artiphone1 = artiphone.getText().toString();
        String managername1 = managername.getText().toString();
//        String managerid1 = managerid.getText().toString();
        String managerphone1 = managerphone.getText().toString();
        String responname1 = responname.getText().toString();
//        String responid1 = responid.getText().toString();
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
//
        String remark1 = marker.getText().toString();


        String nearby1 = nearby.getText().toString();
        String firemannum1 = firemannum.getText().toString();//消防队员人数
        String otherdisp1 = otherdisp.getText().toString();//单位其它情况
        String companyid1 = companyid.getText().toString();//公司编码


        String facility1 = "";
        for (int i = 0; i < autofire.length; i++) {
            if (autofire[i].isChecked()) {
                facility1 += App.getApplication().getResources().getStringArray(R.array.autofire)[i] + "#";
            }
        }

        facility1 = facility1.replace(" ", "");

        //添加修改信息需要id
        try {
            builder.put("id", info.id);
        } catch (Exception e) {
            UiUtils.showToast("单位不存在");
        }

        if (addr1 == null || addr1.equals("")) {
            UiUtils.showToast("公司地址不不能为空!");
            return;
        }

        if (!Utils.isVaild(name1)) {
            UiUtils.showToast("单位名称不能为空");
            return;
        }
//        if (!Utils.isVaild(mainAttrId)) {
//            UiUtils.showToast("单位属性不能为空");
//            return;
//        }

        if (!Utils.isVaild(buildarea1)) {
            UiUtils.showToast("请输入建筑面积");
            return;
        }

        if (!Utils.isVaild(exitnum1)) {
            UiUtils.showToast("请输入安全出口数");
            return;
        }

        if (!Utils.isVaild(stairnum1)) {
            UiUtils.showToast("请消防楼梯数");
            return;
        }


        if (!Utils.isVaild(dangerlevels)) {
            UiUtils.showToast("请选择火灾危险等级!");
            return;
        }

        if (!Utils.isVaild(facility1)) {
            UiUtils.showToast("请选择自动消防设施");
            return;
        }

        if (!Utils.isVaild(artiname1)) {
            UiUtils.showToast("请输入法定人姓名");
            return;
        }

        if (!Utils.isVaild(artiphone1)) {
            UiUtils.showToast("请输入法定人电话");
            return;
        }
        dialog.show();

        builder.put("facility", facility1);
        builder.put("name", name1);
        builder.put("addr", addr1);
        builder.put("dangerlevel", dangerlevels);
        builder.put("buildarea", buildarea1);
        builder.put("exitnum", exitnum1);
        builder.put("stairnum", stairnum1);
        builder.put("artiname", artiname1);

//        builder.put("cmystate", "0");

        if (incharge1 != null) {
            incharge1 = incharge1.replace(" ", "");
            if (!TextUtils.equals("", incharge1))
                builder.put("incharge", incharge1);
        }




//        if (artiid1 != null) {
//            artiid1 = artiid1.replace(" ", "");
//            if (!TextUtils.equals("", artiid1))
//                builder.put("artiid", artiid1);
//        }
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

//        if (managerid1 != null) {
//            managerid1 = managerid1.replace(" ", "");
//            if (!TextUtils.equals("", managerid1))
//                builder.put("managerid", managerid1);
//        }

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

//        if (responid1 != null) {
//            responid1 = responid1.replace(" ", "");
//            if (!TextUtils.equals("", responid1))
//                builder.put("responid", responid1);
//        }

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
        for (int i = 0; i < laneCheckBox.length; i++) {
            if (laneCheckBox[i].isChecked()) {
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


        if (remark1 != null) {
            remark1 = remark1.replace(" ", "");
            if (!TextUtils.equals("", remark1))
                builder.put("remark", remark1);
        }

        if (Utils.isVaild(info.cmystate)) {
            builder.put("cmystate", info.cmystate);
        }

        if (Utils.isVaild(mainAttrId)) {
            builder.put("mainattribute", mainAttrId);
        }

        if (Utils.isVaild(subAttrId)) {
            builder.put("subattribute", subAttrId);
        }


        if (Utils.isVaild(nearby1)) {
            builder.put("nearby", nearby1);
        }

        if (Utils.isVaild(firemannum1)) {
            builder.put("firemannum", firemannum1);
        }

        if (Utils.isVaild(otherdisp1)) {
            builder.put("otherdisp", otherdisp1);
        }
        if (Utils.isVaild(companyid1)) {
            builder.put("companyid", companyid1);
        }

        map1 = builder.build();
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
        RetrofitHelper.getApi().updateCompany(map1)
                .compose(this.<AddCompanyResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AddCompanyResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        UiUtils.showToast(UiUtils.getContext(), "修改单位信息失败!");

                    }

                    @Override
                    public void onNext(AddCompanyResult result) {
                        dialog.dismiss();
                        try {
                            if (result == null) {
                                UiUtils.showToast(UiUtils.getContext(), "修改单位信息失败!");
                            } else {
                                UiUtils.showToast(UiUtils.getContext(), "提示:" + result.result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

//                UiUtils.showToast(UiUtils.getContext(), "修改单位信息失败!");
//                LogUtil.e(t.toString());

    }


//    public LatLng myLocation;//我当前的位置
//    public String myaddr;//我的位置描述
//    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            myLocation = intent.getParcelableExtra("myLocation");
//            myaddr = intent.getStringExtra("addrdes");
//        }
//    };
}
