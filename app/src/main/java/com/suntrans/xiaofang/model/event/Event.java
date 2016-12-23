package com.suntrans.xiaofang.model.event;

/**
 * Created by Looney on 2016/12/22.
 */

public class Event {

    public String id;
    public String user_id;
    public String user_path;
    public String user_done;
    public String company_id;
    public String company_special;
    public String company_name;
    public String company_address;
    public String company_leader;
    public String company_phone;
    public String contents;
    public String location;
    public String lng;
    public String lat;
    public String status;
    public String is_done;
    public String type_ids;

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_path='" + user_path + '\'' +
                ", user_done='" + user_done + '\'' +
                ", company_id='" + company_id + '\'' +
                ", company_special='" + company_special + '\'' +
                ", company_name='" + company_name + '\'' +
                ", company_address='" + company_address + '\'' +
                ", company_leader='" + company_leader + '\'' +
                ", company_phone='" + company_phone + '\'' +
                ", contents='" + contents + '\'' +
                ", location='" + location + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", status='" + status + '\'' +
                ", is_done='" + is_done + '\'' +
                ", type_ids='" + type_ids + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    public String created_at;
    public String updated_at;
}
/*
 "id":185,
            "user_id":"赵思琪",
            "user_path":"0_1_133_134_140",
            "user_done":null,
            "company_id":"107",
            "company_special":"0",
            "company_name":"北湖小学",
            "company_address":"北湖路10号",
            "company_leader":"范永岁",
            "company_phone":"85748280",
            "contents":"测试",
            "location":"114.2737,30.60534",
            "lng":"114.273700",
            "lat":"30.605340",
            "status":"13",
            "is_done":"0",
            "type_ids":"1,4",
            "created_at":"2016-12-20 09:52:23",
            "updated_at":"2016-12-20 09:58:39",
            "deleted_at":null
 */