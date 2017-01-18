package com.suntrans.xiaofang.model.supervise;

/**
 * Created by Looney on 2016/12/23.
 */

public class Supervise {

    public String id;
    public String user_id;
    public String user_path;
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
    public String remark;
    public String status;
    public String is_done;
    public String type_ids;
    public String img_before;
    public String img_after;
    public String video_path;
    public String created_at;
    public String updated_at;
    public String deleted_at;



    @Override
    public String toString() {
        return "Supervise{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_path='" + user_path + '\'' +
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
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                ", is_done='" + is_done + '\'' +
                ", type_ids='" + type_ids + '\'' +
                ", img_before='" + img_before + '\'' +
                ", img_after='" + img_after + '\'' +
                ", video_path='" + video_path + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
