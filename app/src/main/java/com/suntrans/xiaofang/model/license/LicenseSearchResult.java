package com.suntrans.xiaofang.model.license;

import com.suntrans.xiaofang.model.company.CompanyLicenseInfo;

import java.util.List;

/**
 * Created by Looney on 2017/2/13.
 */

public class LicenseSearchResult {
    public String status;
    public String msg;
    public List<CompanyLicenseInfo.License> result;
}
