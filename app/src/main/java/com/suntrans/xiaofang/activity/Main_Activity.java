package com.suntrans.xiaofang.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.Util;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.add.Add_activity;
import com.suntrans.xiaofang.activity.check.Check_Activity;
import com.suntrans.xiaofang.activity.others.CameraScan_Activity;
import com.suntrans.xiaofang.activity.others.InfoDetail_activity;
import com.suntrans.xiaofang.activity.others.Personal_activity;
import com.suntrans.xiaofang.activity.others.Search_activity;
import com.suntrans.xiaofang.model.map.ClusterClickListener;
import com.suntrans.xiaofang.model.map.ClusterItem;
import com.suntrans.xiaofang.model.map.ClusterOverlay;
import com.suntrans.xiaofang.model.map.ClusterRender;
import com.suntrans.xiaofang.model.map.RegionItem;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.SensorEventHelper;
import com.suntrans.xiaofang.utils.UiUtils;
import com.tencent.bugly.Bugly;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amap.api.maps.AMapUtils.calculateLineDistance;

public class Main_Activity extends BasedActivity implements LocationSource, View.OnClickListener, AMap.OnMarkerClickListener, AMapLocationListener, MarkerHelper.onGetInfoFinishListener, ClusterClickListener {
    private static final String TAG = "Main_Activity";
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
    private LinearLayout bottom2;//单位信息底部菜单栏
    private LinearLayout detail;//单位详情


    private static final int maxZoom = 15;

    private Point mScreenPoint;//我的屏幕点的x和y最大值


    private GeocodeSearch geocodeSearch;//地点搜索
    private RegeocodeQuery query;
    private LocationManager manager;

    private MarkerHelper helper;


    private TextView textView;
    private ProgressDialog getDataDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Bugly.init(getApplicationContext(), "7d01f61d8c", false);
        initView();
        initMap(savedInstanceState);
    }

    private void initView() {


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
//        toolbar.setTitle("武汉消防基础信息采集平台");
//        toolbar.setTitleTextAppearance(this, R.style.toolbar);
        cardView = (CardView) findViewById(R.id.search_cardview);
        cardView.setOnClickListener(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        bottom2 = (LinearLayout) findViewById(R.id.ll_bottom2);
        name = (TextView) findViewById(R.id.text_company_name);
        addrDes = (TextView) findViewById(R.id.text_company_addr);
        detail = (LinearLayout) findViewById(R.id.ll_detail);


        detail.setOnClickListener(this);

        mScreenPoint = new Point();
        WindowManager manager = this.getWindowManager();
        manager.getDefaultDisplay().getSize(mScreenPoint);

    }

    LatLng currentCenterLocation;


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
//            aMap.setMinZoomLevel(15);
            aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {


                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    startAssign();
                }
            });
            aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
//                    LogUtil.i("点击的坐标为:" + latLng.latitude + "," + latLng.longitude);
                    LogUtil.i("map被点击了");
                    if (bottom2.getVisibility() == View.VISIBLE)
                    {

                        bottom2.setVisibility(View.GONE);
                        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menushow);
                        bottom2.startAnimation(animation);
                    }
                    if (llMoreDanwei.getVisibility() == View.VISIBLE) {

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
                    getData();

                }
            });
        }
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
        geocodeSearch = new GeocodeSearch(getApplicationContext());
    }

    private void getData() {

        getDataDialog = new ProgressDialog(Main_Activity.this);
        getDataDialog.setCancelable(false);
        getDataDialog.setMessage("正在加载消防信息,请稍后..");
        getDataDialog.show();
        LatLng latLng = new LatLng(30.54260, 114.358693);
        final String dis = "1000000";
        helper.getCompanyList(latLng, dis, "1");
        helper.getCommcmyList(latLng, dis);
        helper.getFireRoomList(latLng, dis);
        helper.getFireBrigadeList(latLng, dis);
        helper.getFireGroupList(latLng, dis);
        helper.getFireAdminStationList(latLng, dis);
        helper.getFireStationList(latLng, dis);
        helper.getLicenseList(latLng, dis);
    }


    private boolean mFirstFix = false;//是否第一次修展示我的位置图标
    private boolean isFristGet = true;

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
//                LogUtil.i("我当前的坐标为:(" + amapLocation.getLatitude() + "," + amapLocation.getLongitude() + ")");
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
//
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
                UiUtils.showToast("定位失败,请确认打开GPS/WIFI和网络连接");
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

            case R.id.ll_detail:
                if (currentmarkerOptions == null) {
                    LogUtil.e("当前选择的图标不存在");
                    return;
                }
                if (currentmarkerOptions.getPosition().equals(mMyLocation)) {
                    UiUtils.showToast(mLocMarker.getTitle());
                    return;
                }

                Intent intent1 = new Intent();
                intent1.putExtra("name", currentmarkerOptions.getTitle());
                intent1.putExtra("from", mMyLocation);
//                    intent1.putExtra("to", currentMarker.getPosition());
                intent1.putExtra("companyID", currentmarkerOptions.getSnippet());
                intent1.setClass(Main_Activity.this, InfoDetail_activity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                }
                break;

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
            LogUtil.i("停止定位");
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
//        mLocMarker.setTitle(LOCATION_MARKER_FLAG + "#" + title);
//        mLocMarker.setSnippet(addr);
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
                LogUtil.e("default=>");
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

        mClusterOverlay.onDestroy();
        mFireRoomClusterOverlay.onDestroy();
        mFireStationClusterOverlay.onDestroy();
        mFireAdminStationClusterOverlay.onDestroy();
        mFireBrigadeClusterOverlay.onDestroy();
        mFireGroupClusterOverlay.onDestroy();
        mLicenseClusterOverlay.onDestroy();
        mCommCmyClusterOverlay.onDestroy();
        super.onDestroy();
    }


    /**
     * 左下角button改变视图的按钮
     */
    private int mapTypeState = 0;//0没移动 1移动
    private int i = 0;

    public void changedViewType(View view) {
        if (mMyLocation==null){
            UiUtils.showToast("正在定位,请稍后..");
            return;
        }
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
                        LogUtil.e(TAG, "重点单位被选中");
                        if (mClusterOverlay != null)
                            mClusterOverlay.setVisible(true);
                    } else {
                        if (mClusterOverlay != null)
                            mClusterOverlay.setVisible(false);
                    }
                    break;

                case R.id.xiaofangshi:
                    if (isChecked) {
                        flag = flag | 0x02;
                        if (mFireRoomClusterOverlay != null)
                            mFireRoomClusterOverlay.setVisible(true);

                    } else {
                        flag = flag & 0xfd;
                        if (mFireRoomClusterOverlay != null)
                            mFireRoomClusterOverlay.setVisible(false);
                    }
                    break;

                case R.id.yibandanwei:
                    if (isChecked) {
                        flag = flag | 0x80;
                        if (mCommCmyClusterOverlay != null) {
                            mCommCmyClusterOverlay.setVisible(true);
                        }
                    } else {
                        flag = flag & 0x7f;
                        if (mCommCmyClusterOverlay != null) {
                            mCommCmyClusterOverlay.setVisible(false);
                        }
                    }
                    break;
                case R.id.dadui:
                    if (isChecked) {
                        flag = flag | 0x04;
                        if (mFireBrigadeClusterOverlay != null) {
                            mFireBrigadeClusterOverlay.setVisible(true);
                        }
                    } else {
                        flag = flag & 0xfb;
                        if (mFireBrigadeClusterOverlay != null) {
                            mFireBrigadeClusterOverlay.setVisible(false);
                        }
                    }
                    break;
                case R.id.zhongdui:
                    if (isChecked) {
                        flag = flag | 0x08;
                        if (mFireGroupClusterOverlay != null) {
                            mFireGroupClusterOverlay.setVisible(true);
                        }
                    } else {
                        flag = flag & 0xf7;
                        if (mFireGroupClusterOverlay != null) {
                            mFireGroupClusterOverlay.setVisible(false);
                        }
                    }

                    break;
                case R.id.xiaoxingzhan:
                    if (mFireAdminStationClusterOverlay == null) {
                        break;
                    }
                    if (isChecked) {
                        flag = flag | 0x40;
                        mFireAdminStationClusterOverlay.setVisible(true);
                    } else {
                        flag = flag & 0xbf;
                        mFireAdminStationClusterOverlay.setVisible(false);

                    }
                    break;
                case R.id.xiangcun:
                    if (mFireStationClusterOverlay == null) {
                        break;
                    }
                    if (isChecked) {
                        flag = flag | 0x10;
                        mFireStationClusterOverlay.setVisible(true);
                    } else {
                        flag = flag & 0xef;
                        mFireStationClusterOverlay.setVisible(false);
                    }
                    break;

                case R.id.xingzhenshenpi:
                    if (mLicenseClusterOverlay == null) {
                        return;
                    }
                    if (isChecked) {
                        flag = flag | 0x20;
                        mLicenseClusterOverlay.setVisible(true);
                    } else {
                        flag = flag & 0xdf;
                        mLicenseClusterOverlay.setVisible(false);

                    }
                    break;

            }
        }
    };


    boolean isrun = true;


    private ClusterOverlay mClusterOverlay;
    private final int clusterRadius = 100;
    int fireGroupClusterRadius = 30;
    int radius = UiUtils.dip2px(50);
    int drawableSize = UiUtils.dip2px(20);
    private SparseArray<Drawable> mCompanyDrawables = new SparseArray<>();

    @Override
    public void onCompanyDataFinish(final List<RegionItem> items) {
        LogUtil.i("重点单位个数:" + items.size());
        new Thread() {
            @Override
            public void run() {
                List<ClusterItem> items1 = new ArrayList<ClusterItem>(items);

                mClusterOverlay = new ClusterOverlay(aMap, items1,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), true);
                mClusterOverlay.setClusterRenderer(new ClusterRender() {
                    @Override
                    public Drawable getDrawAble(int clusterNum) {
                        if (clusterNum == 1) {
                            Drawable bitmapDrawable = mCompanyDrawables.get(1);
                            if (bitmapDrawable == null) {
                                Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_company);
                                Bitmap src = bitmap.createScaledBitmap(bitmap, drawableSize, drawableSize, true);
                                bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), src);
                                bitmap.recycle();
                                mCompanyDrawables.put(1, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        } else {
                            Drawable bitmapDrawable = mFireroomDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(159, 0, 220, 255)));
                                mCompanyDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mClusterOverlay.setOnClusterClickListener(Main_Activity.this);
            }
        }.start();
        dismissDialog();
    }


    private ClusterOverlay mCommCmyClusterOverlay;
    private SparseArray<Drawable> mCommCmyDrawables = new SparseArray<>();

    @Override
    public void onCommCmyDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "一般单位个数为:" + items.size());
        new Thread() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);

                mCommCmyClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), false);
                mCommCmyClusterOverlay.setClusterRenderer(new ClusterRender() {
                    @Override
                    public Drawable getDrawAble(int clusterNum) {
                        if (clusterNum == 1) {
                            Drawable bitmapDrawable = mCommCmyDrawables.get(1);
                            if (bitmapDrawable == null) {
                                Bitmap src = getBitmap(R.drawable.ic_commcmy);
                                bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), src);
                                mCommCmyDrawables.put(1, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        } else {
                            Drawable bitmapDrawable = mFireroomDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(159, 139, 0, 197)));
                                mCommCmyDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mCommCmyClusterOverlay.setOnClusterClickListener(Main_Activity.this);
            }
        }.start();
    }

    private ClusterOverlay mFireRoomClusterOverlay;
    private SparseArray<Drawable> mFireroomDrawables = new SparseArray<>();

    @Override
    public void onFireRoomDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "社区消防室个数:" + items.size());
        new Thread() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);

                mFireRoomClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), false);
                mFireRoomClusterOverlay.setClusterRenderer(new ClusterRender() {
                    @Override
                    public Drawable getDrawAble(int clusterNum) {
                        if (clusterNum == 1) {
                            Drawable bitmapDrawable = mFireroomDrawables.get(1);
                            if (bitmapDrawable == null) {
                                Bitmap src = getBitmap(R.drawable.ic_fireroom);
                                bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), src);
                                mFireroomDrawables.put(1, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        } else {
                            Drawable bitmapDrawable = mFireroomDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(159, 0, 220, 255)));
                                mFireroomDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mFireRoomClusterOverlay.setOnClusterClickListener(Main_Activity.this);
            }
        }.start();
    }


    private ClusterOverlay mFireBrigadeClusterOverlay;
    private SparseArray<Drawable> mFireBrigadeDrawables = new SparseArray<>();

    @Override
    public void onFireBrigadeDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "消防大队个数为:" + items.size());
        new Thread() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);

                mFireBrigadeClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(fireGroupClusterRadius, getApplicationContext()),
                        getApplicationContext(), false);
                mFireBrigadeClusterOverlay.setClusterRenderer(new ClusterRender() {
                    @Override
                    public Drawable getDrawAble(int clusterNum) {
                        if (clusterNum == 1) {
                            Drawable bitmapDrawable = mFireBrigadeDrawables.get(1);
                            if (bitmapDrawable == null) {
                                Bitmap src = getBitmap(R.drawable.ic_firegroup);
                                bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), src);
                                mFireBrigadeDrawables.put(1, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        } else {
                            Drawable bitmapDrawable = mFireBrigadeDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(159, 255, 0, 0)));
                                mFireBrigadeDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mFireBrigadeClusterOverlay.setOnClusterClickListener(Main_Activity.this);
            }
        }.start();
    }

    private ClusterOverlay mFireGroupClusterOverlay;
    private SparseArray<Drawable> mFireGroupDrawables = new SparseArray<>();

    @Override
    public void onFireGroupDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "消防中队个数为:" + items.size());
        new Thread() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);

                mFireGroupClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(fireGroupClusterRadius, getApplicationContext()),
                        getApplicationContext(), false);
                mFireGroupClusterOverlay.setClusterRenderer(new ClusterRender() {
                    @Override
                    public Drawable getDrawAble(int clusterNum) {
                        if (clusterNum == 1) {
                            Drawable bitmapDrawable = mFireGroupDrawables.get(1);
                            if (bitmapDrawable == null) {
                                Bitmap src = getBitmap(R.drawable.ic_firegroup);
                                bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), src);
                                mFireGroupDrawables.put(1, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        } else {
                            Drawable bitmapDrawable = mFireGroupDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(159, 195, 0, 0)));
                                mFireGroupDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mFireGroupClusterOverlay.setOnClusterClickListener(Main_Activity.this);
            }
        }.start();
    }


    private ClusterOverlay mFireAdminStationClusterOverlay;
    private SparseArray<Drawable> mFireAdminStationDrawables = new SparseArray<>();

    @Override
    public void onFireAdminStationDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "政府专职小型站个数为:" + items.size());
        new Thread() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);

                mFireAdminStationClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), false);
                mFireAdminStationClusterOverlay.setClusterRenderer(new ClusterRender() {
                    @Override
                    public Drawable getDrawAble(int clusterNum) {
                        if (clusterNum == 1) {
                            Drawable bitmapDrawable = mFireAdminStationDrawables.get(1);
                            if (bitmapDrawable == null) {
                                Bitmap src = getBitmap(R.drawable.ic_fireroom);
                                bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), src);
                                mFireAdminStationDrawables.put(1, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        } else {
                            Drawable bitmapDrawable = mFireAdminStationDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(159, 0x7f, 0xff, 0)));
                                mFireAdminStationDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mFireAdminStationClusterOverlay.setOnClusterClickListener(Main_Activity.this);
            }
        }.start();
    }

    private ClusterOverlay mFireStationClusterOverlay;
    private SparseArray<Drawable> mFireStationDrawables = new SparseArray<>();

    @Override
    public void onFireStationDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "乡村专职消防队个数为:" + items.size());
        new Thread() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);

                mFireStationClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), false);
                mFireStationClusterOverlay.setClusterRenderer(new ClusterRender() {
                    @Override
                    public Drawable getDrawAble(int clusterNum) {
                        if (clusterNum == 1) {
                            Drawable bitmapDrawable = mFireStationDrawables.get(1);
                            if (bitmapDrawable == null) {
                                Bitmap src = getBitmap(R.drawable.ic_fireroom);
                                bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), src);
                                mFireStationDrawables.put(1, bitmapDrawable);
                            }
                            return bitmapDrawable;

                        } else {
                            Drawable bitmapDrawable = mFireStationDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(159, 0xda, 0xa5, 0x20)));
                                mFireStationDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mFireStationClusterOverlay.setOnClusterClickListener(Main_Activity.this);
            }
        }.start();
    }

    private ClusterOverlay mLicenseClusterOverlay;
    private SparseArray<Drawable> mLicenseDrawablse = new SparseArray<>();

    @Override
    public void onLicenseDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "行政审批项目个数为:" + items.size());
        new Thread() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);

                mLicenseClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), false);
                mLicenseClusterOverlay.setClusterRenderer(new ClusterRender() {
                    @Override
                    public Drawable getDrawAble(int clusterNum) {
                        if (clusterNum == 1) {
                            Drawable bitmapDrawable = mLicenseDrawablse.get(1);
                            if (bitmapDrawable == null) {
                                Bitmap src = getBitmap(R.drawable.ic_license);
                                bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), src);
                                mLicenseDrawablse.put(1, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        } else {
                            Drawable bitmapDrawable = mLicenseDrawablse.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(159, 0, 0, 0)));
                                mLicenseDrawablse.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mLicenseClusterOverlay.setOnClusterClickListener(Main_Activity.this);
            }
        }.start();
    }

    @Override
    public void onLoadFailure(int code) {
        dismissDialog();
    }

    private void dismissDialog() {
        if (getDataDialog.isShowing()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDataDialog.dismiss();
                }
            },500);
        }
    }


    MarkerOptions currentmarkerOptions = null;

    @Override
    public void onClusterItemClick(Marker marker, final List<ClusterItem> clusterItems) {

        if (clusterItems.size() == 1) {
            final String addr = ((RegionItem) clusterItems.get(0)).getAddr();
            String name1 = ((RegionItem) clusterItems.get(0)).getName();
            String id = ((RegionItem) clusterItems.get(0)).getId();
            LatLng po = ((RegionItem) clusterItems.get(0)).getPosition();
            int type = ((RegionItem) clusterItems.get(0)).getType();
            LogUtil.e("name=" + name1 + ",addr=" + addr + ",id=" + id + ",position=" + po.toString() + "type=" + type);
            String markerTitle = name1 + "#" + addr;
            String markerSnippet = id + "#" + type + "#" + "1";
            currentmarkerOptions = new MarkerOptions();
            currentmarkerOptions.title(markerTitle)
                    .snippet(markerSnippet)
                    .position(po);

            mUiSettings.setLogoBottomMargin(UiUtils.dip2px(100));

            bottom2.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menushow);
            bottom2.startAnimation(animation);
            name.setText(name1);
            distance = calculateLineDistance(mMyLocation, clusterItems.get(0).getPosition());
            DecimalFormat df = new DecimalFormat("######0.");
            distance = Float.valueOf(df.format(distance));


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

        } else if (aMap.getCameraPosition().zoom == aMap.getMaxZoomLevel()) {
            List<String> strs = new ArrayList<>();
            for (ClusterItem item :
                    clusterItems) {
                strs.add(((RegionItem) item).getName());
            }
            String[] str = new String[strs.size()];
            strs.toArray(str);
            new AlertDialog.Builder(Main_Activity.this)
                    .setTitle("选择单位")
                    .setItems(str, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name1 = ((RegionItem) clusterItems.get(which)).getName() + "#" + ((RegionItem) clusterItems.get(which)).getAddr();
                            String snippet = ((RegionItem) clusterItems.get(which)).getId() + "#" + ((RegionItem) clusterItems.get(which)).getType() + "#" + "1";
                            Intent intent1 = new Intent();
                            intent1.putExtra("name", name1);
                            intent1.putExtra("from", mMyLocation);
//                    intent1.putExtra("to", currentMarker.getPosition());
                            intent1.putExtra("companyID", snippet);
                            intent1.setClass(Main_Activity.this, InfoDetail_activity.class);
                            startActivity(intent1);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }).create().show();

        } else {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (ClusterItem clusterItem : clusterItems) {
                builder.include(clusterItem.getPosition());
            }
            LatLngBounds latLngBounds = builder.build();
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
        }

    }


//    private class RunnableMockLocation implements Runnable {
//
//        @Override
//        public void run() {
//            while (isrun) {
//                try {
//                    Thread.sleep(1000);
////                    System.out.println("正在模拟位置");
//                    if (hasAddTestProvider == false) {
//                        continue;
//                    }
//                    try {
//                        // 模拟位置（addTestProvider成功的前提下）
//                        String providerStr = LocationManager.GPS_PROVIDER;
//                        Location mockLocation = new Location(providerStr);
//
//                        mockLocation.setLatitude(30.542605);   // 维度（度）
//                        mockLocation.setLongitude(114.358693);  // 经度（度）
//                        mockLocation.setAltitude(30);    // 高程（米）
//                        mockLocation.setBearing(180);   // 方向（度）
////                        mockLocation.setSpeed(10);    //速度（米/秒）
//
//                        mockLocation.setAccuracy(10f);   // 精度（米）
//                        mockLocation.setTime(new Date().getTime());   // 本地时间
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                            mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
//                        }
//                        manager.setTestProviderLocation(providerStr, mockLocation);
//                    } catch (Exception e) {
//                        // 防止用户在软件运行过程中关闭模拟位置或选择其他应用
//                        stopMockLocation();
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    boolean hasAddTestProvider = false;
//
//    private void a() {
//
//        boolean canMockPosition = (Settings.Secure.getInt(getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0)
//                || Build.VERSION.SDK_INT > 22;
//        if (canMockPosition && hasAddTestProvider == false) {
//            try {
//                String providerStr = LocationManager.GPS_PROVIDER;
//                LocationProvider provider = manager.getProvider(providerStr);
//                if (provider != null) {
//                    manager.addTestProvider(
//                            provider.getName()
//                            , provider.requiresNetwork()
//                            , provider.requiresSatellite()
//                            , provider.requiresCell()
//                            , provider.hasMonetaryCost()
//                            , provider.supportsAltitude()
//                            , provider.supportsSpeed()
//                            , provider.supportsBearing()
//                            , provider.getPowerRequirement()
//                            , provider.getAccuracy());
//                } else {
//                    manager.addTestProvider(
//                            providerStr
//                            , true, true, false, false, true, true, true
//                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
//                }
//                manager.setTestProviderEnabled(providerStr, true);
//                manager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
//
//                // 模拟位置可用
//                hasAddTestProvider = true;
//                canMockPosition = true;
//            } catch (SecurityException e) {
//                canMockPosition = false;
//            }
//        }
//    }
//
//    /**
//     * 停止模拟位置，以免启用模拟数据后无法还原使用系统位置
//     * 若模拟位置未开启，则removeTestProvider将会抛出异常；
//     * 若已addTestProvider后，关闭模拟位置，未removeTestProvider将导致系统GPS无数据更新；
//     */
//    public void stopMockLocation() {
//        if (hasAddTestProvider) {
//            try {
//                manager.removeTestProvider(LocationManager.GPS_PROVIDER);
//            } catch (Exception ex) {
//                // 若未成功addTestProvider，或者系统模拟位置已关闭则必然会出错
//            }
//            hasAddTestProvider = false;
//        }
//    }


    //根据marker是否在屏幕上展示marker
    public boolean isShow(MarkerOptions marker, Point screen) {
        Point target = aMap.getProjection().toScreenLocation(marker.getPosition());
        if (target.x < 0 || target.y < 0 || target.x > screen.x || target.y > screen.y) {
            return false;
        }
        return true;
    }


    @Override
    public void onOptionsMenuClosed(Menu menu) {

        super.onOptionsMenuClosed(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (llMoreDanwei.getVisibility() == View.VISIBLE) {
            llMoreDanwei.setVisibility(View.GONE);
            isShowDanweiMenu = false;
        }
        return super.onMenuOpened(featureId, menu);
    }


    private Bitmap drawCircle(int radius, int color) {

        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
        paint.setColor(color);
        canvas.drawArc(rectF, 0, 360, true, paint);
        return bitmap;
    }

    private Bitmap getBitmap(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), resId);
        Bitmap src = bitmap.createScaledBitmap(bitmap, drawableSize, drawableSize, true);
        bitmap.recycle();
        return src;
    }

    private void startAssign() {

        if (mClusterOverlay != null) {
            mClusterOverlay.startAssignClusters();
        }
        if (mFireRoomClusterOverlay != null) {
            mFireRoomClusterOverlay.startAssignClusters();
        }
        if (mCommCmyClusterOverlay != null) {
            mCommCmyClusterOverlay.startAssignClusters();
        }

        if (mFireBrigadeClusterOverlay != null) {
            mFireBrigadeClusterOverlay.startAssignClusters();
        }
        if (mFireGroupClusterOverlay != null) {
            mFireGroupClusterOverlay.startAssignClusters();
        }
        if (mFireAdminStationClusterOverlay != null) {
            mFireAdminStationClusterOverlay.startAssignClusters();
        }
        if (mFireStationClusterOverlay != null) {
            mFireStationClusterOverlay.startAssignClusters();
        }
        if (mLicenseClusterOverlay != null) {
            mLicenseClusterOverlay.startAssignClusters();
        }
    }

    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;
    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo != null && netInfo.isAvailable()) {

                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    if(netInfo.getType()== ConnectivityManager.TYPE_WIFI){
                        LogUtil.i("Internet","网络改变==>网络变成了wifi");

                    }else if(netInfo.getType()== ConnectivityManager.TYPE_ETHERNET) {
                        LogUtil.i("Internet", "网络改变==>网络变成了有线");

                    }else if(netInfo.getType()== ConnectivityManager.TYPE_MOBILE) {
                        LogUtil.i("Internet", "网络改变==>网络变成了移动网");
                    }
                } else {
                    LogUtil.i("Internet","网络改变==>网络断开");

                }
            }

        }
    };

}
