package com.suntrans.xiaofang.model.event;

/**
 * Created by Looney on 2016/12/22.
 */

public class EventDetailResult {
    public String status;

    public Result result;

    @Override
    public String toString() {
        return "EventDetailResult{" +
                "status='" + status + '\'' +
                ", result=" + result.toString() +
                '}';
    }
}
