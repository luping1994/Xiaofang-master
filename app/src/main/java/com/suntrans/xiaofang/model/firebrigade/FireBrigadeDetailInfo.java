package com.suntrans.xiaofang.model.firebrigade;

import java.io.Serializable;

/**
 * Created by Looney on 2016/12/21.
 */

public class FireBrigadeDetailInfo implements Serializable{
    public String name;
    public String id;
    public String addr;
    public String lng;
    public String lat;

    public String area;
    public String phone;
    public String membernum;
    public String servingnum;
    public String policenum;
    public String fulltimenum;

    @Override
    public String toString() {
        return "FireBrigadeDetailInfo{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", addr='" + addr + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", area='" + area + '\'' +
                ", phone='" + phone + '\'' +
                ", membernum='" + membernum + '\'' +
                ", servingnum='" + servingnum + '\'' +
                ", policenum='" + policenum + '\'' +
                ", fulltimenum='" + fulltimenum + '\'' +
                ", clerknum='" + clerknum + '\'' +
                '}';
    }

    public String clerknum;




//    public String carnum;
//    public String cardisp;
//    public String waterweight;
//    public String soapweight;
//    public String district;
//    public String street;
//    public String community;
//    public String group;


}
