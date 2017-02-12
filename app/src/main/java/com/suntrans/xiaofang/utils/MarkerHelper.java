package com.suntrans.xiaofang.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

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
    public static final int S0CIETY = 0;//标识社会单位
    public static final int FIREROOM = 1;//标识社区消防室标识
    public static final int FIRESTATION = 2;//标识乡村小型站
    public static final int FIREGROUP = 3;//标识消防中队
    public static final int LICENSE = 4;//标识行政审批
    public static final int FIREBRIGADE = 5;//标识消防大队
    public static final int FIREADMINSTATION = 6;//标识政府小型站
    Bitmap fireroomBitmap;
    Bitmap firegroupBitmap;
    Bitmap firestationBitmap;
    Bitmap companyBitmap;
    Bitmap zddwBitmap;


    Main_Activity activity;

    public MarkerHelper(Main_Activity activity){
        fireroomBitmap = BitmapFactory.decodeResource(App.getApplication().getResources(), R.drawable.ic_fireroom);
        firegroupBitmap = BitmapFactory.decodeResource(App.getApplication().getResources(), R.drawable.ic_firegroup);
        firestationBitmap = BitmapFactory.decodeResource(App.getApplication().getResources(), R.drawable.firestation_marker);
        companyBitmap = BitmapFactory.decodeResource(App.getApplication().getResources(), R.drawable.company);
        zddwBitmap = BitmapFactory.decodeResource(App.getApplication().getResources(), R.drawable.zddw);

        this.activity = activity;
    }

    //获取附近的社会单位位置
    public int getCompanyInfo( LatLng center, String distance) {
        final int[] size = {0};
        RetrofitHelper.getApi().getCompanyList(String.valueOf(center.longitude), String.valueOf(center.latitude), distance)
                .compose(activity.<CompanyListResult>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<CompanyListResult>() {
                    @Override
                    public void call(CompanyListResult result) {

                        if (result == null) {
                            UiUtils.showToast(App.getApplication(), "获取服务器单位信息数据失败!");
                            return;
                        }
                        if (!result.status.equals("0") && result != null) {
                            List<CompanyList> lists = result.results;
                            ArrayList<MarkerOptions> optionses = new ArrayList<MarkerOptions>();
                            for (CompanyList info : lists) {
                                if (info.lat == null || info.lng == null) {
                                    continue;
                                }
                                double lat = Double.valueOf(info.lat);
                                double lng = Double.valueOf(info.lng);
                                LatLng src = new LatLng(lat, lng);
                                if (info.special != null) {
                                    if (info.special.equals("1")) {
                                        MarkerOptions marker = new MarkerOptions()
                                                .position(src).title(info.name + "#" + info.address)
                                                .snippet(info.id + "#" + S0CIETY + "#" + "1")
                                                .icon(BitmapDescriptorFactory.fromBitmap(zddwBitmap));
                                        optionses.add(marker);
                                    } else {
                                        MarkerOptions marker = new MarkerOptions()
                                                .position(src).title(info.name + "#" + info.address)
                                                .snippet(info.id + "#" + S0CIETY + "#" + "1")
                                                .icon(BitmapDescriptorFactory.fromBitmap(companyBitmap));
                                        optionses.add(marker);
                                    }

                                }
                            }
                            listener.onCompanyInfoGetFinish(optionses);

//                            showCompanyMarker();
                        } else {
//                    UiUtils.showToast(App.getApplication(), result.msg);
                            LogUtil.i(result.msg);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        UiUtils.showToast("请求失败");
                    }
                });
        return 0;
    }




    /**
     * 获取消防室数据
     */
    public int getFireRoomList(LatLng center, String distance) {
        System.out.println("消防室请求距离="+distance+"km");
        final int[] size = {0};
        RetrofitHelper.getApi().getFireroomList(String.valueOf(center.longitude), String.valueOf(center.latitude),distance)
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
                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            UiUtils.showToast(App.getApplication(), "获取数据失败");
                            return;
                        }
                        if (list.status.equals("1")) {
                            List<FireComponentGeneralInfo> lists = list.result;
                            ArrayList<MarkerOptions> optionses = new ArrayList<MarkerOptions>();

                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat == null || info.lng == null) {
                                    continue;
                                }

                                double lat = Double.valueOf(info.lat);
                                double lng = Double.valueOf(info.lng);
                                LatLng src = new LatLng(lat, lng);
                                //title后面拼凑字符串 name + addr;snippet 内容为 id+ 单位类型(社会单位、消防室四种，用于详情activity决定使用哪个fragment)+
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(src).title(info.name + "#" + info.addr)
                                        .snippet(info.id + "#" + FIREROOM)
                                        .icon(BitmapDescriptorFactory.fromBitmap(fireroomBitmap));
                                optionses.add(markerOptions);
                            }

                            listener.onFireRoomInfoGetFinish(optionses);
//
                        } else {
                            LogUtil.i("fireroom", list.msg);
                        }
                    }
                });
        return 0;
    }


    /**
     * 获取station数据
     */
    public int  getFirStationList(LatLng center, String distance) {
        RetrofitHelper.getApi().getFirestationList(String.valueOf(center.longitude), String.valueOf(center.latitude),distance)
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
                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            UiUtils.showToast(App.getApplication(), "获取数据失败");
                            return;
                        }
                        if (list.status.equals("1")) {
                            ArrayList<MarkerOptions> optionses = new ArrayList<MarkerOptions>();
                            List<FireComponentGeneralInfo> lists = list.result;
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat != null || info.lng != null || !TextUtils.equals("", String.valueOf(info.lat)) || !TextUtils.equals("", String.valueOf(info.lng))) {
//                                    LogUtil.i("小型站" + info.toString());
                                    double lat = Double.valueOf(info.lat);
                                    double lng = Double.valueOf(info.lng);
                                    LatLng src = new LatLng(lat, lng);

                                    //title后面拼凑字符串 name + addr;snippet 内容为 id+ 单位类型(社会单位、消防室四种，用于详情activity决定使用哪个fragment)+
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(src)
                                            .title(info.name + "#" + info.addr)
                                            .snippet(info.id + "#" + FIRESTATION)
                                            .icon(BitmapDescriptorFactory.fromBitmap(firestationBitmap));
                                    optionses.add(markerOptions);
                                }
                            }
                            listener.onFireStationInfoGetFinish(optionses);
                        } else {
                            LogUtil.i("firestation", list.msg);
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
        RetrofitHelper.getApi().getFireGroupList(String.valueOf(center.longitude), String.valueOf(center.latitude),"3")
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
                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            UiUtils.showToast(App.getApplication(), "获取数据失败");
                            return;
                        }
                        if (list.status.equals("1")) {
                            ArrayList<MarkerOptions> optionses = new ArrayList<MarkerOptions>();

                            List<FireComponentGeneralInfo> lists = list.result;
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat == null || info.lng == null) {
                                    continue;
                                }
//                                LogUtil.i(info.toString());
                                double lat = Double.valueOf(info.lat);
                                double lng = Double.valueOf(info.lng);
                                LatLng src = new LatLng(lat, lng);
                                //title后面拼凑字符串 name + addr;snippet 内容为 id+ 单位类型(社会单位、消防室四种，用于详情activity决定使用哪个fragment)+
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(src).title(info.name + "#" + info.addr)
                                        .snippet(info.id + "#" + FIREGROUP)

                                        .icon(BitmapDescriptorFactory.fromBitmap(firegroupBitmap));
                                optionses.add(markerOptions);
                            }
                            listener.onFireGroupInfoGetFinish(optionses);
                        } else {
                            LogUtil.i("firegroup", list.msg);
                        }
                    }
                });
        return 0;
    }


    /**
     * 获取行政许可建筑数据
     */
    public int getLicenseList(LatLng center, String distance) {
        RetrofitHelper.getApi().getLicenseList(String.valueOf(center.longitude), String.valueOf(center.latitude),distance)
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
                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            UiUtils.showToast(App.getApplication(), "获取数据失败");
                            return;
                        }
                        if (list.status.equals("1")) {
                            ArrayList<MarkerOptions> optionses = new ArrayList<MarkerOptions>();

                            List<FireComponentGeneralInfo> lists = list.result;
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat == null || info.lng == null) {
                                    continue;
                                }
//                                LogUtil.i(info.toString());
                                double lat = Double.valueOf(info.lat);
                                double lng = Double.valueOf(info.lng);
                                LatLng src = new LatLng(lat, lng);
                                //title后面拼凑字符串 name + addr;snippet 内容为 id+ 单位类型(社会单位、消防室四种，用于详情activity决定使用哪个fragment)+
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(src).title(info.name + "#" + info.addr)
                                        .snippet(info.id + "#" + LICENSE)
                                        .icon(BitmapDescriptorFactory.fromBitmap(fireroomBitmap));
                                optionses.add(markerOptions);
                            }
                            listener.onLicenseInfoGetFinish(optionses);
                        } else {
                            LogUtil.i("LICENSE", list.msg);
                        }
                    }
                });
        return 0;
    }


    public void setOnGetInfoFinishListener(MarkerHelper.onGetInfoFinishListener onGetInfoFinishListener) {
        this.listener = onGetInfoFinishListener;
    }

    private onGetInfoFinishListener listener;
    public interface onGetInfoFinishListener{
        void onCompanyInfoGetFinish(ArrayList<MarkerOptions> optionses);
        void onFireRoomInfoGetFinish(ArrayList<MarkerOptions> optionses);
        void onFireStationInfoGetFinish(ArrayList<MarkerOptions> optionses);
        void onFireGroupInfoGetFinish(ArrayList<MarkerOptions> optionses);
        void onLicenseInfoGetFinish(ArrayList<MarkerOptions> optionses);

        void onFireAdminStationInfoGetFinish(ArrayList<MarkerOptions> optionses);
        void onFireBrigadeInfoGetFinish(ArrayList<MarkerOptions> optionses);
    }


//    private Point mScreenPoint;
//    /**
//     * 展示社会单位
//     */
//    private void showCompanyMarker(int flag,int type) {
//        if ((flag & 0x01) != 0x01) {
//            return;
//        }
//        if (optionsArray.get(type).isEmpty()) {
//            UiUtils.showToast(App.getApplication(), "附近无该类型单位信息");
//            return;
//        }
//        for (int i = 0; i < CompanyMarkerOptions.size(); i++) {
//            if (isShow(CompanyMarkerOptions.get(i), mScreenPoint)) {//假如markeroption的位置在屏幕上则把它添加到map并且放入CompanyMarkers集合
//                boolean a = false;
//                for (Marker ma : CompanyMarkers) {//遍历companymarkers是否添加了该markeroption
//                    ma.setVisible(true);
//                    if (ma.getOptions().equals(CompanyMarkerOptions.get(i))) {
//                        a = true;
//                        break;
//                    }
//
//                }
//                if (!a) {//假如CompanyMarker里面还没有添加过,则添加上amap,并显示
//                    Marker mar = aMap.addMarker(CompanyMarkerOptions.get(i));
//                    CompanyMarkers.add(mar);
//                    mar.setVisible(true);
//                }
//
//            } else {
//
//            }
//        }
//    }
//

    /**
     * 获取政府station数据
     */
    public int  getFireAdminStationList(LatLng center, String distance) {
        RetrofitHelper.getApi().getFireadminstation(String.valueOf(center.longitude), String.valueOf(center.latitude),distance)
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
                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            LogUtil.i("获取政府station数据失败");
                            return;
                        }
                        if (list.status.equals("1")) {
                            ArrayList<MarkerOptions> optionses = new ArrayList<MarkerOptions>();
                            List<FireComponentGeneralInfo> lists = list.result;
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat != null || info.lng != null || !TextUtils.equals("", String.valueOf(info.lat)) || !TextUtils.equals("", String.valueOf(info.lng))) {
//                                    LogUtil.i("小型站" + info.toString());
                                    double lat = Double.valueOf(info.lat);
                                    double lng = Double.valueOf(info.lng);
                                    LatLng src = new LatLng(lat, lng);

                                    //title后面拼凑字符串 name + addr;snippet 内容为 id+ 单位类型(社会单位、消防室四种，用于详情activity决定使用哪个fragment)+
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(src)
                                            .title(info.name + "#" + info.addr)
                                            .snippet(info.id + "#" + FIREADMINSTATION)
                                            .icon(BitmapDescriptorFactory.fromBitmap(firestationBitmap));
                                    optionses.add(markerOptions);
                                }
                            }
                            listener.onFireAdminStationInfoGetFinish(optionses);
                        } else {
                            LogUtil.i("FIREADMINSTATION", list.msg);
                        }
                    }
                });
        return 0;
    }


    /**
     * 获取消防大队
     */
    public int  getFireBrigadeList(LatLng center, String distance) {
        RetrofitHelper.getApi().getFirebrigade(String.valueOf(center.longitude), String.valueOf(center.latitude),distance)
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
                    }

                    @Override
                    public void onNext(FireComponentGeneralInfoList list) {
                        if (list == null) {
                            UiUtils.showToast(App.getApplication(), "获取数据失败");
                            return;
                        }
                        if (list.status.equals("1")) {
                            ArrayList<MarkerOptions> optionses = new ArrayList<MarkerOptions>();
                            List<FireComponentGeneralInfo> lists = list.result;
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat != null || info.lng != null || !TextUtils.equals("", String.valueOf(info.lat)) || !TextUtils.equals("", String.valueOf(info.lng))) {
//                                    LogUtil.i("小型站" + info.toString());
                                    double lat = Double.valueOf(info.lat);
                                    double lng = Double.valueOf(info.lng);
                                    LatLng src = new LatLng(lat, lng);

                                    //title后面拼凑字符串 name + addr;snippet 内容为 id+ 单位类型(社会单位、消防室四种，用于详情activity决定使用哪个fragment)+
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(src)
                                            .title(info.name + "#" + info.addr)
                                            .snippet(info.id + "#" + FIREBRIGADE);
//                                            .icon(BitmapDescriptorFactory.fromBitmap(firestationBitmap));
                                    optionses.add(markerOptions);
                                }
                            }
                            listener.onFireBrigadeInfoGetFinish(optionses);
                        } else {
                            LogUtil.i("FIREBrigade", list.msg);
                        }
                    }
                });
        return 0;
    }

}
