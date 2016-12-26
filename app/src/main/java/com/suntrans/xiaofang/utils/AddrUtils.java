package com.suntrans.xiaofang.utils;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.suntrans.xiaofang.App;

/**
 * Created by Looney on 2016/12/22.
 */

public class AddrUtils {
    private static String addr;
    public static String getAddr(LatLonPoint point){

        GeocodeSearch geocodeSearch = new GeocodeSearch(App.getApplication());
        RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                if (rCode == 1000) {
                    if (result != null && result.getRegeocodeAddress() != null
                            && result.getRegeocodeAddress().getFormatAddress() != null) {
                        addr= result.getRegeocodeAddress().getFormatAddress();
                    } else {
                        addr= "0";
                    }
                } else {
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            }
        });

        return addr;
    }
}
