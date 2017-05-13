package com.suntrans.xiaofang.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.SparseArray;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.Main_Activity;
import com.suntrans.xiaofang.model.company.CompanyList;
import com.suntrans.xiaofang.model.company.CompanyListResult;
import com.suntrans.xiaofang.model.fireroom.FireComponentGeneralInfo;
import com.suntrans.xiaofang.model.fireroom.FireComponentGeneralInfoList;
import com.suntrans.xiaofang.model.map.RegionItem;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2017/1/18.
 */

public class MarkerHelper {

    private static final String TAG = "MarkerHelper";
    public static final int S0CIETY = 0;//标识社会单位
    public static final int FIREROOM = 1;//标识社区消防室标识
    public static final int FIRESTATION = 2;//标识乡村小型站
    public static final int FIREGROUP = 3;//标识消防中队
    public static final int LICENSE = 4;//标识行政审批
    public static final int FIREBRIGADE = 5;//标识消防大队
    public static final int FIREADMINSTATION = 6;//标识政府小型站
    public static final int COMMONCOMPANY = 7;//标识一般单位

    public static final int ERROR_EMPTY = 0;
    public static final int ERROR_FAILED = 1;

    public static SparseArray<String> com = new SparseArray<>();
    Main_Activity activity;
    private onGetInfoFinishListener listener;

    public MarkerHelper(Main_Activity activity) {
        this.activity = activity;
        com.put(MarkerHelper.S0CIETY, "重点单位");
        com.put(MarkerHelper.FIREROOM, "社区消防室");
        com.put(MarkerHelper.FIRESTATION, "消防大队");
        com.put(MarkerHelper.FIREGROUP, "消防中队");
        com.put(MarkerHelper.LICENSE, "行政审批");
        com.put(MarkerHelper.FIREADMINSTATION, "政府专职小型站");
        com.put(MarkerHelper.COMMONCOMPANY, "一般社会单位");
        com.put(MarkerHelper.FIRESTATION, "乡镇专职消防队");
    }


    public void setOnGetInfoFinishListener(MarkerHelper.onGetInfoFinishListener onGetInfoFinishListener) {
        this.listener = onGetInfoFinishListener;
    }

    public interface onGetInfoFinishListener {

        void onCompanyDataFinish(List<RegionItem> items);

        void onCommCmyDataFinish(List<RegionItem> items);

        void onFireRoomDataFinish(List<RegionItem> items);

        void onFireBrigadeDataFinish(List<RegionItem> items);

        void onFireGroupDataFinish(List<RegionItem> items);

        void onFireAdminStationDataFinish(List<RegionItem> items);

        void onFireStationDataFinish(List<RegionItem> items);

        void onLicenseDataFinish(List<RegionItem> items);

        void onLoadFailure(int code,int type);
    }


    //获重点单位位置
    public int getCompanyList(LatLng center, String distance, String special) {
        final int[] size = {0};
        RetrofitHelper.getApi().getCompanyList(String.valueOf(center.longitude), String.valueOf(center.latitude), distance, "1")
                .compose(activity.<CompanyListResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<CompanyListResult>() {
                    @Override
                    public void call(CompanyListResult result) {

                        if (result == null) {
                            LogUtil.e(TAG, "重点单位数据为空");
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,S0CIETY);
                            return;
                        }
                        if (!result.status.equals("0") && result != null) {
                            List<CompanyList> lists = result.results;

                            List<RegionItem> items = new ArrayList<RegionItem>();
                            for (CompanyList info : lists) {
                                if (info.lat == null || info.lng == null) {
                                    continue;
                                }
                                double lat = Double.valueOf(info.lat);
                                double lng = Double.valueOf(info.lng);
                                LatLng src = new LatLng(lat, lng);
                                RegionItem item = new RegionItem(src, "");
                                item.setId(info.id);
                                item.setName(info.name);
                                item.setAddr(info.address);
                                item.setType(MarkerHelper.S0CIETY);
                                items.add(item);
                            }

                            listener.onCompanyDataFinish(items);

                        } else {
                            LogUtil.e("company:", result.msg);
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,S0CIETY);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        listener.onLoadFailure(MarkerHelper.ERROR_FAILED,S0CIETY);

                    }
                });
        return 0;
    }

    //一般单位
    public int getCommcmyList(LatLng center, String distance) {
        final int[] size = {0};
        RetrofitHelper.getApi().getCommcmyList(String.valueOf(center.longitude), String.valueOf(center.latitude), distance)
                .compose(activity.<CompanyListResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<CompanyListResult>() {
                    @Override
                    public void call(CompanyListResult result) {

                        if (result == null) {
                            LogUtil.e(TAG, "一般单位数据为空");
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,COMMONCOMPANY);
                            return;
                        }
                        if (!result.status.equals("0") && result != null) {
                            List<CompanyList> lists = result.results;

                            List<RegionItem> items = new ArrayList<RegionItem>();
                            for (CompanyList info : lists) {
                                if (info.lat == null || info.lng == null) {
                                    continue;
                                }
                                double lat = Double.valueOf(info.lat);
                                double lng = Double.valueOf(info.lng);
                                LatLng src = new LatLng(lat, lng);
                                RegionItem item = new RegionItem(src, "");
                                item.setId(info.id);
                                item.setName(info.name);
                                item.setAddr(info.address);
                                item.setType(MarkerHelper.COMMONCOMPANY);
                                items.add(item);
                            }

                            listener.onCommCmyDataFinish(items);

                        } else {
                            LogUtil.e("commCmy:", result.msg);
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,COMMONCOMPANY);

                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        listener.onLoadFailure(MarkerHelper.ERROR_FAILED,COMMONCOMPANY);

                    }
                });
        return 0;
    }

    /**
     * 获取消防室数据
     */
    public int getFireRoomList(LatLng center, String distance) {
        RetrofitHelper.getApi().getFireroomList(String.valueOf(center.longitude), String.valueOf(center.latitude), distance)
                .compose(activity.<FireComponentGeneralInfoList>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FireComponentGeneralInfoList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onLoadFailure(MarkerHelper.ERROR_FAILED,FIREROOM);
                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            LogUtil.e(TAG, "fireroom数据为空");
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,FIREROOM);
                            return;
                        }
                        if (list.status.equals("1")) {
                            List<FireComponentGeneralInfo> lists = list.result;

                            List<RegionItem> items = new ArrayList<RegionItem>();
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat == null || info.lng == null) {
                                    continue;
                                }
                                double lat = Double.valueOf(info.lat);
                                double lng = Double.valueOf(info.lng);
                                LatLng src = new LatLng(lat, lng);
                                RegionItem item = new RegionItem(src, "");
                                item.setId(info.id);
                                item.setName(info.name);
                                item.setAddr(info.addr);
                                item.setType(MarkerHelper.FIREROOM);
                                items.add(item);
                            }
                            listener.onFireRoomDataFinish(items);
//
                        } else {
                            LogUtil.e("fireroom", list.msg);
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,FIREROOM);

                        }
                    }
                });
        return 0;
    }


    /**
     * 获取消防大队
     */
    public int getFireBrigadeList(LatLng center, String distance) {
        RetrofitHelper.getApi().getFirebrigade(String.valueOf(center.longitude), String.valueOf(center.latitude), distance)
                .compose(activity.<FireComponentGeneralInfoList>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FireComponentGeneralInfoList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onLoadFailure(MarkerHelper.ERROR_FAILED,FIREBRIGADE);
                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            LogUtil.e(TAG, "firebrigade数据为空");
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,FIREBRIGADE);
                            return;
                        }
                        if (list.status.equals("1")) {
                            List<RegionItem> items = new ArrayList<RegionItem>();
                            List<FireComponentGeneralInfo> lists = list.result;
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat != null || info.lng != null || !TextUtils.equals("", String.valueOf(info.lat)) || !TextUtils.equals("", String.valueOf(info.lng))) {
//                                    LogUtil.i("小型站" + info.toString());
                                    double lat = Double.valueOf(info.lat);
                                    double lng = Double.valueOf(info.lng);
                                    LatLng src = new LatLng(lat, lng);
                                    RegionItem item = new RegionItem(src, "");
                                    item.setId(info.id);
                                    item.setName(info.name);
                                    item.setAddr(info.addr);
                                    item.setType(MarkerHelper.FIREBRIGADE);
                                    items.add(item);
                                }
                            }
                            listener.onFireBrigadeDataFinish(items);
                        } else {
                            LogUtil.e("FIREBrigade", list.msg);
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,FIREBRIGADE);

                        }
                    }
                });
        return 0;
    }


    /**
     * 获取消防中队数据
     */
    public int getFireGroupList(LatLng center, String distance) {
        final int[] size = {0};
        RetrofitHelper.getApi().getFireGroupList(String.valueOf(center.longitude), String.valueOf(center.latitude), distance)
                .compose(activity.<FireComponentGeneralInfoList>bindUntilEvent(ActivityEvent.DESTROY))
//        RetrofitHelper.getApi().getFireGroupList("114.342014", "30.547186")
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FireComponentGeneralInfoList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onLoadFailure(MarkerHelper.ERROR_FAILED,FIREGROUP);
                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            LogUtil.e(TAG, "firegroup数据为空");
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,FIREGROUP);

                            return;
                        }
                        if (list.status.equals("1")) {

                            List<FireComponentGeneralInfo> lists = list.result;
                            List<RegionItem> items = new ArrayList<RegionItem>();
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat == null || info.lng == null) {
                                    continue;
                                }
//                                LogUtil.i(info.toString());
                                double lat = Double.valueOf(info.lat);
                                double lng = Double.valueOf(info.lng);
                                LatLng src = new LatLng(lat, lng);
                                RegionItem item = new RegionItem(src, "");
                                item.setId(info.id);
                                item.setName(info.name);
                                item.setAddr(info.addr);
                                item.setType(MarkerHelper.FIREGROUP);
                                items.add(item);
                            }

                            listener.onFireGroupDataFinish(items);
                        } else {
                            LogUtil.e("firegroup", list.msg);
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,FIREGROUP);
                        }
                    }
                });
        return 0;
    }


    /**
     * 获取政府station数据
     */
    public int getFireAdminStationList(LatLng center, String distance) {
        RetrofitHelper.getApi().getFireadminstation(String.valueOf(center.longitude), String.valueOf(center.latitude), distance)
                .compose(activity.<FireComponentGeneralInfoList>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FireComponentGeneralInfoList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onLoadFailure(MarkerHelper.ERROR_FAILED,FIREADMINSTATION);

                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            LogUtil.e(TAG, "获取政府station数据为空");
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,FIREADMINSTATION);

                            return;
                        }
                        if (list.status.equals("1")) {
                            List<RegionItem> items = new ArrayList<RegionItem>();
                            List<FireComponentGeneralInfo> lists = list.result;
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat != null || info.lng != null || !TextUtils.equals("", String.valueOf(info.lat)) || !TextUtils.equals("", String.valueOf(info.lng))) {
//                                    LogUtil.i("小型站" + info.toString());
                                    double lat = Double.valueOf(info.lat);
                                    double lng = Double.valueOf(info.lng);
                                    LatLng src = new LatLng(lat, lng);
                                    RegionItem item = new RegionItem(src, "");
                                    item.setId(info.id);
                                    item.setName(info.name);
                                    item.setAddr(info.addr);
                                    item.setType(MarkerHelper.FIREADMINSTATION);
                                    items.add(item);
                                }
                            }
                            listener.onFireAdminStationDataFinish(items);
                        } else {
                            LogUtil.i("FIREADMINSTATION", list.msg);
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,FIREADMINSTATION);
                        }
                    }
                });
        return 0;
    }


    /**
     * 获取station数据
     */
    public int getFireStationList(LatLng center, String distance) {
        RetrofitHelper.getApi().getFirestationList(String.valueOf(center.longitude), String.valueOf(center.latitude), distance)
                .compose(activity.<FireComponentGeneralInfoList>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FireComponentGeneralInfoList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onLoadFailure(MarkerHelper.ERROR_FAILED,FIRESTATION);

                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            LogUtil.e(TAG, "firestation数据为空");
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,FIRESTATION);

                            return;
                        }
                        if (list.status.equals("1")) {
                            List<RegionItem> items = new ArrayList<RegionItem>();
                            List<FireComponentGeneralInfo> lists = list.result;
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat != null || info.lng != null || !TextUtils.equals("", String.valueOf(info.lat)) || !TextUtils.equals("", String.valueOf(info.lng))) {
//                                    LogUtil.i("小型站" + info.toString());
                                    double lat = Double.valueOf(info.lat);
                                    double lng = Double.valueOf(info.lng);
                                    LatLng src = new LatLng(lat, lng);
                                    RegionItem item = new RegionItem(src, "");
                                    item.setId(info.id);
                                    item.setName(info.name);
                                    item.setAddr(info.addr);
                                    item.setType(MarkerHelper.FIRESTATION);
                                    items.add(item);
                                }
                            }
                            listener.onFireStationDataFinish(items);
                        } else {
                            LogUtil.e("firestation", list.msg);
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,FIRESTATION);
                        }
                    }
                });
        return 0;
    }


    /**
     * 获取行政许可建筑数据
     */
    public int getLicenseList(LatLng center, String distance) {
        RetrofitHelper.getApi().getLicenseList(String.valueOf(center.longitude), String.valueOf(center.latitude), distance)
                .compose(activity.<FireComponentGeneralInfoList>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<FireComponentGeneralInfoList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        listener.onLoadFailure(MarkerHelper.ERROR_FAILED,LICENSE);

                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,LICENSE);

                            return;
                        }
                        if (list.status.equals("1")) {
                            List<RegionItem> items = new ArrayList<RegionItem>();
                            List<FireComponentGeneralInfo> lists = list.result;
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat == null || info.lng == null) {
                                    continue;
                                }
//                                LogUtil.i(info.toString());
                                double lat = Double.valueOf(info.lat);
                                double lng = Double.valueOf(info.lng);
                                LatLng src = new LatLng(lat, lng);
                                RegionItem item = new RegionItem(src, "");
                                item.setId(info.id);
                                item.setName(info.name);
                                item.setAddr(info.addr);
                                item.setType(MarkerHelper.LICENSE);
                                items.add(item);
                            }
                            listener.onLicenseDataFinish(items);
                        } else {
                            listener.onLoadFailure(MarkerHelper.ERROR_EMPTY,LICENSE);
                            LogUtil.e("LICENSE", list.msg);
                        }
                    }
                });
        return 0;
    }
}
