package com.suntrans.xiaofang.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.lidroid.xutils.util.LogUtils;
import com.suntrans.xiaofang.BaseApplication;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.add.Add_activity;
import com.suntrans.xiaofang.activity.add.Add_detail_activity;
import com.suntrans.xiaofang.activity.check.Check_Activity;
import com.suntrans.xiaofang.activity.others.CameraScan_Activity;
import com.suntrans.xiaofang.activity.others.Help_activity;
import com.suntrans.xiaofang.activity.others.InfoDetail_activity;
import com.suntrans.xiaofang.activity.others.Personal_activity;
import com.suntrans.xiaofang.activity.others.Search_activity;
import com.suntrans.xiaofang.model.company.CompanyList;
import com.suntrans.xiaofang.model.company.CompanyListResult;
import com.suntrans.xiaofang.model.fireroom.FireComponentGeneralInfo;
import com.suntrans.xiaofang.model.fireroom.FireComponentGeneralInfoList;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.SensorEventHelper;
import com.suntrans.xiaofang.utils.UiUtils;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Main_Activity extends AppCompatActivity implements LocationSource, View.OnClickListener, AMap.OnMarkerClickListener, AMapLocationListener {
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
    private LinearLayout bottom1;//默认底部菜单
    private LinearLayout bottom2;//单位信息底部菜单栏
    private LinearLayout detail;//单位详情


    private Spinner spinner;
    LinearLayout llNearby;//底部菜单附近单位按钮
    LinearLayout llAdd;//底部菜单附近添加单位按钮
    LinearLayout llResume;//底部菜单个人中心按钮
    private Point mScreenPoint;//我的屏幕点的x和y最大值

    ArrayList<Marker> CompanyMarkers;
    ArrayList<MarkerOptions> CompanyMarkerOptions;

    ArrayList<Marker> fireRoomMarkers;
    ArrayList<MarkerOptions> fireRoomOptions;

    ArrayList<Marker> fireStationMarkers;
    ArrayList<MarkerOptions> fireStationOptions;

    ArrayList<Marker> fireGroupMarkers;
    ArrayList<MarkerOptions> fireGroupOptions;


    Bitmap fireroomBitmap;
    Bitmap firestationBitmap;
    Bitmap companyBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initMap(savedInstanceState);
    }

    private void initView() {

        fireRoomMarkers = new ArrayList<>();
        fireRoomOptions = new ArrayList<>();

        CompanyMarkerOptions = new ArrayList<>();
        CompanyMarkers = new ArrayList<>();

        fireStationMarkers = new ArrayList<>();
        fireStationOptions = new ArrayList<>();

        fireGroupMarkers = new ArrayList<>();
        fireGroupOptions = new ArrayList<>();

        markersArray.put(0,CompanyMarkers);
        markersArray.put(1,fireRoomMarkers);
        markersArray.put(2,fireStationMarkers);
        markersArray.put(3,fireGroupMarkers);

        fireroomBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fireroom_marker);
        firestationBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.firestation_marker);
        companyBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.company);

//        rootview = (LinearLayout) findViewById(R.id.rootview);
        spinner = (Spinner) findViewById(R.id.spinner);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("单位总览");
        toolbar.setLogo(R.mipmap.ic_launcher);
        cardView = (CardView) findViewById(R.id.search_cardview);
        cardView.setOnClickListener(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        llNearby = (LinearLayout) findViewById(R.id.ll_nearby);
        llAdd = (LinearLayout) findViewById(R.id.ll_add);
        llResume = (LinearLayout) findViewById(R.id.ll_gerenzhongxin);

        bottom1 = (LinearLayout) findViewById(R.id.ll_bottom1);
        bottom2 = (LinearLayout) findViewById(R.id.ll_bottom2);
        name = (TextView) findViewById(R.id.text_company_name);
        addrDes = (TextView) findViewById(R.id.text_company_addr);
        detail = (LinearLayout) findViewById(R.id.ll_detail);

        llNearby.setOnClickListener(this);
        llAdd.setOnClickListener(this);
        llResume.setOnClickListener(this);
        detail.setOnClickListener(this);

        mScreenPoint = new Point();
        WindowManager manager = this.getWindowManager();
        manager.getDefaultDisplay().getSize(mScreenPoint);

    }
    boolean a =true;
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
                    LogUtils.i("onCameraChangeFinish:" + cameraPosition.zoom);
                    if (mMyLocation != null){
                        switch (showingType){
                            case 0:
                                showCompanyMarker();
                                break;
                            case 1:
                                showFireRoomMarker();
                                break;
                            case 2:
                                showFireStationMarker();
                                break;
                            case 3:
                                showFireGroupMarker();
                                break;
                        }
                    }
                }
            });
            aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    LogUtil.i("点击的坐标为:" + latLng.latitude + "," + latLng.longitude);
                    if (currentMarker.getPosition().latitude != latLng.latitude &&
                            currentMarker.getPosition().longitude != latLng.longitude) {
                        bottom2.setVisibility(View.GONE);
                        mUiSettings.setLogoBottomMargin(UiUtils.dip2px(5));
                    } else {
                    }
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (a){
                            break;
                        }
                        hideMarkers(showingType,0);
                        showCompanyMarker();
                        showingType = 0;
                        break;
                    case 1:
                        a=false;
                        hideMarkers(showingType,1);
                        showFireRoomMarker();
                        showingType = 1;
                        break;
                    case 2:
                        a=false;
                        hideMarkers(showingType,2);
                        showFireStationMarker();
                        showingType=2;
                        break;
                    case 3:
                        a=false;
                        hideMarkers(showingType,3);
                        showFireGroupMarker();
                        showingType = 3;
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     * 隐藏单位marker
     * @param currentShowingType 现在展示在地图上的单位类型(即要隐藏的单位)
     * @param targetType//spinner点击选择的类型
     */
    private void hideMarkers(int currentShowingType, int targetType) {
//        LogUtils.i("currentShowingType："+currentShowingType+" targetType： "+targetType);
//        boolean a = currentShowingType!=targetType;
//        boolean b = markersArray.get(targetType).size()!=0;
//        LogUtils.i("1："+a+" 2： "+b);
//        &&markersArray.get(targetType).size()!=0
        if (currentShowingType!=targetType) {
//            for (Marker marker : markersArray.get(targetType)) {
//                marker.setVisible(true);
//            }

            for (Marker marker : markersArray.get(currentShowingType)) {
                marker.setVisible(false);
            }

        }else {
            UiUtils.showToast(BaseApplication.getApplication(),"无法获取该类型单位信息");
        }

    }


    public static final int S0CIETY = 0;//标识社会单位
    public static final int FIREROOM = 1;//标识社区消防室标识
    public static final int FIRESTATION = 2;//标识社区消防室标识
    public static final int FIREGROUP = 3;//标识社区消防室标识

    private boolean mFirstFix = false;//是否第一次修展示我的位置图标
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                LogUtils.i("我当前的坐标为:(" + amapLocation.getLatitude() + ","
                        + amapLocation.getLongitude() + ")");
                LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                if (!mFirstFix) {
//                    mMyLocation_pre = mMyLocation;
                    mMyLocation = null;
                    mMyLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    mFirstFix = true;
                    addCircle(location, amapLocation.getAccuracy());//添加定位精度圆
                    addMarker(location, amapLocation.getAddress());//添加定位图标
                    if (mapTypeState == 0 || mapTypeState == 1)
                        mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                    if (mMyLocation != null) {
                        getCompanyInfo(mMyLocation, "0.01", "0.001");
                        getFireRoomList(mMyLocation);
                        getFirStationList(mMyLocation);
                        getFireGroupList(mMyLocation);
                    }
                } else {
                    mCircle.setCenter(location);
                    mCircle.setRadius(amapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(location));
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                UiUtils.showToast(BaseApplication.getApplication(), "定位失败,请确认打开GPS/WIFI和网络连接");
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
            case R.id.search_cardview:
                Intent intent = new Intent();
                intent.setClass(Main_Activity.this, Search_activity.class);
                intent.putExtra("from", mLocMarker.getPosition());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.ll_add:
                startActivity(new Intent(this, Add_detail_activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.ll_nearby:
                break;
            case R.id.ll_detail:
                if (currentMarker != null) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("name", currentMarker.getTitle());
                    intent1.putExtra("from", mLocMarker.getPosition());
                    intent1.putExtra("to", currentMarker.getPosition());
                    intent1.putExtra("companyID", currentMarker.getSnippet());
                    intent1.setClass(Main_Activity.this, InfoDetail_activity.class);
                    startActivity(intent1);
                }
                break;
            case R.id.ll_gerenzhongxin:
                startActivity(new Intent(this, Personal_activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            mLocationOption = new AMapLocationClientOption();
//      //设置定位模式为AMapLocationMode.Hight_Accuracy，默认为高精度模式。
//      mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setInterval(10000);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(10000);
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
        currentMarker = marker;
        LogUtil.i(currentMarker.getPosition().toString());
//        bottom1.setVisibility(View.GONE);
        bottom2.setVisibility(View.VISIBLE);
        name.setText(currentMarker.getTitle().split("#")[0]);
        distance = AMapUtils.calculateLineDistance(mMyLocation, currentMarker.getPosition());
        DecimalFormat df = new DecimalFormat("######0.");
        distance = Float.valueOf(df.format(distance));

        if (currentMarker.getTitle().split("#")[1].equals("null")) {
            GeocodeSearch geocodeSearch = new GeocodeSearch(getApplicationContext());
            RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude), 200, GeocodeSearch.AMAP);
            geocodeSearch.getFromLocationAsyn(query);
            geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                    if (rCode == 1000) {
                        if (result != null && result.getRegeocodeAddress() != null
                                && result.getRegeocodeAddress().getFormatAddress() != null) {
                            addressName = result.getRegeocodeAddress().getFormatAddress();
                            if (addrDes != null) {
                                handler.post(new Runnable() {
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
                                });
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
            LogUtil.i(addressName);
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
            UiUtils.showToast(BaseApplication.getApplication(), result);
            Intent intent = new Intent();
            intent.putExtra("url", result);
            intent.setClass(this, InfoDetail_activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            IntentResult result1 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result1 != null) {
                if (result1.getContents() == null) {

                } else {
                    LogUtil.i(result1.getContents());
                    UiUtils.showToast(BaseApplication.getApplication(), result1.getContents());
                    Intent intent = new Intent();
                    intent.putExtra("url", result);
                    intent.setClass(this, InfoDetail_activity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.navi_map_gps_locked)));
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
        mLocMarker.setSnippet(addr);
    }

    public static final String LOCATION_MARKER_FLAG = "我的位置";


    private long[] mHits = new long[2];
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
                finish();
            } else {
                UiUtils.showToast(BaseApplication.getApplication(), "再按一次退出");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main, menu);
        setIconEnable(menu,true);  //  就是这一句使图标能显示
        return super.onCreateOptionsMenu(menu);
    }
    private void setIconEnable(Menu menu, boolean enable)
    {
        try
        {
            Class<?> clazz = Class.forName("android.support.v7.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);

            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
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
                if (mMyLocation!=null){
                    intent.putExtra("location",mMyLocation);
                }
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.check:
                startActivity(new Intent(this, Check_Activity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();
        }
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        deactivate();
        mFirstFix = false;
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
    public boolean isShow(MarkerOptions marker, Point screen) {
        Point target = aMap.getProjection().toScreenLocation(marker.getPosition());
        if (target.x < 0 || target.y < 0 || target.x > screen.x || target.y > screen.y) {
            return false;
        }
        return true;
    }

    SparseArray<ArrayList<Marker>> markersArray = new SparseArray<>();//所有marker集合的数组
    private int showingType = 0;//当前展示的单位类型
    //获取附近的社会单位位置
    private void getCompanyInfo(LatLng center, String lngFangwei, String latFangwei) {

        LatLng location = new LatLng(30.547186, 114.342014);
        if (CompanyMarkers.size() < 5)
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
//        RetrofitHelper.getApi().getCompanyList(String.valueOf(center.longitude), String.valueOf(center.latitude), "0.01", "0.001").enqueue(new Callback<CompanyListResult>() {
                    RetrofitHelper.getApi().getCompanyList("114.342014", "30.547186", "0.01", "0.001").enqueue(new Callback<CompanyListResult>() {
            @Override
            public void onResponse(Call<CompanyListResult> call, Response<CompanyListResult> response) {
                CompanyListResult result = response.body();
                if (result == null) {
                    UiUtils.showToast(BaseApplication.getApplication(), "获取服务器单位信息数据失败!");
                    return;
                }
                if (!result.status.equals("0") && result != null) {
                    List<CompanyList> lists = result.results;
                    for (CompanyList info : lists) {
                        if (info.lat == null || info.lng == null) {
                            continue;
                        }
                        double lat = Double.valueOf(info.lat);
                        double lng = Double.valueOf(info.lng);
                        LatLng src = new LatLng(lat, lng);
                        MarkerOptions marker = new MarkerOptions()
                                .position(src).title(info.name + "#" + info.address)
                                .snippet(info.id + "#" + S0CIETY + "#" + "1")
                                .icon(BitmapDescriptorFactory.fromBitmap(companyBitmap));
                        CompanyMarkerOptions.add(marker);
                    }
                    showCompanyMarker();
                }else {
//                    UiUtils.showToast(BaseApplication.getApplication(), result.msg);
                    LogUtil.i(result.msg);
                }
            }

            @Override
            public void onFailure(Call<CompanyListResult> call, Throwable t) {

            }
        });
    }

    /**
     *展示社会单位
     */
    private void showCompanyMarker() {
        if (CompanyMarkerOptions == null&&CompanyMarkerOptions.size()==0) {
            UiUtils.showToast(BaseApplication.getApplication(),"附近无政府社会单位信息");
            return;
        }
        for (int i = 0; i < CompanyMarkerOptions.size(); i++) {
            if (isShow(CompanyMarkerOptions.get(i), mScreenPoint)) {//假如markeroption的位置在屏幕上则把它添加到map并且放入CompanyMarkers集合
                boolean a = false;
                for (Marker ma : CompanyMarkers) {//遍历companymarkers是否添加了该markeroption
                    ma.setVisible(true);
                    if (ma.getOptions().equals(CompanyMarkerOptions.get(i))) {
                        a = true;
                        break;
                    }

                }
                if (!a) {//假如CompanyMarker里面还没有添加过,则添加上amap,并显示
                    Marker mar = aMap.addMarker(CompanyMarkerOptions.get(i));
                    CompanyMarkers.add(mar);
                    mar.setVisible(true);
                }

            } else {

            }
        }
    }

    /**
     * 获取消防室数据
     */
    public void getFireRoomList(LatLng center){

//        RetrofitHelper.getApi().getFireroomList(String.valueOf(center.longitude),String.valueOf(center.latitude))
        RetrofitHelper.getApi().getFireroomList("114.342014", "30.547186")
                .observeOn(AndroidSchedulers.mainThread())
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
                        if (list==null){
                            UiUtils.showToast(BaseApplication.getApplication(),"获取数据失败");
                            return;
                        }
                        if (list.status.equals("1")){
                                List<FireComponentGeneralInfo> lists = list.result;
                                for (FireComponentGeneralInfo info : lists) {
                                    if (info.lat == null || info.lng == null) {
                                        continue;
                                    }
                                    LogUtils.i(info.toString());
                                    double lat = Double.valueOf(info.lat);
                                    double lng = Double.valueOf(info.lng);
                                    LatLng src = new LatLng(lat, lng);
                                    //title后面拼凑字符串 name + addr;snippet 内容为 id+ 单位类型(社会单位、消防室四种，用于详情activity决定使用哪个fragment)+
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .position(src).title(info.name + "#"+"null" )
                                            .snippet(info.id + "#" + FIREROOM )
                                            .icon(BitmapDescriptorFactory.fromBitmap(fireroomBitmap));
                                    fireRoomOptions.add(markerOptions);
                                }
//                            
                        }else {
                            LogUtil.i("fireroom",list.msg);
                        }
                    }
                });
    }

    /**
     *，展示消防室单位
     */
    private void showFireRoomMarker() {
        if (fireRoomOptions == null&&fireRoomOptions.size()==0) {
            UiUtils.showToast(BaseApplication.getApplication(),"附近社区菜消防室信息");
            return;
        }
        for (int i = 0; i < fireRoomOptions.size(); i++) {
            if (isShow(fireRoomOptions.get(i), mScreenPoint)) {
                boolean a = false;
                for (Marker ma : fireRoomMarkers) {
                    ma.setVisible(true);
                    if (ma.getOptions().equals(fireRoomOptions.get(i))) {
                        a = true;
                        break;
                    }

                }
                if (!a) {
                    Marker mar = aMap.addMarker(fireRoomOptions.get(i));
                    fireRoomMarkers.add(mar);
                        mar.setVisible(true);
                }

            } else {
//
            }
        }
    }



    /**
     * 获取station数据
     */
    public void getFirStationList(LatLng center){

//        RetrofitHelper.getApi().getFireroomList(String.valueOf(center.longitude),String.valueOf(center.latitude))
        RetrofitHelper.getApi().getFirestationList("114.342014", "30.547186")
                .observeOn(AndroidSchedulers.mainThread())
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
                        if (list==null){
                            UiUtils.showToast(BaseApplication.getApplication(),"获取数据失败");
                            return;
                        }
                        if (list.status.equals("1")){

                            List<FireComponentGeneralInfo> lists = list.result;
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat != null || info.lng != null||!TextUtils.equals("",String.valueOf(info.lat))||!TextUtils.equals("",String.valueOf(info.lng))) {
                                LogUtils.i("小型站"+info.toString());
                                double lat = Double.valueOf(info.lat);
                                double lng = Double.valueOf(info.lng);
                                LatLng src = new LatLng(lat, lng);

                                //title后面拼凑字符串 name + addr;snippet 内容为 id+ 单位类型(社会单位、消防室四种，用于详情activity决定使用哪个fragment)+
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(src)
                                        .title(info.name + "#"+" " )
                                        .snippet(info.id + "#" + FIRESTATION )
                                        .icon(BitmapDescriptorFactory.fromBitmap(firestationBitmap));
                                fireStationOptions.add(markerOptions);
                                }
                            }

                        }else {
                            LogUtil.i("firestation",list.msg);
                        }
                    }
                });
    }

    /**
     *展示小型工作站makers
     */
    private void showFireStationMarker() {
        if (fireStationOptions == null&&fireStationOptions.size()==0) {
            UiUtils.showToast(BaseApplication.getApplication(),"附近无政府专职小型站信息");
            return;
        }
        for (int i = 0; i < fireStationOptions.size(); i++) {
            if (isShow(fireRoomOptions.get(i), mScreenPoint)) {
            boolean a = false;
            for (Marker ma : fireStationMarkers) {
                ma.setVisible(true);
                if (ma.getOptions().equals(fireStationOptions.get(i))) {
                    a = true;
                    break;
                }

            }
            if (!a) {
                Marker mar = aMap.addMarker(fireStationOptions.get(i));
                fireStationMarkers.add(mar);
//                LogUtils.i("小型站添加成功");
                    mar.setVisible(true);
            }

            } else {

            }
        }
    }



    /**
     * 获取消防中队数据
     */
    public void getFireGroupList(LatLng center){

//        RetrofitHelper.getApi().getFireroomList(String.valueOf(center.longitude),String.valueOf(center.latitude))
        RetrofitHelper.getApi().getFireGroupList("114.342014", "30.547186")
                .observeOn(AndroidSchedulers.mainThread())
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
                        if (list==null){
                            UiUtils.showToast(BaseApplication.getApplication(),"获取数据失败");
                            return;
                        }
                        if (list.status.equals("1")){
                            List<FireComponentGeneralInfo> lists = list.result;
                            for (FireComponentGeneralInfo info : lists) {
                                if (info.lat == null || info.lng == null) {
                                    continue;
                                }
                                LogUtils.i(info.toString());
                                double lat = Double.valueOf(info.lat);
                                double lng = Double.valueOf(info.lng);
                                LatLng src = new LatLng(lat, lng);
                                //title后面拼凑字符串 name + addr;snippet 内容为 id+ 单位类型(社会单位、消防室四种，用于详情activity决定使用哪个fragment)+
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .position(src).title(info.name + "#"+"null" )
                                        .snippet(info.id + "#" + FIREGROUP )
                                        .icon(BitmapDescriptorFactory.fromBitmap(fireroomBitmap));
                                fireGroupOptions.add(markerOptions);
                            }
//                            showFireRoomMarker();
                        }else {
                            LogUtil.i("firegroup",list.msg);
                        }
                    }
                });
    }

    /**
     *，展示消防中队单位
     */
    private void showFireGroupMarker() {
        if (fireGroupOptions == null&&fireGroupOptions.size()==0) {
            UiUtils.showToast(BaseApplication.getApplication(),"附近无消防中队信息");
            return;
        }
        for (int i = 0; i < fireGroupOptions.size(); i++) {
            if (isShow(fireGroupOptions.get(i), mScreenPoint)) {
                boolean a = false;
                for (Marker ma : fireGroupMarkers) {
                    ma.setVisible(true);
                    if (ma.getOptions().equals(fireGroupOptions.get(i))) {
                        a = true;
                        break;
                    }

                }
                if (!a) {
                    Marker mar = aMap.addMarker(fireGroupOptions.get(i));
                    fireGroupMarkers.add(mar);
                    mar.setVisible(true);
                }

            } else {
//
            }
        }
    }
}
