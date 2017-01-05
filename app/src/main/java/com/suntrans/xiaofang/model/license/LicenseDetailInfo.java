package com.suntrans.xiaofang.model.license;

import java.io.Serializable;

/**
 * Created by Looney on 2016/12/21.
 */

public class LicenseDetailInfo implements Serializable{
    public String id;
    public String user_id;
    public String building_id;

    public String completetime;
    public String cisqualified;
    public String cnumber;


    public String opentime;
    public String oisqualified;
    public String onumber;

    public String status;
    public String updated_at;
    public String created_at;
    public String deleted_at;
    public Building building;

    public String lat;
    public String lng;



/*
     "id":20,
        "user_id":"1",
        "building_id":"16",
        "completetime":null,
        "cisqualified":null,
        "cnumber":null,
        "opentime":null,
        "oisqualified":null,
        "onumber":null,
        "status":"1",
        "updated_at":"2016-12-20 03:42:10",
        "created_at":"2016-12-20 03:42:10",
        "deleted_at":null,
        "building":{
            "id":16,
            "user_id":null,
            "name":"柳帅7",
            "addraddr":"珞珈山巅",
            "lng":"114.359747",
            "lat":"30.542620",
            "leader":"柳帅",
            "phone":"123124123",
            "updated_at":"2016-12-20 03:42:10",
            "created_at":"2016-12-20 03:42:10",
            "deleted_at":null

     */
}
