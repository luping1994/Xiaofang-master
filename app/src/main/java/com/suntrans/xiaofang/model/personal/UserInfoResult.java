package com.suntrans.xiaofang.model.personal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Looney on 2016/12/16.
 */

public class UserInfoResult {
    public int status;

    @SerializedName("result")
    public UserInfo lists;
}
