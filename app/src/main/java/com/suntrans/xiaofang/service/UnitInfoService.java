package com.suntrans.xiaofang.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Looney on 2016/12/15.
 */

public interface UnitInfoService {
    @POST("")
    Call<List<String>> ListUnitinfo();
}
