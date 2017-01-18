package com.suntrans.xiaofang.utils;

import android.util.SparseArray;

import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Looney on 2017/1/18.
 */

public class MarkerHelper {
    private SparseArray<ArrayList<Marker>> markerArray = new SparseArray<>();
    private SparseArray<ArrayList<MarkerOptions>> optionsArray = new SparseArray<>();
    private static MarkerHelper helper;
    private MarkerHelper(){
    }

    public static MarkerHelper getInstance(){
        if (helper==null)
            helper= new MarkerHelper();
        return helper;
    }

}
