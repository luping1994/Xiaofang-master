package com.suntrans.xiaofang.model.supervise;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Looney on 2016/12/23.
 */

public class SuperviseListResult {
    public String status;

    @SerializedName("result")
    public List<Supervise> result;

}
