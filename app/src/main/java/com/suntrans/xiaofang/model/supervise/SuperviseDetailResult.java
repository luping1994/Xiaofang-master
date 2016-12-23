package com.suntrans.xiaofang.model.supervise;

/**
 * Created by Looney on 2016/12/23.
 */

public class SuperviseDetailResult {
    public String status;

    public ResultSup result;

    @Override
    public String toString() {
        return "SuperviseDetailResult{" +
                "status='" + status + '\'' +
                ", result=" + result.toString() +
                '}';
    }
}
