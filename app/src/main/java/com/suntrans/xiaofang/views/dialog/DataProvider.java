package com.suntrans.xiaofang.views.dialog;

import java.util.List;


public interface DataProvider {
    void provideMainAttr(AddressReceiver<MainAttr> addressReceiver);
    void provideSubAttrWith(int mainId, AddressReceiver<SubAttr> addressReceiver);


    interface AddressReceiver<T> {
        void send(List<T> data);
    }
}