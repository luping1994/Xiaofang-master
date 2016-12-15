package com.suntrans.xiaofang.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Looney on 2016/12/14.
 */

public class UnitInfo implements Parcelable {

    protected UnitInfo(Parcel in) {
    }

    public static final Creator<UnitInfo> CREATOR = new Creator<UnitInfo>() {
        @Override
        public UnitInfo createFromParcel(Parcel in) {
            return new UnitInfo(in);
        }

        @Override
        public UnitInfo[] newArray(int size) {
            return new UnitInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
