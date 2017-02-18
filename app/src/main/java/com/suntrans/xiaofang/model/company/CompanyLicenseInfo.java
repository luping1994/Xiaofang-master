package com.suntrans.xiaofang.model.company;

/**
 * Created by Looney on 2017/2/13.
 */

public class CompanyLicenseInfo {
    public String id;
    public String user_id;
    public String license_id;
    public String time;
    public String isqualified;
    public String number;
    public String type;
    public License license;

    public static class License{
        public String id;
        public String user_id;
        public String cmyname;
        public String name;
        public String addr;
        public String lng;
        public String lat;
        public String contact;
        public String phone;

    }
}
