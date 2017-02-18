package com.suntrans.xiaofang.model.license;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Looney on 2016/12/21.
 */

public class LicenseDetailInfo implements Serializable{


    public String id;
    public String user_id;
    public String cmyname;
    public String name;
    public String addr;
    public String lng;
    public String lat;
    public String contact;
    public String phone;

    public List<LicenseItemInfo> detail;


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
