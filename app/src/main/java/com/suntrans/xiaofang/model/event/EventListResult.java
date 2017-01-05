package com.suntrans.xiaofang.model.event;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Looney on 2016/12/22.
 */

public class EventListResult {
    public String status;

    @SerializedName("result")
    public List<Event> result;


}
