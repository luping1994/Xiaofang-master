package com.suntrans.xiaofang.model.license;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Looney on 2016/12/23.
 * 行政审批项目条目信息，建审,开业前,验收
 */

public class LicenseItemInfo implements Serializable{

    public String id;
    public String user_id;
    public String license_id;
    public String time;
    public String isqualified;
    public String number;
    public String type;

    @Override
    public String toString() {
        return "LicenseItemInfo{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", license_id='" + license_id + '\'' +
                ", time='" + time + '\'' +
                ", isqualified='" + isqualified + '\'' +
                ", number='" + number + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
