package com.suntrans.xiaofang.model.event;

import java.util.List;

/**
 * Created by Looney on 2016/12/22.
 */

public class Result {

    public Event item;
    public String type_ids;
    public List<String> img_before;
    public List<String> img_after;

    public List<String> img_beforeR;
    public List<String> img_afterR;
    public String video_path_1;
    public String video_path_2;

    public RecordNew record2;

    @Override
    public String toString() {
        return "Result{" +
                "item=" + item +
                ", type_ids=" + type_ids +
                ", img_before=" + img_before.get(0) +
                ", img_after=" + img_after.get(0) +
                ", img_beforeR=" + img_beforeR.get(0) +
                ", img_afterR=" + img_afterR.get(0) +
                ", video_path_1='" + video_path_1 + '\'' +
                ", video_path_2='" + video_path_2 + '\'' +
                ", record2=" + record2.toString() +
                '}';
    }
}
