package com.suntrans.xiaofang.model.firestation;

import java.io.Serializable;

/**
 * Created by Looney on 2016/12/21.
 */

public class FireStationDetailInfo implements Serializable{
    public String name;
    public String id;
    public String addr;
    public String lng;
    public String lat;

    public String area;
    public String phone;
    public String servingnum;
    public String fulltimenum;
    public String carnum;
    public String cardisp;
    public String waterweight;
    public String soapweight;
    public String district;
    public String street;
    public String community;
    public String group;
    public String cmystate;

    @Override
    public String toString() {
        return "FireStationDetailInfo{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", addr='" + addr + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", area='" + area + '\'' +
                ", phone='" + phone + '\'' +
                ", servingnum='" + servingnum + '\'' +
                ", fulltimenum='" + fulltimenum + '\'' +
                ", carnum='" + carnum + '\'' +
                ", cardisp='" + cardisp + '\'' +
                ", waterweight='" + waterweight + '\'' +
                ", soapweight='" + soapweight + '\'' +
                ", district='" + district + '\'' +
                ", street='" + street + '\'' +
                ", communuty='" + community + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
