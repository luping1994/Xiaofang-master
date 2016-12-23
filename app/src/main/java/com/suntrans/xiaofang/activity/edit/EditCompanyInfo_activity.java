package com.suntrans.xiaofang.activity.edit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.common.collect.ImmutableMap;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.model.company.AddCompanyResult;
import com.suntrans.xiaofang.model.company.CompanyDetailnfo;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.StatusBarCompat;
import com.suntrans.xiaofang.utils.UiUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Looney on 2016/12/3.
 */

public class EditCompanyInfo_activity extends AppCompatActivity {
    @BindView(R.id.addr)
    EditText addr;
    @BindView(R.id.incharge)
    EditText incharge;
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
    @BindView(R.id.have)
    RadioButton have;
    @BindView(R.id.no)
    RadioButton no;
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
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.marker)
    EditText marker;
    private Toolbar toolbar;
    private EditText txName;
    private CompanyDetailnfo info;

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
//        txName = (EditText) findViewById(R.id.name);
//        txName.setText(getIntent().getStringExtra("title"));
        info = (CompanyDetailnfo) getIntent().getSerializableExtra("info");
    }


    private void initData() {
        name.setText(info.name);
        addr.setText(info.addr);
        incharge.setText(info.incharge);
        if (info.dangerlevel != null) {
            if (info.dangerlevel.equals("1"))
                dangerlevel1.setChecked(true);
            else
                dangerlevel2.setChecked(true);
        } else {
            dangerlevel1.setChecked(false);
            dangerlevel2.setChecked(false);
        }
        buildarea.setText(info.buildarea);
        exitnum.setText(info.exitnum);
        stairnum.setText(info.stairnum);
        if (hasfacility.equals(1)) {
            have.setChecked(true);
        } else {
            no.setChecked(true);
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
        east.setText("东:" + info.east);
        south.setText("南" + info.south);
        west.setText("西" + info.west);
        north.setText("北" + info.north);
        marker.setText(info.remark);

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
        }
        return super.onOptionsItemSelected(item);
    }


    Map<String, String> map1;
    ProgressDialog dialog;

    public void commit(View view) {

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
        dialog.show();
        map1 = null;
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
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


        //添加修改信息需要id
        try {
            builder.put("id",info.id);
        }catch (Exception e){
            UiUtils.showToast(this,"单位不存在");
        }
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


        map1 = builder.build();
        for (Map.Entry<String, String> entry : map1.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            System.out.println(key + "," + value);
        }
        RetrofitHelper.getApi().updateCompany(map1).enqueue(new Callback<AddCompanyResult>() {
            @Override
            public void onResponse(Call<AddCompanyResult> call, Response<AddCompanyResult> response) {
                dialog.dismiss();
                System.out.println("message" + response.message());
                System.out.println("body" + response.body());
                System.out.println("raw" + response.raw());
                AddCompanyResult result = response.body();
                try {
                    if (result == null) {
                        UiUtils.showToast(UiUtils.getContext(), "修改单位信息失败!");
                    } else {
                        UiUtils.showToast(UiUtils.getContext(), "提示:" + result.result);
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<AddCompanyResult> call, Throwable t) {
                dialog.dismiss();
                UiUtils.showToast(UiUtils.getContext(), "修改单位信息失败!");
                LogUtil.e(t.toString());
            }
        });
    }
}
