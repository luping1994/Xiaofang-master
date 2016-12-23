package com.suntrans.xiaofang.model.license;

import java.io.Serializable;

/**
 * Created by Looney on 2016/12/21.
 */

public class LicenseDetailInfo implements Serializable{
    public String name;
    public String id;
    public String addr;
    public String lng;
    public String lat;
    public String contact;
    public String phone;
    public String membernum;
    public String cardisp;
    public String equipdisp;
    public String district;
    public String group;
    public String created_at;
    public String updated_at;
    public String deleted_at;

    @Override
    public String toString() {
        return "FireRoomDetailInfo{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", addr='" + addr + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", membernum='" + membernum + '\'' +
                ", cardisp='" + cardisp + '\'' +
                ", equipdisp='" + equipdisp + '\'' +
                ", district='" + district + '\'' +
                ", group='" + group + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                '}';
    }

/*
     "id": 1,
        "name": "花水街东民社区",
        "addr": "花楼水塔街东民社区",
        "lng": null,
        "lat": null,
        "contact": "",
        "phone": "",
        "membernum": "10",
        "cardisp": "消防摩托车ATLM250-2  1辆 ",
        "equipdisp": "灭火器 MF/ABC2 20具   消防斧 大斧 1把    强光手电筒YD-900 3只   消火栓扳手 地上、地下 1个            消防水带13-65-20 10盘   消防水枪QZ3.5/7.5 2支         6米两节拉梯 承征 1把          战斗服（含头盔及战斗靴）4套                      救生绳Zj8x15m  4根       ",
        "district": "江汉",
        "group": "江汉",
        "created_at": null,
        "updated_at": null,
        "deleted_at": null
     */
}
