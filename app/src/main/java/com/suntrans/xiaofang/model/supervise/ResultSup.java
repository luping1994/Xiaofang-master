package com.suntrans.xiaofang.model.supervise;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Looney on 2016/12/23.
 */
public class ResultSup {

    @SerializedName("item")
    public Supervise item;

    public List<String> imgs;
    public List<String> imgraws;

}
