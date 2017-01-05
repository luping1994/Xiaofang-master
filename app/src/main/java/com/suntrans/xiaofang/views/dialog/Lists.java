package com.suntrans.xiaofang.views.dialog;

/**
 * Created by Looney on 2016/12/13.
 */

import java.util.List;

public class Lists {
    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean notEmpty(List list) {
        return list != null && list.size() > 0;
    }
}

