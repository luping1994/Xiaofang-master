package com.suntrans.xiaofang.service;

import com.suntrans.xiaofang.model.company.AddCompanyResult;
import com.suntrans.xiaofang.model.company.CompanyDetailnfoResult;
import com.suntrans.xiaofang.model.company.CompanyListResult;
import com.suntrans.xiaofang.model.company.CompanyPassResult;
import com.suntrans.xiaofang.model.login.LoginInfo;
import com.suntrans.xiaofang.model.personal.UserInfoResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Looney on 2016/12/15.
 */

public interface Api {
    /**
     * 登录api
     * @param grant_type
     * @param client_id
     * @param client_secret
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("/oauth/token")
    Call<LoginInfo> login(@Field("grant_type") String grant_type,
                          @Field("client_id") String client_id,
                          @Field("client_secret") String client_secret,
                          @Field("username") String username,
                          @Field("password") String password
                          );

    /**
     * 查询个人信息api
     *
     * @return
     */
    @POST("/api/v1/user/profile")
    Observable<UserInfoResult> getUserInfo();

    /**
     * 根据坐标以及范围查找单位信息
     * @param lat
     * @param Lat
     * @param lngbias
     * @param latbias
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/company/range")
    Call<CompanyListResult> getCompanyList(@Field("lng") String lat,
                                           @Field("lat") String Lat,
                                           @Field("lngbias") String lngbias,
                                           @Field("latbias") String latbias);

    /**
     * 获取等待审核的单位
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/social/all")
    Call<CompanyListResult> getCompanyCheck(@Field("id") String id);



    /**
     * 获取等待审核的单位详情
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/social/detail")
    Call<CompanyDetailnfoResult> getCompanyCheckDetail(@Field("id") String id);

    /**
     * 获取公司详情
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/company/detail")
    Call<CompanyDetailnfoResult> getCompanyDetail(@Field("id") String id);


    /**
     * 添加公司信息
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/company/create")
    Call<AddCompanyResult> createCompany(@FieldMap Map<String,String> info);

    /**
     * 更新公司信息
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/company/update")
    Call<AddCompanyResult> updateCompany(@FieldMap Map<String,String> info);




    /**
     * 根据关键词查询公司信息
     * @param name
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/company/search")
    Call<CompanyListResult> queryCompany(@Field("name") String name);

    /**
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/social/pass")
    Call<CompanyPassResult> passCompany(@Field("id") String id);


    /**
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/social/unpass")
    Call<CompanyPassResult> unpassCompany(@Field("id") String id);
}

