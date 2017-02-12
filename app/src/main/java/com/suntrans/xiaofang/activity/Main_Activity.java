package com.suntrans.xiaofang.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.add.Add_activity;
import com.suntrans.xiaofang.activity.check.Check_Activity;
import com.suntrans.xiaofang.activity.others.CameraScan_Activity;
import com.suntrans.xiaofang.activity.others.Help_activity;
import com.suntrans.xiaofang.activity.others.InfoDetail_activity;
import com.suntrans.xiaofang.activity.others.Personal_activity;
import com.suntrans.xiaofang.activity.others.Search_activity;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.SensorEventHelper;
import com.suntrans.xiaofang.utils.UiUtils;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.suntrans.xiaofang.R.id.marker;

public class Main_Activity extends BasedActivity implements LocationSource, View.OnClickListener, AMap.OnMarkerClickListener, AMapLocationListener, MarkerHelper.onGetInfoFinishListener {
    @BindView(R.id.zhongdiandanwei)
    AppCompatCheckBox zhongdiandanwei;
    @BindView(R.id.yibandanwei)
    AppCompatCheckBox yibandanwei;
    @BindView(R.id.dadui)
    AppCompatCheckBox dadui;
    @BindView(R.id.zhongdui)
    AppCompatCheckBox zhongdui;
    @BindView(R.id.xiaoxingzhan)
    AppCompatCheckBox xiaoxingzhan;
    @BindView(R.id.xiangcun)
    AppCompatCheckBox xiangcun;
    @BindView(R.id.xiaofangshi)
    AppCompatCheckBox xiaofangshi;

    @BindView(R.id.xingzhenshenpi)
    AppCompatCheckBox xingzhenshenpi;


    @BindView(R.id.ll_more_danwei)
    LinearLayout llMoreDanwei;
    @BindView(R.id.bottom_menu)
    RelativeLayout bottomMenu;

    private Toolbar toolbar;
    private MapView mapView = null;
    private LinearLayout rootview;
    private AMap aMap;
    private UiSettings mUiSettings;//高德地图设置类
    public AMapLocationClient mLocationClient = null; //声明AMapLocationClient类对象
    //声明定位回调监听器
//    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private OnLocationChangedListener mListener;//定位位置改变近挺累
    private SensorEventHelper mSensorHelper;//手机传感器帮助类
    private Marker mLocMarker;
    private Circle mCircle;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private CardView cardView;//搜索栏
    //底部菜单栏
    private TextView name;//单位名字
    private TextView addrDes;//单位地址描述
    //    private LinearLayout bottom1;//默认底部菜单
    private LinearLayout bottom2;//单位信息底部菜单栏
    private LinearLayout detail;//单位详情


    //    private Spinner spinner;
//    LinearLayout llNearby;//底部菜单附近单位按钮
//    LinearLayout llAdd;//底部菜单附近添加单位按钮
//    LinearLayout llResume;//底部菜单个人中心按钮
    private Point mScreenPoint;//我的屏幕点的x和y最大值

//    private TextView textView;

    SparseArray<ArrayList<Marker>> markersArray = new SparseArray<>();//所有marker集合的数组

    private ArrayList<MarkerOptions> companyOptions = null;
    private ArrayList<MarkerOptions> fireRoomOptions = null;
    private ArrayList<MarkerOptions> fireStationoptions = null;//乡村小型站
    private ArrayList<MarkerOptions> fireGroupOptions = null;
    private ArrayList<MarkerOptions> fireLicenseoptions = null;//政府小型站
    private ArrayList<MarkerOptions> fireAdminStationoptions = null;//政府小型站
    private ArrayList<MarkerOptions> fireBrigadeoptions = null;//政府小型站

    ArrayList<Marker> CompanyMarkers = null;
    ArrayList<Marker> fireRoomMarkers = null;
    ArrayList<Marker> fireStationMarkers = null;
    ArrayList<Marker> fireGroupMarkers = null;
    ArrayList<Marker> fireLicenseMarkers = null;
    ArrayList<Marker> fireAdminStationMarkers = null;
    ArrayList<Marker> fireBrigadeMarkers = null;

    private GeocodeSearch geocodeSearch;//地点搜索
    private RegeocodeQuery query;
    private LocationManager manager;

    private MarkerHelper helper;


    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        Bugly.init(getApplicationContext(), "7d01f61d8c", false);
        initView();
        initMap(savedInstanceState);
    }

    private void initView() {

        companyOptions = new ArrayList<>();
        fireRoomOptions = new ArrayList<>();
        fireStationoptions = new ArrayList<>();
        fireGroupOptions = new ArrayList<>();
        fireLicenseoptions = new ArrayList<>();
        fireAdminStationoptions = new ArrayList<>();
        fireBrigadeoptions = new ArrayList<>();


        CompanyMarkers = new ArrayList<>();
        fireRoomMarkers = new ArrayList<>();
        fireStationMarkers = new ArrayList<>();
        fireGroupMarkers = new ArrayList<>();
        fireLicenseMarkers = new ArrayList<>();
        fireAdminStationMarkers = new ArrayList<>();
        fireBrigadeMarkers = new ArrayList<>();

//        markersArray.put(0, CompanyMarkers);
//        markersArray.put(1, fireRoomMarkers);
//        markersArray.put(2, fireStationMarkers);
//        markersArray.put(3, fireGroupMarkers);

//        /**
//         *模拟武汉大学地址，release时删除
//         */
//        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        a();
//        new Thread(new RunnableMockLocation()).start();


        helper = new MarkerHelper(this);
        helper.setOnGetInfoFinishListener(this);


        zhongdiandanwei.setSupportButtonTintList(ColorStateList.valueOf(Color.WHITE));
        yibandanwei.setSupportButtonTintList(ColorStateList.valueOf(Color.WHITE));
        dadui.setSupportButtonTintList(ColorStateList.valueOf(Color.WHITE));
        zhongdui.setSupportButtonTintList(ColorStateList.valueOf(Color.WHITE));
        xiaoxingzhan.setSupportButtonTintList(ColorStateList.valueOf(Color.WHITE));
        xiangcun.setSupportButtonTintList(ColorStateList.valueOf(Color.WHITE));
        xiaofangshi.setSupportButtonTintList(ColorStateList.valueOf(Color.WHITE));
        xingzhenshenpi.setSupportButtonTintList(ColorStateList.valueOf(Color.WHITE));

        zhongdiandanwei.setOnCheckedChangeListener(listener);
        yibandanwei.setOnCheckedChangeListener(listener);
        dadui.setOnCheckedChangeListener(listener);
        zhongdui.setOnCheckedChangeListener(listener);
        xiaoxingzhan.setOnCheckedChangeListener(listener);
        xiangcun.setOnCheckedChangeListener(listener);
        xiaofangshi.setOnCheckedChangeListener(listener);
        xingzhenshenpi.setOnCheckedChangeListener(listener);

        textView = (TextView) findViewById(R.id.tx_count);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("武汉消防基础信息采集平台");
        toolbar.setTitleTextAppearance(this, R.style.toolbar);
        cardView = (CardView) findViewById(R.id.search_cardview);
        cardView.setOnClickListener(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
        }
//        llNearby = (LinearLayout) findViewById(R.id.ll_nearby);
//        llAdd = (LinearLayout) findViewById(R.id.ll_add);
//        llResume = (LinearLayout) findViewById(R.id.ll_gerenzhongxin);

//        bottom1 = (LinearLayout) findViewById(R.id.ll_bottom1);
        bottom2 = (LinearLayout) findViewById(R.id.ll_bottom2);
        name = (TextView) findViewById(R.id.text_company_name);
        addrDes = (TextView) findViewById(R.id.text_company_addr);
        detail = (LinearLayout) findViewById(R.id.ll_detail);

//        llNearby.setOnClickListener(this);
//        llAdd.setOnClickListener(this);
//        llResume.setOnClickListener(this);
        detail.setOnClickListener(this);

        mScreenPoint = new Point();
        WindowManager manager = this.getWindowManager();
        manager.getDefaultDisplay().getSize(mScreenPoint);

    }

    LatLng currentCenterLocation;
    float currentZoom = 16;
    float currentDistance;

    //初始化map
    private void initMap(Bundle savedInstanceState) {

        mapView = (MapView) findViewById(R.id.mapview);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            aMap.setLocationSource(this);// 设置定位资源。如果不设置此定位资源则定位按钮不可点击。并且实现activate激活定位,停止定位的回调方法
//            aMap.setMyLocationEnabled(true);// 可触发定位并显示定位层
            mUiSettings.setScaleControlsEnabled(true);//显示比例尺控件
//            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//            mUiSettings.setCompassEnabled(true);
//            mUiSettings.setMyLocationButtonEnabled(true);
            mUiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
            mUiSettings.setRotateGesturesEnabled(false);//设置地图是否可以旋转
//            mUiSettings.setLogoBottomMargin(UiUtils.dip2px(0));
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                    if (cameraPosition.zoom < 15) {
                    for (Marker marker :
                            aMap.getMapScreenMarkers()) {
                        if (!marker.equals(mLocMarker)) {
                            marker.remove();
                        }

                    }
//                    }
                    LogUtil.i("onCameraChangeFinish:" + cameraPosition.zoom + cameraPosition.target.toString());

                    if (mMyLocation != null) {
                        if (currentCenterLocation != null) {
//                            if (currentCenterLocation.equals(cameraPosition.target)){
//                                if (cameraPosition.zoom<=currentZoom){
//                                    float dis = aMap.getScalePerPixel() * mScreenPoint.x / 1000/2;
//                                    helper.getCompanyInfo(cameraPosition.target, String.valueOf(dis));
//                                    helper.getFireRoomList(cameraPosition.target, String.valueOf(dis));
//                                    currentCenterLocation = cameraPosition.target;
//                                }
//                            }else {
//
//                            }
                            float dis = aMap.getScalePerPixel() * mScreenPoint.x / 1000;
                            helper.getCompanyInfo(cameraPosition.target, String.valueOf(dis));
                            helper.getFireRoomList(cameraPosition.target, String.valueOf(dis));
                            helper.getFirStationList(cameraPosition.target, String.valueOf(dis));
                            helper.getFireGroupList(cameraPosition.target, String.valueOf(dis));
                            helper.getLicenseList(cameraPosition.target, String.valueOf(dis));
                            helper.getFireAdminStationList(cameraPosition.target, String.valueOf(dis));
                            helper.getFireBrigadeList(cameraPosition.target, String.valueOf(dis));
                            currentCenterLocation = cameraPosition.target;
                        }
                    }

                    currentZoom = cameraPosition.zoom;

                }
            });
            aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
//                    LogUtil.i("点击的坐标为:" + latLng.latitude + "," + latLng.longitude);
                    LogUtil.i("map被点击了");
                    if (bottom2.getVisibility()==View.VISIBLE)
                    bottom2.setVisibility(View.GONE);
                    if (llMoreDanwei.getVisibility()==View.VISIBLE){

                    llMoreDanwei.setVisibility(View.GONE);
                        isShowDanweiMenu = false;
                    }
                    mUiSettings.setLogoBottomMargin(UiUtils.dip2px(5));

                }
            });
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    aMap.showBuildings(true);
                }
            });
        }
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
        aMap.setOnMarkerClickListener(this);//设置marker点击监听

        geocodeSearch = new GeocodeSearch(getApplicationContext());


    }


    /**
     * 隐藏单位marker
     *
     * @param currentShowingType         现在展示在地图上的单位类型(即要隐藏的单位)
     * @param targetType//spinner点击选择的类型
     */
    private void hideMarkers(int currentShowingType, int targetType) {

        for (Marker marker : markersArray.get(currentShowingType)) {
            marker.setVisible(false);
        }

    }


    private boolean mFirstFix = false;//是否第一次修展示我的位置图标

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                LogUtil.i("我当前的坐标为:(" + amapLocation.getLatitude() + "," + amapLocation.getLongitude() + ")");
                LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
//                Intent intent = new Intent();
//                intent.setAction("com.suntrans.addr.RECEIVE");
//                intent.putExtra("myLocation",location);
//                intent.putExtra("addrdes",amapLocation.getAddress());
//                sendBroadcast(intent);
//                SharedPreferences.Editor editor = App.getSharedPreferences().edit();
//                editor.putString("lng", location.longitude + "");
//                editor.putString("lat", location.latitude + "");
//                editor.putString("addr", amapLocation.getAddress() + "");
//                editor.commit();

                if (!mFirstFix) {
//                    mMyLocation_pre = mMyLocation;
                    mMyLocation = null;
                    mMyLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    currentCenterLocation = mMyLocation;

                    mFirstFix = true;
                    addCircle(location, amapLocation.getAccuracy());//添加定位精度圆
                    addMarker(location, amapLocation.getAddress());//添加定位图标
                    if (mapTypeState == 0 || mapTypeState == 1)
                        mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                    if (mMyLocation != null) {
//                        LogUtil.e(aMap.getScalePerPixel()+"");
//                        LogUtil.e(mScreenPoint.x+"");
//                        float dis = aMap.getScalePerPixel() * mScreenPoint.x / 1000;
//                        String distance = String.valueOf(dis);
//                        LogUtil.i("屏幕范围" + distance);
//
//                        helper.getCompanyInfo(mMyLocation, distance);
//                        helper.getFireRoomList(mMyLocation, distance);
//                        getCompanyInfo(mMyLocation,distance);
//                        getFireRoomList(mMyLocation);
//                        getFirStationList(mMyLocation);
//                        getFireGroupList(mMyLocation);
//                        getLicenseList(mMyLocation);
                    }
                } else {
                    mCircle.setCenter(location);
                    mCircle.setRadius(amapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
                    currentCenterLocation = mMyLocation;
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(location));
                }


            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                UiUtils.showToast(App.getApplication(), "定位失败,请确认打开GPS/WIFI和网络连接");
            }


        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.search_cardview:
//                Intent intent = new Intent();
//                intent.setClass(Main_Activity.this, Search_activity.class);
//                if (mLocMarker != null) {
//                    intent.putExtra("from", mLocMarker.getPosition());
//                }
//                intent.putExtra("type", showingType);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
//            case R.id.ll_add:
//                startActivity(new Intent(this, Add_detail_activity.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
//            case R.id.ll_nearby:
//                break;
            case R.id.ll_detail:
                if (currentMarker.equals(mLocMarker)) {
                    UiUtils.showToast(App.getApplication(), mLocMarker.getTitle());
                    return;
                }
                if (currentMarker != null) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("name", currentMarker.getTitle());
                    intent1.putExtra("from", mLocMarker.getPosition());
//                    intent1.putExtra("to", currentMarker.getPosition());
                    intent1.putExtra("companyID", currentMarker.getSnippet());
                    intent1.setClass(Main_Activity.this, InfoDetail_activity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
//            case R.id.ll_gerenzhongxin:
//                startActivity(new Intent(this, Personal_activity.class));
//                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                break;
        }
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            LogUtil.i("开始定位");
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            mLocationOption = new AMapLocationClientOption();
//      //设置定位模式为AMapLocationMode.Hight_Accuracy，默认为高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setInterval(10000);
            //设置允许模拟定位
            mLocationOption.setMockEnable(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(30000);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        }
    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            LogUtil.i("开始定位");
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;

    }

    Handler handler = new Handler();
    String addressName;//当前点击的marker的地址
    float distance;//当前点击的marker与我的距离
    Marker currentMarker;//我当前位置的marker

    @Override
    public boolean onMarkerClick(Marker marker) {
        mUiSettings.setLogoBottomMargin(UiUtils.dip2px(100));
        currentMarker = null;
        System.out.println("marker被点击了哦");
        currentMarker = marker;
        bottom2.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menushow);
        bottom2.startAnimation(animation);
        name.setText(currentMarker.getTitle().split("#")[0]);
        distance = AMapUtils.calculateLineDistance(mMyLocation, currentMarker.getPosition());
        DecimalFormat df = new DecimalFormat("######0.");
        distance = Float.valueOf(df.format(distance));

        if (currentMarker.getTitle().split("#")[1].equals("null")) {
            query = new RegeocodeQuery(new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude), 200, GeocodeSearch.AMAP);
            geocodeSearch.getFromLocationAsyn(query);
            geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                    if (rCode == 1000) {
                        if (result != null && result.getRegeocodeAddress() != null
                                && result.getRegeocodeAddress().getFormatAddress() != null) {
                            addressName = result.getRegeocodeAddress().getFormatAddress();
                            if (addrDes != null) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        String dist;
                                        if (distance > 1000) {
                                            dist = distance / 1000 + "千米";
                                        } else {
                                            dist = distance + "米";
                                        }
                                        addrDes.setText("距离您" + dist + "| " + addressName);
                                    }
                                }, 500);
                            }
                        } else {

                        }
                    } else {
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
        } else {
            addressName = currentMarker.getTitle().split("#")[1];
            String dis;
            if (distance > 1000) {
                dis = distance / 1000 + "千米";
            } else {
                dis = distance + "米";
            }
            addrDes.setText("距离您" + dis + "| " + addressName);
        }

        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = "";
        if (resultCode == 300) {
            result = data.getStringExtra("result");
            if (result != null && !result.equals("")) {
                if (result.contains("http://xf.91yunpan.com/weixin/company/show/")) {
                    String id = result.split("company/show/")[1];
                    Intent intent = new Intent();
                    intent.putExtra("name", "");
                    intent.putExtra("from", mMyLocation);
                    intent.putExtra("companyID", id + "#" + MarkerHelper.S0CIETY);
                    intent.setClass(Main_Activity.this, InfoDetail_activity.class);
                    startActivity(intent);
                } else {
                    UiUtils.showToast("无效的二维码");
                }
                Intent intent = new Intent();
                intent.putExtra("id", result);
                intent.setClass(this, InfoDetail_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

        } else {
            IntentResult result1 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result1 != null) {
                if (result1.getContents() == null) {
//                    UiUtils.showToast("无效的二维码");
                } else {
//                    LogUtil.i("扫描结果为:" + result1.getContents());
//                    UiUtils.showToast(App.getApplication(), result1.getContents());
                    if (result1.getContents().contains("http://xf.91yunpan.com/weixin/company/show/")) {
                        String id = result1.getContents().split("company/show/")[1];
                        Intent intent1 = new Intent();
                        intent1.putExtra("name", "");
                        intent1.putExtra("from", mMyLocation);
//                        intent1.putExtra("to", currentMarker.getPosition());
                        intent1.putExtra("companyID", id + "#" + MarkerHelper.S0CIETY);
//                        .snippet(info.id + "#" + S0CIETY + "#" + "1")
                        intent1.setClass(Main_Activity.this, InfoDetail_activity.class);
                        startActivity(intent1);
                    } else {
                        UiUtils.showToast("无效的二维码");
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    private LatLng mMyLocation;//我当前的坐标

    //    private LatLng mMyLocation_pre;//上次的坐标
    private void addCircle(LatLng latlng, double radius) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);
    }

    private void addMarker(LatLng latlng, String addr) {
        if (mLocMarker != null) {
            return;
        }
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(App.getApplication().getResources(),
                R.drawable.navi_map_gps_locked)));
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        String title = addr == null ? "null" : addr;
        mLocMarker.setTitle(LOCATION_MARKER_FLAG + "#" + title);
        mLocMarker.setSnippet(addr);
    }

    public static final String LOCATION_MARKER_FLAG = "我的位置";


    private long[] mHits = new long[2];

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (bottom2.getVisibility() == View.VISIBLE) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menuhide);
                bottom2.setVisibility(View.GONE);
                bottom2.startAnimation(animation);
                return true;
            }
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
//                finish();
                Process.killProcess(Process.myPid());
            } else {
                Snackbar.make(mapView, "再按一次退出", Snackbar.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        setIconEnable(menu,true);  //  就是这一句使图标能显示
        return super.onCreateOptionsMenu(menu);
    }

    private void setIconEnable(Menu menu, boolean enable) {
        try {
            Class<?> clazz = Class.forName("android.support.v7.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);

            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    boolean isShowDanweiMenu = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.per:
                startActivity(new Intent(this, Personal_activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                break;
            case R.id.scan1:
                new IntentIntegrator(this)
                        .setOrientationLocked(false)
                        .setBeepEnabled(false)
                        .setCaptureActivity(CameraScan_Activity.class) // 设置自定义的activity是CustomActivity
                        .initiateScan(); // 初始化扫描
                break;
//            case R.id.pulldata:
//                startActivity(new Intent(this, PullData_activity.class));
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                break;
            case R.id.help:
                startActivity(new Intent(this, Help_activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.add:
                Intent intent = new Intent(this, Add_activity.class);
                if (mMyLocation != null) {
                    intent.putExtra("location", mMyLocation);
                }
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.check:
                startActivity(new Intent(this, Check_Activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.search:
                Intent intent1 = new Intent();
                intent1.setClass(Main_Activity.this, Search_activity.class);
                if (mLocMarker != null) {
                    intent1.putExtra("from", mLocMarker.getPosition());
                }
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.danwei:
                if (isShowDanweiMenu) {
                    llMoreDanwei.setVisibility(View.GONE);
                    isShowDanweiMenu = false;
                } else {
                    llMoreDanwei.setVisibility(View.VISIBLE);
                    isShowDanweiMenu = true;

                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //重写此方法解决icon不显示问题
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        if (menu != null) {
//            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
//                try {
//                    Method m = menu.getClass().getDeclaredMethod(
//                            "setOptionalIconsVisible", Boolean.TYPE);
//                    m.setAccessible(true);
//                    m.invoke(menu, true);
//                } catch (NoSuchMethodException e) {
//                } catch (Exception e) {
//                }
//            }
//        }
//        return super.onPrepareOptionsMenu(menu);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        isrun = false;
    }

    @Override
    protected void onDestroy() {
//        PgyUpdateManager.unregister();/
        handler.removeCallbacksAndMessages(null);
        mapView.onDestroy();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        deactivate();
        mFirstFix = false;
        super.onDestroy();
    }


    /**
     * 左下角button改变视图的按钮
     */
    private int mapTypeState = 0;//0没移动 1移动
    private int i = 0;

    public void changedViewType(View view) {
        if (mapTypeState == 1) {
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(mMyLocation));
            mapTypeState = 0;
        } else {
            i++;
            if (i % 2 == 0) {
                mUiSettings.setCompassEnabled(false);
                mUiSettings.setRotateGesturesEnabled(false);//设置地图是否可以旋转
                CameraUpdate update2 = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        mMyLocation,
                        16, //新的缩放级别
                        0, //俯仰角0°~45°（垂直与地图时为0）
                        0  ////偏航角 0~360° (正北方为0)
                ));
                aMap.animateCamera(update2);
            } else {
                mUiSettings.setCompassEnabled(true);
                mUiSettings.setRotateGesturesEnabled(true);//设置地图是否可以旋转

                CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        mMyLocation,
                        20, //新的缩放级别
                        30, //俯仰角0°~45°（垂直与地图时为0）
                        0  ////偏航角 0~360° (正北方为0)
                ));
                aMap.animateCamera(update);
            }
        }
    }


    //根据marker是否在屏幕上展示marker
    public boolean isVisible(MarkerOptions marker, Point screen) {
        Point target = aMap.getProjection().toScreenLocation(marker.getPosition());
        if (target.x < 0 || target.y < 0 || target.x > screen.x || target.y > screen.y) {
            return false;
        }
        return true;
    }


    int flag = 0x01;//显示哪中类型单位标记
    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.zhongdiandanwei:
                    if (isChecked) {
                        flag = flag | 0x01;
                        if (CompanyMarkers.size() == 0) {
                            for (MarkerOptions marker :
                                    companyOptions) {
                                CompanyMarkers.add(aMap.addMarker(marker));
                            }
                        } else {
                            for (Marker marker :
                                    CompanyMarkers) {
                                marker.setVisible(true);
                            }
                        }
//                        showCompanyMarker();
                    } else {
                        for (Marker marker :
                                CompanyMarkers) {
                            marker.setVisible(false);
                        }
//                        hideMarkers(0, 0);
                        flag = flag & 0xfe;
                    }
                    break;

                case R.id.xiaofangshi:
                    if (isChecked) {
                        flag = flag | 0x02;
                        if (fireRoomMarkers.size() == 0) {
                            for (MarkerOptions marker :
                                    fireRoomOptions) {
                                fireRoomMarkers.add(aMap.addMarker(marker));
                            }
                        } else {
                            for (Marker marker :
                                    fireRoomMarkers) {
                                marker.setVisible(true);
                            }
                        }

                    } else {
                        flag = flag & 0xfd;
                        for (Marker marker :
                                fireRoomMarkers) {
                            marker.setVisible(false);
                        }
                    }
                    break;

//                case R.id.yibandanwei:
//
//                    break;
                case R.id.dadui:
                    if (isChecked) {
                        flag = flag | 0x04;
                        if (fireBrigadeMarkers.size() == 0) {
                            for (MarkerOptions marker :
                                    fireBrigadeoptions) {
                                fireBrigadeMarkers.add(aMap.addMarker(marker));
                            }
                        } else {
                            for (Marker marker :
                                    fireBrigadeMarkers) {
                                marker.setVisible(true);
                            }
                        }
                    } else {
                        flag = flag & 0xfb;
                        for (Marker marker :
                                fireBrigadeMarkers) {
                            marker.setVisible(false);
                        }
                    }
                    break;
                case R.id.zhongdui:
                    if (isChecked) {
                        flag = flag | 0x08;
                        if (fireGroupMarkers.size() == 0) {
                            for (MarkerOptions marker :
                                    fireGroupOptions) {
                                fireGroupMarkers.add(aMap.addMarker(marker));
                            }
                        } else {
                            for (Marker marker :
                                    fireGroupMarkers) {
                                marker.setVisible(true);
                            }
                        }

                    } else {
                        flag = flag & 0xf7;
                        for (Marker marker :
                                fireGroupMarkers) {
                            marker.setVisible(false);
                        }
                    }

                    break;
                case R.id.xiaoxingzhan:
                    if (isChecked) {
                        flag = flag | 0x40;
                        if (fireAdminStationMarkers.size() == 0) {
                            for (MarkerOptions marker :
                                    fireAdminStationoptions) {
                                fireAdminStationMarkers.add(aMap.addMarker(marker));
                            }
                        } else {
                            for (Marker marker :
                                    fireAdminStationMarkers) {
                                marker.setVisible(true);
                            }
                        }
                    } else {
                        flag = flag & 0xbf;
                        for (Marker marker :
                                fireAdminStationMarkers) {
                            marker.setVisible(false);
                        }

                    }
                    break;
                case R.id.xiangcun:
                    if (isChecked) {
                        flag = flag | 0x10;
                        if (fireStationMarkers.size() == 0) {
                            for (MarkerOptions marker :
                                    fireStationoptions) {
                                fireStationMarkers.add(aMap.addMarker(marker));
                            }
                        } else {
                            for (Marker marker :
                                    fireStationMarkers) {
                                marker.setVisible(true);
                            }
                        }
                    } else {
                        flag = flag & 0xef;
                        for (Marker marker :
                                fireStationMarkers) {
                            marker.setVisible(false);
                        }

                    }
                    break;

                case R.id.xingzhenshenpi:
                    if (isChecked) {
                        flag = flag | 0x20;
                        if (fireLicenseMarkers.size() == 0) {
                            for (MarkerOptions marker :
                                    fireLicenseoptions) {
                                fireLicenseMarkers.add(aMap.addMarker(marker));
                            }
                        } else {
                            for (Marker marker :
                                    fireLicenseMarkers) {
                                marker.setVisible(true);
                            }
                        }
                    } else {
                        flag = flag & 0xdf;
                        for (Marker marker :
                                fireLicenseMarkers) {
                            marker.setVisible(false);
                        }

                    }
                    break;

            }
        }
    };


    boolean isrun = true;


    @Override
    public void onCompanyInfoGetFinish(ArrayList<MarkerOptions> optionses) {
        if (currentZoom >= 15) {
            if ((flag & 0x01) != 0x01) {
                companyOptions.clear();
                CompanyMarkers.clear();

                companyOptions.addAll(optionses);
            } else {
//                ArrayList<MarkerOptions> copy = new ArrayList<>(companyOptions);
                companyOptions.clear();
                CompanyMarkers.clear();

                companyOptions.addAll(optionses);
                CompanyMarkers.addAll(aMap.addMarkers(optionses, false));
//                LogUtil.e("当前marker的数量为:" + aMap.getMapScreenMarkers().size());
            }

        } else {
//            LogUtil.i("重点单位:" + optionses.size() + "个");
//            textView.setText("重点单位:" + optionses.size() + "个");
        }
    }

    @Override
    public void onFireRoomInfoGetFinish(ArrayList<MarkerOptions> optionses) {
        if (currentZoom >= 15) {
            if ((flag & 0x02) != 0x02) {
                fireRoomOptions.clear();
                fireRoomMarkers.clear();
                fireRoomOptions.addAll(optionses);
            } else {
                fireRoomOptions.clear();
                fireRoomOptions.addAll(optionses);

                fireRoomMarkers.clear();
                fireRoomMarkers.addAll(aMap.addMarkers(optionses, false));
            }

        } else {
//            textView.setVisibility(View.VISIBLE);
            LogUtil.i("消防室:" + optionses.size() + "个");

//            textView.setText("消防室:" + optionses.size() + "个");
        }

    }

    @Override
    public void onFireStationInfoGetFinish(ArrayList<MarkerOptions> optionses) {
        if (currentZoom >= 15) {
            if ((flag & 0x10) != 0x10) {
                fireStationoptions.clear();
                fireStationMarkers.clear();

                fireStationoptions.addAll(optionses);
            } else {
                fireStationoptions.clear();
                fireStationoptions.addAll(optionses);

                fireStationMarkers.clear();
                fireStationMarkers.addAll(aMap.addMarkers(optionses, false));
            }

        } else {
//            textView.setVisibility(View.VISIBLE);
            LogUtil.i("小型站:" + optionses.size() + "个");
//            textView.setText("消防室:" + optionses.size() + "个");
        }
    }

    @Override
    public void onFireGroupInfoGetFinish(ArrayList<MarkerOptions> optionses) {
        LogUtil.i("消防中队个数"+optionses.size());
        if (currentZoom >= 15) {
            if ((flag & 0x08) != 0x08) {
                fireGroupOptions.clear();
                fireGroupMarkers.clear();

                fireGroupOptions.addAll(optionses);
            } else {
                fireGroupOptions.clear();
                fireGroupOptions.addAll(optionses);

                fireGroupMarkers.clear();
                fireGroupMarkers.addAll(aMap.addMarkers(optionses, false));
            }

        } else {
            LogUtil.i("中队:" + optionses.size() + "个");
        }
    }

    @Override
    public void onLicenseInfoGetFinish(ArrayList<MarkerOptions> optionses) {
        LogUtil.i("行政审批个数"+optionses.size());
        if (currentZoom >= 15) {
            if ((flag & 0x20) != 0x20) {
                fireLicenseoptions.clear();
                fireLicenseMarkers.clear();

                fireLicenseoptions.addAll(optionses);
            } else {
                fireLicenseoptions.clear();
                fireLicenseoptions.addAll(optionses);

                fireLicenseMarkers.clear();
                fireLicenseMarkers.addAll(aMap.addMarkers(optionses, false));
            }

        } else {
            LogUtil.i("行政审批:" + optionses.size() + "个");
        }
    }

    @Override
    public void onFireAdminStationInfoGetFinish(ArrayList<MarkerOptions> optionses) {
        LogUtil.i("政府小型站个数"+optionses.size());
        if (currentZoom >= 15) {
            if ((flag & 0x40) != 0x40) {
                fireAdminStationoptions.clear();
                fireAdminStationMarkers.clear();

                fireAdminStationoptions.addAll(optionses);
            } else {
                fireAdminStationoptions.clear();
                fireAdminStationoptions.addAll(optionses);

                fireAdminStationMarkers.clear();
                fireAdminStationMarkers.addAll(aMap.addMarkers(optionses, false));
            }

        } else {
            LogUtil.i("政府小型站个数:" + optionses.size() + "个");
        }
    }

    @Override
    public void onFireBrigadeInfoGetFinish(ArrayList<MarkerOptions> optionses) {
        LogUtil.i("消防大队个数:"+optionses.size());
        if (currentZoom >= 15) {
            if ((flag & 0x04) != 0x04) {
                fireBrigadeoptions.clear();
                fireBrigadeMarkers.clear();

                fireBrigadeoptions.addAll(optionses);
            } else {
                fireBrigadeoptions.clear();
                fireBrigadeoptions.addAll(optionses);

                fireBrigadeMarkers.clear();
                fireBrigadeMarkers.addAll(aMap.addMarkers(optionses, false));
            }

        } else {
            LogUtil.i("消防大队个数:" + optionses.size() + "个");
        }
    }

    private class RunnableMockLocation implements Runnable {

        @Override
        public void run() {
            while (isrun) {
                try {
                    Thread.sleep(1000);
//                    System.out.println("正在模拟位置");
                    if (hasAddTestProvider == false) {
                        continue;
                    }
                    try {
                        // 模拟位置（addTestProvider成功的前提下）
                        String providerStr = LocationManager.GPS_PROVIDER;
                        Location mockLocation = new Location(providerStr);

                        mockLocation.setLatitude(30.542605);   // 维度（度）
                        mockLocation.setLongitude(114.358693);  // 经度（度）
                        mockLocation.setAltitude(30);    // 高程（米）
                        mockLocation.setBearing(180);   // 方向（度）
//                        mockLocation.setSpeed(10);    //速度（米/秒）

                        mockLocation.setAccuracy(10f);   // 精度（米）
                        mockLocation.setTime(new Date().getTime());   // 本地时间
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                        }
                        manager.setTestProviderLocation(providerStr, mockLocation);
                    } catch (Exception e) {
                        // 防止用户在软件运行过程中关闭模拟位置或选择其他应用
                        stopMockLocation();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    boolean hasAddTestProvider = false;

    private void a() {

        boolean canMockPosition = (Settings.Secure.getInt(getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0)
                || Build.VERSION.SDK_INT > 22;
        if (canMockPosition && hasAddTestProvider == false) {
            try {
                String providerStr = LocationManager.GPS_PROVIDER;
                LocationProvider provider = manager.getProvider(providerStr);
                if (provider != null) {
                    manager.addTestProvider(
                            provider.getName()
                            , provider.requiresNetwork()
                            , provider.requiresSatellite()
                            , provider.requiresCell()
                            , provider.hasMonetaryCost()
                            , provider.supportsAltitude()
                            , provider.supportsSpeed()
                            , provider.supportsBearing()
                            , provider.getPowerRequirement()
                            , provider.getAccuracy());
                } else {
                    manager.addTestProvider(
                            providerStr
                            , true, true, false, false, true, true, true
                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                }
                manager.setTestProviderEnabled(providerStr, true);
                manager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis());

                // 模拟位置可用
                hasAddTestProvider = true;
                canMockPosition = true;
            } catch (SecurityException e) {
                canMockPosition = false;
            }
        }
    }

    /**
     * 停止模拟位置，以免启用模拟数据后无法还原使用系统位置
     * 若模拟位置未开启，则removeTestProvider将会抛出异常；
     * 若已addTestProvider后，关闭模拟位置，未removeTestProvider将导致系统GPS无数据更新；
     */
    public void stopMockLocation() {
        if (hasAddTestProvider) {
            try {
                manager.removeTestProvider(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                // 若未成功addTestProvider，或者系统模拟位置已关闭则必然会出错
            }
            hasAddTestProvider = false;
        }
    }


}
