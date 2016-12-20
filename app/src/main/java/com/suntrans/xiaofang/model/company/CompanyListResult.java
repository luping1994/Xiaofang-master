package com.suntrans.xiaofang.model.company;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Looney on 2016/12/16.
 */

public class CompanyListResult {
    public String status;
    public String msg;

    @SerializedName("result")
    public List<CompanyList> results;
}
