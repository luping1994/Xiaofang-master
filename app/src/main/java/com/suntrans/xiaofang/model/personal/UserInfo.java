package com.suntrans.xiaofang.model.personal;

/**
 * Created by Looney on 2016/12/16.
 */

public class UserInfo {
    /*
     "id": 1,
    "username": "shengwei",
    "truename": "盛巍",
    "mobile": "13825821001",
    "sex": 0,
    "area_id": 3,
    "area1": "江汉",
    "area2": "唐家墩街",
    "area3": "唐家墩派出所",
    "area4": "",
    "role": "jdy",
    "active": 1,
    "uuid": null,
    "avatar": null,
    "login_num": 0,
    "last_ip": null,
    "setting_path": null,
    "setting_msg": 1,
    "setting_distance": 3,
    "views": 0,
    "likes": 0,
    "likeme": 0,
    "nolinks": 0,
    "signs": 0,
    "cards": 0,
    "imgs": 0,
    "isauth": null,
    "level": 0,
    "remark": null,
    "last_at": null,
    "created_at": null,
    "updated_at": "2016-12-07 10:30:46",
    "deleted_at": null
     */

    public String id;
    public String username;
    public String truename;
    public String mobile;
    public String sex;
    public String area_id;
    public String area1;
    public String area2;
    public String area3;
    public String area4;
    public String role;
//    public String active;
//    public String uuid;
//    public String avatar;
//    public String login_num;
//    public String last_ip;
//    public String setting_path;
//    public String setting_msg;
//    public String setting_distance;
//    public String views;
//    public String likes;
//    public String likeme;
//    public String nolinks;
//    public String signs;
//    public String cards;
//    public String imgs;
//    public String isauth;
//    public String level;
//    public String remark;
//    public String last_at;
//    public String created_at;
//    public String updated_at;
//    public String deleted_at;


    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTruename() {
        return truename;
    }

    public String getMobile() {
        return mobile;
    }

    public String getSex() {
        return sex;
    }

    public String getArea_id() {
        return area_id;
    }

    public String getArea1() {
        return area1;
    }

    public String getArea2() {
        return area2;
    }

    public String getArea3() {
        return area3;
    }

    public String getArea4() {
        return area4;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", truename='" + truename + '\'' +
                ", mobile='" + mobile + '\'' +
                ", sex='" + sex + '\'' +
                ", area_id='" + area_id + '\'' +
                ", area1='" + area1 + '\'' +
                ", area2='" + area2 + '\'' +
                ", area3='" + area3 + '\'' +
                ", area4='" + area4 + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
