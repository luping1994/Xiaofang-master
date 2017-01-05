package com.suntrans.xiaofang.views.dialog;

import java.util.ArrayList;

/**
 * Created by Looney on 2016/12/30.
 */

public interface onAttrSelectedListener {
    void onSelectSuccess(MainAttr mainAttr, ArrayList<SubAttr> subData, int type);//1表示只有主属性
    void onSelectFailed(String msg);
}
