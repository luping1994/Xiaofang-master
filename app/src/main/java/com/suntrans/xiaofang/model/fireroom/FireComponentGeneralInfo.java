package com.suntrans.xiaofang.model.fireroom;

/**
 * Created by Looney on 2016/12/20.
 */

public class FireComponentGeneralInfo {
    public String id;
    public String name;
    public String lng;
    public String lat;
    public String addr;

    @Override
    public String toString() {
        return "FireComponentGeneralInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }

    public String distance;
}
