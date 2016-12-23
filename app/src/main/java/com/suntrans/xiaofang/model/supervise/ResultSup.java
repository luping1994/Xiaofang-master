package com.suntrans.xiaofang.model.supervise;

import java.util.List;

/**
 * Created by Looney on 2016/12/23.
 */
public class ResultSup {

    Supervise item;
    public List<String> imgs;
    public List<String> imgraws;

    @Override
    public String toString() {
        return "ResultSup{" +
                "item=" + item.toString()+
                ", imgs=" + imgs.get(0) +
                ", imgraws=" + imgraws.get(0) +
                '}';
    }
}
