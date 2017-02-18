package com.suntrans.xiaofang.model.firegroup;

import java.io.Serializable;

/**
 * Created by Looney on 2016/12/21.
 */

public class FireGroupDetailInfo implements Serializable{
    public String name;
    public String id;
    public String addr;
    public String lng;
    public String lat;

    public String area;
    public String phone;
    public String membernum;
    public String carnum;
    public String cardisp;
    public String waterweight;
    public String soapweight;
    public String district;
    public String street;
    public String community;
    public String group;
    public String brigade_path;
    public String brigade_name;

    @Override
    public String toString() {
        return "FireGroupDetailInfo{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", addr='" + addr + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", area='" + area + '\'' +
                ", phone='" + phone + '\'' +
                ", membernum='" + membernum + '\'' +
                ", carnum='" + carnum + '\'' +
                ", cardisp='" + cardisp + '\'' +
                ", waterweight='" + waterweight + '\'' +
                ", soapweight='" + soapweight + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                ", community='" + community + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
