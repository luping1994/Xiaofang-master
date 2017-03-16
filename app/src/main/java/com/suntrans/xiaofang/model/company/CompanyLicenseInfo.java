package com.suntrans.xiaofang.model.company;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Looney on 2017/2/13.
 */

public class CompanyLicenseInfo {
    public List<License> project;



    public List<CompanyLicenseItem> company;//社会单位
    public static class License implements Parcelable {
        public String id;
        public String user_id;
        public String cmyname;
        public String name;
        public String addr;
        public String lng;
        public String lat;
        public String contact;
        public String phone;
        public String company_id;
        public List<CompanyLicenseItem> detail;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.user_id);
            dest.writeString(this.cmyname);
            dest.writeString(this.name);
            dest.writeString(this.addr);
            dest.writeString(this.lng);
            dest.writeString(this.lat);
            dest.writeString(this.contact);
            dest.writeString(this.phone);
            dest.writeString(this.company_id);
            dest.writeTypedList(this.detail);
        }

        public License() {
        }

        protected License(Parcel in) {
            this.id = in.readString();
            this.user_id = in.readString();
            this.cmyname = in.readString();
            this.name = in.readString();
            this.addr = in.readString();
            this.lng = in.readString();
            this.lat = in.readString();
            this.contact = in.readString();
            this.phone = in.readString();
            this.company_id = in.readString();
            this.detail = in.createTypedArrayList(CompanyLicenseItem.CREATOR);
        }

        public static final Parcelable.Creator<License> CREATOR = new Parcelable.Creator<License>() {
            @Override
            public License createFromParcel(Parcel source) {
                return new License(source);
            }

            @Override
            public License[] newArray(int size) {
                return new License[size];
            }
        };
    }

    public static class CompanyLicenseItem implements Parcelable {
        public String id;
        public String company_id;
        public String time;
        public String isqualified;
        public String number;
        public String type;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.company_id);
            dest.writeString(this.time);
            dest.writeString(this.isqualified);
            dest.writeString(this.number);
            dest.writeString(this.type);
        }

        public CompanyLicenseItem() {
        }

        protected CompanyLicenseItem(Parcel in) {
            this.id = in.readString();
            this.company_id = in.readString();
            this.time = in.readString();
            this.isqualified = in.readString();
            this.number = in.readString();
            this.type = in.readString();
        }

        public static final Parcelable.Creator<CompanyLicenseItem> CREATOR = new Parcelable.Creator<CompanyLicenseItem>() {
            @Override
            public CompanyLicenseItem createFromParcel(Parcel source) {
                return new CompanyLicenseItem(source);
            }

            @Override
            public CompanyLicenseItem[] newArray(int size) {
                return new CompanyLicenseItem[size];
            }
        };
    }


}
