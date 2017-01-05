package com.suntrans.xiaofang.api;

import com.suntrans.xiaofang.model.company.AddCompanyResult;
import com.suntrans.xiaofang.model.company.CompanyDetailnfoResult;
import com.suntrans.xiaofang.model.company.CompanyListResult;
import com.suntrans.xiaofang.model.company.CompanyPassResult;
import com.suntrans.xiaofang.model.event.EventDetailResult;
import com.suntrans.xiaofang.model.event.EventListResult;
import com.suntrans.xiaofang.model.firegroup.AddFireGroupResult;
import com.suntrans.xiaofang.model.firegroup.FireGroupDetailResult;
import com.suntrans.xiaofang.model.fireroom.AddFireRoomResult;
import com.suntrans.xiaofang.model.fireroom.FireComponentGeneralInfoList;
import com.suntrans.xiaofang.model.fireroom.FireRoomDetailResult;
import com.suntrans.xiaofang.model.firestation.AddFireStationResult;
import com.suntrans.xiaofang.model.firestation.FireStationDetailResult;
import com.suntrans.xiaofang.model.license.AddLicenseResult;
import com.suntrans.xiaofang.model.license.LicenseDetailResult;
import com.suntrans.xiaofang.model.login.LoginInfo;
import com.suntrans.xiaofang.model.personal.CPasswordResult;
import com.suntrans.xiaofang.model.personal.UserInfoResult;
import com.suntrans.xiaofang.model.supervise.SuperviseDetailResult;
import com.suntrans.xiaofang.model.supervise.SuperviseListResult;

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
     *
     * @param grant_type    默认填password
     * @param client_id     默认填6
     * @param client_secret 默认填test
     * @param username      账号
     * @param password      密码
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
     * @return 获得个人信息
     */
    @POST("/api/v1/user/profile")
    Observable<UserInfoResult> getUserInfo();

    /**
     * 根据坐标以及范围查找单位信息
     *
     * @param lng     中心点的经度
     * @param Lat     中心点的纬度
     * @param lngbias
     * @param latbias
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/company/range")
    Call<CompanyListResult> getCompanyList(@Field("lng") String lng,
                                           @Field("lat") String Lat,
                                           @Field("lngbias") String lngbias,
                                           @Field("latbias") String latbias);

    /**
     * 获取等待审核的单位
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/social/all")
    Call<CompanyListResult> getCompanyCheck(@Field("id") String id);


    /**
     * 获取等待审核的单位详情
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/social/detail")
    Call<CompanyDetailnfoResult> getCompanyCheckDetail(@Field("id") String id);

    /**
     * 获取公司详情
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/company/detail")
    Call<CompanyDetailnfoResult> getCompanyDetail(@Field("id") String id);


    /**
     * 添加公司信息
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/company/create")
    Call<AddCompanyResult> createCompany(@FieldMap Map<String, String> info);

    /**
     * 删除公司信息
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/company/delete")
    Observable<AddCompanyResult> deleteCompany(@Field("id") String id);

    /**
     * 更新公司信息
     *
     * @param info
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/company/update")
    Call<AddCompanyResult> updateCompany(@FieldMap Map<String, String> info);


    /**
     * 根据关键词查询公司信息
     *
     * @param name
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/company/search")
    Call<CompanyListResult> queryCompany(@Field("name") String name);


    /**
     * 根据关键词查询fireroom信息
     *
     * @param name
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/fireroom/search")
    Call<CompanyListResult> queryFireroom(@Field("name") String name);


    /**
     * 根据关键词查询firestation信息
     *
     * @param name
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/firestation/search")
    Call<CompanyListResult> queryFirestation(@Field("name") String name);


    /**
     * 根据关键词查询firegroup信息
     *
     * @param name
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/firegroup/search")
    Call<CompanyListResult> queryFiregroup(@Field("name") String name);

    /**
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/social/pass")
    Call<CompanyPassResult> passCompany(@Field("id") String id);


    /**
     * 社会单位通过
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/social/unpass")
    Call<CompanyPassResult> unpassCompany(@Field("id") String id);


    /**
     * 获取消防室一定范围内的记录
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/fireroom/range")
    Observable<FireComponentGeneralInfoList> getFireroomList(@Field("lng") String lng,
                                                             @Field("lat") String lat);

    /**
     * 获取小型站一定范围内的记录
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/firestation/range")
    Observable<FireComponentGeneralInfoList> getFirestationList(@Field("lng") String lng,
                                                                @Field("lat") String lat);

    /**
     * 获取消防大队一定范围内的记录
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/firegroup/range")
    Observable<FireComponentGeneralInfoList> getFireGroupList(@Field("lng") String lng,
                                                              @Field("lat") String lat);

    /**
     * 新增设去消防室
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/fireroom/create")
    Observable<AddFireRoomResult> createFireRoom(@FieldMap Map<String, String> map);

    /**
     * 删除消防室
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/fireroom/delete")
    Observable<AddFireRoomResult> deleteFireRoom(@Field("id") String id);


    @FormUrlEncoded
    @POST("/api/v1/fireroom/detail")
    Observable<FireRoomDetailResult> getFireRoomDetailInfo(@Field("id") String id);

    @FormUrlEncoded
    @POST("/api/v1/fireroom/update")
    Observable<AddFireRoomResult> updateFireRoom(@FieldMap Map<String, String> map);

    /**
     * 创建小型站
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/firestation/create")
    Observable<AddFireStationResult> createStation(@FieldMap Map<String, String> map);

    /**
     * 删除小型站
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/firestation/delete")
    Observable<AddFireStationResult> deleteStation(@Field("id") String id);


    @FormUrlEncoded
    @POST("/api/v1/firestation/detail")
    Observable<FireStationDetailResult> getFireStationDetailInfo(@Field("id") String id);

    @FormUrlEncoded
    @POST("/api/v1/firestation/update")
    Observable<AddFireStationResult> updateFireStation(@FieldMap Map<String, String> map);

    /**
     * 创建中队
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/firegroup/create")
    Observable<AddFireGroupResult> createGroup(@FieldMap Map<String, String> map);


    /**
     * 删除中队
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/firegroup/delete")
    Observable<AddFireGroupResult> deleteGroup(@Field("id") String id);


    @FormUrlEncoded
    @POST("/api/v1/firegroup/detail")
    Observable<FireGroupDetailResult> getFireGroupDetailInfo(@Field("id") String id);

    @FormUrlEncoded
    @POST("/api/v1/firegroup/update")
    Observable<AddFireGroupResult> updateFireGroup(@FieldMap Map<String, String> map);


    /**
     * 行政许可建审查
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/license/create")
    Observable<AddLicenseResult> createLicense(@FieldMap Map<String, String> map);

    /**
     * 删除行政许可
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/license/delete")
    Observable<AddLicenseResult> deleteLicense(@Field("id") String id);

    /**
     * 一定范围内的行政许可建筑
     *
     * @param lng
     * @param lat
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/license/range")
    Observable<FireComponentGeneralInfoList> getLicenseList(@Field("lng") String lng,
                                                            @Field("lat") String lat);


    /**
     * 行政许可详情
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/license/detail")
    Observable<LicenseDetailResult> getLicenseDetailInfo(@Field("id") String id);

    /**
     * 验收接口
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/license/complete")
    Observable<AddLicenseResult> completeLicense(@FieldMap Map<String, String> map);


    /**
     * 开业前接口
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/license/open")
    Observable<AddLicenseResult> openLicense(@FieldMap Map<String, String> map);

    /**
     *
     *根据单位id获取事件
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/event/search")
    Observable<EventListResult> getEventList(@Field("id") String id);


    /**
     *
     *根据单位id获取事件
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/event/detail")
    Observable<EventDetailResult> getEventDetail(@Field("id") String id);


    /**
     *
     *根据单位id获取监督
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/issued/search")
    Observable<SuperviseListResult> getSuperviseList(@Field("id") String id);

    /**
     *
     *根据单位id获取监督详情
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/issued/detail")
    Observable<SuperviseDetailResult> getSuperviseDetail(@Field("id") String id);

    //api/v1/license/range

    /**
     *
     *根据单位id获取监督详情
     * @param contents
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/guestbook/create")
    Observable<AddCompanyResult> commitGuesBook(@Field("contents") String contents);


    /**
     *
     *根据单位id获取监督详情
     * @param contents
     * @return
     */
    @FormUrlEncoded
    @POST("/api/v1/user/password")
    Observable<CPasswordResult> changedPassword(@Field("password") String contents);

}

