package com.suntrans.xiaofang.model.company;

/**
 * Created by Looney on 2017/2/9.
 */

public class InchargeInfo {
    public String id;
    public String name;
    public String parent_id_path;

    @Override
    public String toString() {
        return "InchargeInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parent_id_path='" + parent_id_path + '\'' +
                '}';
    }
}
