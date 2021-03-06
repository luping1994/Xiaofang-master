package com.suntrans.xiaofang.views.dialog;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suntrans.xiaofang.utils.DbHelper;

import java.util.ArrayList;

/**
 * Created by Looney on 2016/12/30.
 */

public class GeneralProvider implements DataProvider {
    private  Context context;

    public GeneralProvider(Context context) {
        this.context = context;
    }


    @Override
    public void provideMainAttr(AddressReceiver<MainAttr> addressReceiver) {
        ArrayList<MainAttr> list = new ArrayList<>();

        DbHelper helper = new DbHelper(context,"Fire",null,1);
        SQLiteDatabase db = helper.getReadableDatabase();
        db.beginTransaction();

        Cursor cursor = db.rawQuery("select Id,Name from attr_general",null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                MainAttr attr = new MainAttr(Integer.valueOf(cursor.getString(0)),cursor.getString(1));
                list.add(attr);
            }
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();

        addressReceiver.send(list);
    }

    @Override
    public void provideSubAttrWith(int mainId, AddressReceiver<SubAttr> addressReceiver) {
        addressReceiver.send(null);
    }
}
