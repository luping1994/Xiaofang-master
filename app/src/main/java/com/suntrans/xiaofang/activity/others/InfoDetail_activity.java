package com.suntrans.xiaofang.activity.others;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.fragment.infodetail.Type1__info_fragment;
import com.suntrans.xiaofang.fragment.infodetail.Type1__info_yiban_fragment;
import com.suntrans.xiaofang.fragment.infodetail.Type2__info_fragment;
import com.suntrans.xiaofang.fragment.infodetail.Type3__info_fragment;
import com.suntrans.xiaofang.fragment.infodetail.Type4__info_fragment;
import com.suntrans.xiaofang.fragment.infodetail.Type5__info_fragment;
import com.suntrans.xiaofang.fragment.infodetail.Type6__info_fragment;
import com.suntrans.xiaofang.fragment.infodetail.Type7__info_fragment;
import com.suntrans.xiaofang.model.company.UnitInfo;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.MarkerHelper;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Looney on 2016/12/1.
 */

public class InfoDetail_activity extends BasedActivity {




    protected LinearLayoutManager manager;

    private ArrayList<Map<String,UnitInfo>> datas = new ArrayList<>();


    public String companyId;//单位的id,通过id查找详细信息
    public String companyType;//单位

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companyinfo);
        initData();
        initView();
    }

    private void initData() {

    }


    private void initView() {
        companyId = getIntent().getStringExtra("companyID").split("#")[0];
        companyType = getIntent().getStringExtra("companyID").split("#")[1];
        LogUtil.i("InfodetailActivity:"+"id="+companyId);
        switch (Integer.valueOf(companyType)){
            case 0:
                Type1__info_fragment type1__info_fragment = new Type1__info_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type1__info_fragment).commit();
                break;
            case 1:
                Type2__info_fragment type2__info_fragment = new Type2__info_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type2__info_fragment).commit();
                break;
            case MarkerHelper.FIRESTATION:
                Type3__info_fragment type3__info_fragment = Type3__info_fragment.newInstance(MarkerHelper.FIRESTATION);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type3__info_fragment).commit();
                break;
            case 3:
                Type4__info_fragment type4__info_fragment = new Type4__info_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type4__info_fragment).commit();
                break;
            case 4:
                Type5__info_fragment type5__info_fragment = new Type5__info_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type5__info_fragment).commit();
                break;
            case MarkerHelper.FIREBRIGADE:
                Type6__info_fragment type6__info_fragment = new Type6__info_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type6__info_fragment).commit();
                break;
            case MarkerHelper.FIREADMINSTATION:
                Type3__info_fragment type3__info_fragment_admin =Type3__info_fragment.newInstance(MarkerHelper.FIREADMINSTATION);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, type3__info_fragment_admin).commit();
                break;
            case MarkerHelper.COMMONCOMPANY:
                Type1__info_yiban_fragment fragment = new Type1__info_yiban_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, fragment).commit();
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }






}
