package com.suntrans.xiaofang.model.company;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Looney on 2016/12/17.
 */

public class  AddCompanyResult {
    @SerializedName("status")
    public String status;

    @SerializedName("result")
    public String result;
    public String msg;
}
