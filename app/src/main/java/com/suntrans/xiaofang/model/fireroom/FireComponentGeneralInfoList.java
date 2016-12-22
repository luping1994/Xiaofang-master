package com.suntrans.xiaofang.model.fireroom;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Looney on 2016/12/20.
 */

public class FireComponentGeneralInfoList {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("result")
    public List<FireComponentGeneralInfo> result;
}
