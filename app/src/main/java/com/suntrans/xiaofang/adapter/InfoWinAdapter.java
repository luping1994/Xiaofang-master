package com.suntrans.xiaofang.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.suntrans.xiaofang.R;


/**
 * Created by Looney on 2017/6/8.
 */

public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {
    private Context mContext;


    public InfoWinAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        initData(marker);
        View view = initView();
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void initData(Marker marker) {

    }

    @NonNull
    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.marker_info, null);
//        call.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {

    }
}
