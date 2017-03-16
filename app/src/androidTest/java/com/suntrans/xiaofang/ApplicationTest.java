package com.suntrans.xiaofang;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test(){
        int flag = 0x01;//显示哪中类型单位标记
        int flag_biao = 0x01;
        int flag_Reversal = 0xfe;

        System.out.println("1==>"+(flag | flag_biao<<0));
        System.out.println("1==>"+(flag | flag_biao<<1));
        System.out.println("1==>"+(flag | flag_biao<<2));
        System.out.println("1==>"+(flag | flag_biao<<3));
        System.out.println("1==>"+(flag | flag_biao<<4));
        System.out.println("1==>"+(flag | flag_biao<<5));
        System.out.println("1==>"+(flag | flag_biao<<6));
        System.out.println("1==>"+(flag | flag_biao<<7));
    }
}