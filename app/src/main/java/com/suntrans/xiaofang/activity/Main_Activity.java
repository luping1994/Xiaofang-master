package com.suntrans.xiaofang.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
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
import com.pgyersdk.update.PgyUpdateManager;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.add.Add_activity;
import com.suntrans.xiaofang.activity.check.Check_Activity;
import com.suntrans.xiaofang.activity.others.CameraScan_Activity;
import com.suntrans.xiaofang.activity.others.InfoDetail_activity;
import com.suntrans.xiaofang.activity.others.Personal_activity;
import com.suntrans.xiaofang.activity.others.Search_activity;
import com.suntrans.xiaofang.activity.others.Smarfire_tActivity;
import com.suntrans.xiaofang.adapter.InfoWinAdapter;
import com.suntrans.xiaofang.model.A;
import com.suntrans.xiaofang.model.map.Cluster;
import com.suntrans.xiaofang.model.map.ClusterClickListener;
import com.suntrans.xiaofang.model.map.ClusterItem;
import com.suntrans.xiaofang.model.map.ClusterOverlay;
import com.suntrans.xiaofang.model.map.ClusterRender;
import com.suntrans.xiaofang.model.map.RegionItem;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.MarkerHelper;
import com.suntrans.xiaofang.utils.RxBus;
import com.suntrans.xiaofang.utils.SensorEventHelper;
import com.suntrans.xiaofang.utils.ThreadManager;
import com.suntrans.xiaofang.utils.UiUtils;

import java.lang.reflect.Method;
import java.nio.channels.NonReadableChannelException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.amap.api.maps.AMapUtils.calculateLineDistance;
import static com.suntrans.xiaofang.R.id.check;
import static com.suntrans.xiaofang.R.id.marker;

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

    @BindView(R.id.smartxiaofan)
    AppCompatCheckBox smartxiaofan;


    @BindView(R.id.ll_more_danwei)
    LinearLayout llMoreDanwei;
    @BindView(R.id.bottom_menu)
    RelativeLayout bottomMenu;

    @BindView(R.id.ll_control)
    LinearLayout control;

    private Toolbar toolbar;
    private MapView mapView = null;
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

    //底部菜单栏
    private TextView name;//单位名字
    private TextView addrDes;//单位地址描述
    private CardView bottom2;//单位信息底部菜单栏


    private Point mScreenPoint;//我的屏幕点的x和y最大值


    private GeocodeSearch geocodeSearch;//地点搜索
    private RegeocodeQuery query;

    private MarkerHelper helper;


    private ProgressDialog getDataDialog;
    private Marker smartFireMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initMap(savedInstanceState);
    }

    private void initView() {

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("net.suntrans.xiaofang.lp");
        registerReceiver(myNetReceiver, mFilter);   //注册接收网络连接状态改变广播接收器

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
        smartxiaofan.setSupportButtonTintList(ColorStateList.valueOf(Color.WHITE));
        zhongdiandanwei.setChecked(true);

        zhongdiandanwei.setOnCheckedChangeListener(listener);
        yibandanwei.setOnCheckedChangeListener(listener);
        dadui.setOnCheckedChangeListener(listener);
        zhongdui.setOnCheckedChangeListener(listener);
        xiaoxingzhan.setOnCheckedChangeListener(listener);
        xiangcun.setOnCheckedChangeListener(listener);
        xiaofangshi.setOnCheckedChangeListener(listener);
        xingzhenshenpi.setOnCheckedChangeListener(listener);
        smartxiaofan.setOnCheckedChangeListener(listener);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("武汉市消防信息采集端");
//        toolbar.setTitleTextAppearance(this, R.style.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        bottom2 = (CardView) findViewById(R.id.ll_bottom2);
        name = (TextView) findViewById(R.id.text_company_name);
        addrDes = (TextView) findViewById(R.id.text_company_addr);
        bottom2.setOnClickListener(this);

        mScreenPoint = new Point();
        WindowManager manager = this.getWindowManager();
        manager.getDefaultDisplay().getSize(mScreenPoint);

        getDataDialog = new ProgressDialog(Main_Activity.this);
        getDataDialog.setCancelable(false);


        RxBus.getInstance().toObserverable(A.class)
                .compose(this.<A>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<A>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(final A a) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handleSearchResult(a);
                            }
                        },500);
                    }
                });

        PgyUpdateManager.register(this,"com.suntrans.xiaofang.fileProvider");

    }

    private void handleSearchResult(A a) {
        int type = a.type;
        switch (type) {
            case MarkerHelper.S0CIETY:
                zhongdiandanwei.setChecked(true);
                break;
            case MarkerHelper.COMMONCOMPANY:
                yibandanwei.setChecked(true);
                break;
            case MarkerHelper.FIREROOM:
                xiaofangshi.setChecked(true);
                break;
            case MarkerHelper.FIREBRIGADE:
                dadui.setChecked(true);
                break;
            case MarkerHelper.FIREGROUP:
                zhongdui.setChecked(true);
                break;
            case MarkerHelper.FIREADMINSTATION:
                xiaoxingzhan.setChecked(true);
                break;
            case MarkerHelper.FIRESTATION:
                xiangcun.setChecked(true);
                break;
            case MarkerHelper.LICENSE:
                xingzhenshenpi.setChecked(true);
                break;
        }
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(a.latLng, 18, 0, 0)));
        name.setText(a.title);
        addrDes.setText("");
        currentmarkerOptions.title(a.title)
                .snippet(a.id+"#"+a.type)
                .position(a.latLng);
        showBottom2Menu();
//        query = new RegeocodeQuery(new LatLonPoint(a.latLng.latitude,a.latLng.longitude), 200, GeocodeSearch.AMAP);
//        geocodeSearch.getFromLocationAsyn(query);
//        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//            @Override
//            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
//                if (rCode == 1000) {
//                    if (result != null && result.getRegeocodeAddress() != null
//                            && result.getRegeocodeAddress().getFormatAddress() != null) {
//                        addressName = result.getRegeocodeAddress().getFormatAddress();
//                        if (addrDes != null) {
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    String dist;
//                                    if (distance > 1000) {
//                                        dist = distance / 1000 + "千米";
//                                    } else {
//                                        dist = distance + "米";
//                                    }
//                                    addrDes.setText("距离您" + dist + "| " + addressName);
//                                    showBottom2Menu();
//                                }
//                            }, 500);
//                        }
//                    } else {
//
//                    }
//                } else {
//                }
//            }
//
//            @Override
//            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//
//            }
//        });
//        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
    }

    //初始化map
    private void initMap(Bundle savedInstanceState) {

        mapView = (MapView) findViewById(R.id.mapview);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            //先把地图视角移动到武汉市
            LatLng localLatLng = new LatLng(30.5810841269, 114.316200103);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localLatLng, 13));
            mUiSettings = aMap.getUiSettings();
            aMap.setLocationSource(this);// 设置定位资源。如果不设置此定位资源则定位按钮不可点击。并且实现activate激活定位,停止定位的回调方法
            mUiSettings.setScaleControlsEnabled(true);//显示比例尺控件


            mUiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
            mUiSettings.setRotateGesturesEnabled(false);//设置地图是否可以旋转
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//            aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
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
                    closeBottomMenu();
                    closeDanwei();

                }
            });
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    aMap.showBuildings(true);


                    MarkerOptions options = new MarkerOptions();
                    LatLng latLng = new LatLng(30.542522, 114.358749);
                    options.position(latLng)
                            .title("武汉大学");
                    Cluster cluster = new Cluster(latLng);

                    smartFireMarker = aMap.addMarker(options);
                    smartFireMarker.setObject(cluster);
                    smartFireMarker.setVisible(false);
                    getData(1);
                }
            });
        }
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
        geocodeSearch = new GeocodeSearch(getApplicationContext());
        aMap.setOnMarkerClickListener(this);
//        InfoWinAdapter adapter = new InfoWinAdapter(this) ;
//        aMap.setInfoWindowAdapter(adapter);

    }


    private boolean isRefreshing = false;
    private int currentRefreshtype = 1;

    private void getData(int type) {
        currentRefreshtype = type;
        if (isRefreshing) {
            UiUtils.showToast("正在努力获取数据~");
            return;
        }
        final LatLng latLng = new LatLng(30.54260, 114.358693);
        final String dis = "1000000";
        count = 0;
        isRefreshing = true;

        if (type == 1) {
            getDataDialog.setMessage("正在加载,请稍后..");
        } else if (type == 2) {
            getDataDialog.setMessage("正在刷新数据,请稍后..");
        }
        getDataDialog.show();
        destroyoverlay(-1);

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

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                LogUtil.i("我当前的坐标为:(" + amapLocation.getLatitude() + "," + amapLocation.getLongitude() + ")");
                LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());


                if (!mFirstFix) {
                    mMyLocation = null;
                    mMyLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
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

                    mMyLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    mCircle.setCenter(location);
                    mCircle.setRadius(amapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(location));
                }


            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                UiUtils.showToast("定位失败,请打开GPS/WIFI和网络连接");
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

            case R.id.ll_bottom2:
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
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    UiUtils.showToast("无效的二维码");
                }
            }

        } else {
            IntentResult result1 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result1 != null) {
                if (result1.getContents() == null) {
                    UiUtils.showToast("无效的二维码");
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
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
            closeDanwei();
            if (bottomMenu.getVisibility() == View.VISIBLE) {
                closeBottomMenu();
                return true;
            }
            System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
            mHits[mHits.length - 1] = SystemClock.uptimeMillis();
            if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
                Process.killProcess(Process.myPid());
            } else {
                UiUtils.showToast("再按一次退出");
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
            case check:
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
                overridePendingTransition(0, 0);
                break;
            case R.id.danwei:
                if (isShowDanweiMenu) {
                    closeDanwei();
                } else {
                    openDanwei();
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
        destroyoverlay(-1);


        if (myNetReceiver != null) {
            unregisterReceiver(myNetReceiver);   //注销接收网络变化的广播通知的广播接收器
        }
        PgyUpdateManager.unregister();
        super.onDestroy();
    }

    private void destroyoverlay(int firetype) {
        switch (firetype) {
            case MarkerHelper.S0CIETY:
                destroy(mClusterOverlay);
                break;
            case MarkerHelper.COMMONCOMPANY:
                destroy(mCommCmyClusterOverlay);

                break;
            case MarkerHelper.FIREROOM:
                destroy(mFireRoomClusterOverlay);
                break;
            case MarkerHelper.FIREBRIGADE:
                destroy(mFireBrigadeClusterOverlay);

                break;
            case MarkerHelper.FIREGROUP:
                destroy(mFireGroupClusterOverlay);

                break;
            case MarkerHelper.FIREADMINSTATION:
                destroy(mFireAdminStationClusterOverlay);

                break;
            case MarkerHelper.FIRESTATION:
                destroy(mFireStationClusterOverlay);

                break;
            case MarkerHelper.LICENSE:
                destroy(mLicenseClusterOverlay);

                break;
            case -1:
                destroy(mClusterOverlay);
                destroy(mFireRoomClusterOverlay);
                destroy(mFireStationClusterOverlay);
                destroy(mFireAdminStationClusterOverlay);
                destroy(mFireBrigadeClusterOverlay);
                destroy(mFireGroupClusterOverlay);
                destroy(mLicenseClusterOverlay);
                destroy(mCommCmyClusterOverlay);
                break;

        }

    }

    private void destroy(ClusterOverlay overlay) {
        if (overlay != null) {
            overlay.onDestroy();
            overlay = null;
        }
    }

    /**
     * 左下角button改变视图的按钮
     */
    private int mapTypeState = 0;//0没移动 1移动
    private int i = 0;

    public void changedViewType(View view) {
        if (mMyLocation == null) {
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
    int flag_biao = 0x01;
    int flag_Reversal = 0xfe;
    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.zhongdiandanwei:
                    if (isChecked) {
                        flag = flag | 0x01;
                        if (mClusterOverlay != null)
                            mClusterOverlay.setVisible(true);
                        else {
//                            getData(1,MarkerHelper.S0CIETY);
                        }
                    } else {
                        flag = flag & 0xfe;
                        if (mClusterOverlay != null)
                            mClusterOverlay.setVisible(false);
                    }
                    break;
                case R.id.yibandanwei:
                    if (isChecked) {
                        flag = flag | 0x02;
                        if (mCommCmyClusterOverlay != null) {
                            mCommCmyClusterOverlay.setVisible(true);
                        } else {
//                            getData(1,MarkerHelper.COMMONCOMPANY);

                        }
                    } else {
                        flag = flag & 0xfd;
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
                        } else {
//                            getData(1,MarkerHelper.FIREBRIGADE);

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
                        } else {
//                            getData(1,MarkerHelper.FIREGROUP);

                        }
                    } else {
                        flag = flag & 0xf7;
                        if (mFireGroupClusterOverlay != null) {
                            mFireGroupClusterOverlay.setVisible(false);
                        }
                    }

                    break;
                case R.id.xiaoxingzhan:

                    if (isChecked) {
                        flag = flag | 0x10;
                        if (mFireAdminStationClusterOverlay != null)
                            mFireAdminStationClusterOverlay.setVisible(true);
                    } else {
                        flag = flag & 0xef;
                        if (mFireAdminStationClusterOverlay != null)
                            mFireAdminStationClusterOverlay.setVisible(false);
                    }
                    break;
                case R.id.xiangcun:
                    if (isChecked) {
                        flag = flag | 0x20;
                        if (mFireStationClusterOverlay != null)
                            mFireStationClusterOverlay.setVisible(true);
                    } else {
                        flag = flag & 0xdf;
                        if (mFireStationClusterOverlay != null)
                            mFireStationClusterOverlay.setVisible(false);
                    }
                    break;
                case R.id.xiaofangshi:
                    if (isChecked) {
                        flag = flag | 0x40;
                        if (mFireRoomClusterOverlay != null)
                            mFireRoomClusterOverlay.setVisible(true);
                        else {
//                            getData(1,MarkerHelper.FIREROOM);
                        }
                    } else {
                        flag = flag & 0xbf;

                        if (mFireRoomClusterOverlay != null)
                            mFireRoomClusterOverlay.setVisible(false);
                    }
                    break;

                case R.id.xingzhenshenpi:

                    if (isChecked) {
                        flag = flag | 0x80;
                        if (mLicenseClusterOverlay != null)
                            mLicenseClusterOverlay.setVisible(true);
                        else {
//                            getData(1,MarkerHelper.LICENSE);

                        }
                    } else {
                        flag = flag & 0x7f;
                        if (mLicenseClusterOverlay != null)
                            mLicenseClusterOverlay.setVisible(false);

                    }
                    break;
                case R.id.smartxiaofan:
                    if (isChecked) {
                        smartFireMarker.setVisible(true);
                    } else {
                        smartFireMarker.setVisible(false);
                    }
                    break;
            }
        }
    };


    boolean isrun = true;
    private ClusterOverlay mClusterOverlay;
    private int toumingdu = 200;

    private final int clusterRadius = 100;
    int fireGroupClusterRadius = 30;
    int radius = UiUtils.dip2px(50);
    int drawableSize = UiUtils.dip2px(20);
    private SparseArray<Drawable> mCompanyDrawables = new SparseArray<>();


    @Override
    public void onCompanyDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "重点单位个数:" + items.size());
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ClusterItem> items1 = new ArrayList<ClusterItem>(items);
                if (mClusterOverlay != null) {
                    mClusterOverlay.onDestroy();
                }
                boolean a = (flag & 0x01) == 0x01;
                mClusterOverlay = new ClusterOverlay(aMap, items1,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), a);
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
                            Drawable bitmapDrawable = mCompanyDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(toumingdu, 0, 0x17, 0xf0)));
                                mCompanyDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mClusterOverlay.setOnClusterClickListener(Main_Activity.this);
                checkDownLoad();
            }
        });
    }


    private ClusterOverlay mCommCmyClusterOverlay;
    private SparseArray<Drawable> mCommCmyDrawables = new SparseArray<>();

    @Override
    public void onCommCmyDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "一般单位个数为:" + items.size());
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);
                if (mCommCmyClusterOverlay != null) {
                    mCommCmyClusterOverlay.onDestroy();
                }
                boolean a = (flag & 0x02) == 0x02;

                mCommCmyClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), a);
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
                            Drawable bitmapDrawable = mCommCmyDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(toumingdu, 0x00, 237, 240)));
                                mCommCmyDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mCommCmyClusterOverlay.setOnClusterClickListener(Main_Activity.this);
                checkDownLoad();

            }
        });
    }

    private ClusterOverlay mFireRoomClusterOverlay;
    private SparseArray<Drawable> mFireroomDrawables = new SparseArray<>();

    @Override
    public void onFireRoomDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "社区消防室个数:" + items.size());
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);
                if (mFireRoomClusterOverlay != null) {
                    mFireRoomClusterOverlay.onDestroy();
                }
                boolean a = (flag & 0x40) == 0x40;
                LogUtil.i("fireroom是否开始显示:" + a);
                mFireRoomClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), a);
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
                                        Color.argb(toumingdu, 0x93, 0x00, 0xf0)));
                                mFireroomDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mFireRoomClusterOverlay.setOnClusterClickListener(Main_Activity.this);
                checkDownLoad();

            }
        });
    }


    private ClusterOverlay mFireBrigadeClusterOverlay;
    private SparseArray<Drawable> mFireBrigadeDrawables = new SparseArray<>();

    @Override
    public void onFireBrigadeDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "消防大队个数为:" + items.size());
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);
                if (mFireBrigadeClusterOverlay != null) {
                    mFireBrigadeClusterOverlay.onDestroy();
                }
                boolean a = (flag & 0x04) == 0x04;

                mFireBrigadeClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(fireGroupClusterRadius, getApplicationContext()),
                        getApplicationContext(), a);
                mFireBrigadeClusterOverlay.setClusterRenderer(new ClusterRender() {
                    @Override
                    public Drawable getDrawAble(int clusterNum) {
                        if (clusterNum == 1) {
                            Drawable bitmapDrawable = mFireBrigadeDrawables.get(1);
                            if (bitmapDrawable == null) {
                                Bitmap src = getBitmap(R.drawable.ic_firebrigade);
                                bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), src);
                                mFireBrigadeDrawables.put(1, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        } else {
                            Drawable bitmapDrawable = mFireBrigadeDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(toumingdu, 0x96, 0x58, 0x2a)));
                                mFireBrigadeDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mFireBrigadeClusterOverlay.setOnClusterClickListener(Main_Activity.this);
                checkDownLoad();

            }
        });
    }

    private ClusterOverlay mFireGroupClusterOverlay;
    private SparseArray<Drawable> mFireGroupDrawables = new SparseArray<>();

    @Override
    public void onFireGroupDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "消防中队个数为:" + items.size());
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);
                if (mFireGroupClusterOverlay != null) {
                    mFireGroupClusterOverlay.onDestroy();
                }
                boolean a = (flag & 0x08) == 0x08;
                mFireGroupClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(fireGroupClusterRadius, getApplicationContext()),
                        getApplicationContext(), a);
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
                                        Color.argb(toumingdu, 0xf0, 0x88, 0x00)));
                                mFireGroupDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mFireGroupClusterOverlay.setOnClusterClickListener(Main_Activity.this);
                checkDownLoad();

            }
        });
    }


    private ClusterOverlay mFireAdminStationClusterOverlay;
    private SparseArray<Drawable> mFireAdminStationDrawables = new SparseArray<>();

    @Override
    public void onFireAdminStationDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "政府专职小型站个数为:" + items.size());
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);
                if (mFireAdminStationClusterOverlay != null) {
                    mFireAdminStationClusterOverlay.onDestroy();
                }
                boolean a = (flag & 0x10) == 0x10;

                mFireAdminStationClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), a);
                mFireAdminStationClusterOverlay.setClusterRenderer(new ClusterRender() {
                    @Override
                    public Drawable getDrawAble(int clusterNum) {
                        if (clusterNum == 1) {
                            Drawable bitmapDrawable = mFireAdminStationDrawables.get(1);
                            if (bitmapDrawable == null) {
                                Bitmap src = getBitmap(R.drawable.ic_fireadminstation);
                                bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), src);
                                mFireAdminStationDrawables.put(1, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        } else {
                            Drawable bitmapDrawable = mFireAdminStationDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(toumingdu, 0xf0, 0xdc, 0x00)));
                                mFireAdminStationDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mFireAdminStationClusterOverlay.setOnClusterClickListener(Main_Activity.this);
                checkDownLoad();
            }
        });
    }

    private ClusterOverlay mFireStationClusterOverlay;
    private SparseArray<Drawable> mFireStationDrawables = new SparseArray<>();

    @Override
    public void onFireStationDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "乡村专职消防队个数为:" + items.size());
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);
                if (mFireStationClusterOverlay != null) {
                    mFireStationClusterOverlay.onDestroy();
                }
                boolean a = (flag & 0x20) == 0x20;
                mFireStationClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), a);
                mFireStationClusterOverlay.setClusterRenderer(new ClusterRender() {
                    @Override
                    public Drawable getDrawAble(int clusterNum) {
                        if (clusterNum == 1) {
                            Drawable bitmapDrawable = mFireStationDrawables.get(1);
                            if (bitmapDrawable == null) {
                                Bitmap src = getBitmap(R.drawable.ic_firestation);
                                bitmapDrawable = new BitmapDrawable(getApplicationContext().getResources(), src);
                                mFireStationDrawables.put(1, bitmapDrawable);
                            }
                            return bitmapDrawable;

                        } else {
                            Drawable bitmapDrawable = mFireStationDrawables.get(2);
                            if (bitmapDrawable == null) {
                                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                                        Color.argb(toumingdu, 0xa9, 0xf0, 0x00)));
                                mFireStationDrawables.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mFireStationClusterOverlay.setOnClusterClickListener(Main_Activity.this);
                checkDownLoad();
            }
        });
    }

    private ClusterOverlay mLicenseClusterOverlay;
    private SparseArray<Drawable> mLicenseDrawablse = new SparseArray<>();

    @Override
    public void onLicenseDataFinish(final List<RegionItem> items) {
        LogUtil.i(TAG, "行政审批项目个数为:" + items.size());
        ThreadManager.getInstance().createLongPool().execute(new Runnable() {
            @Override
            public void run() {
                List<ClusterItem> items2 = new ArrayList<ClusterItem>(items);
                if (mLicenseClusterOverlay != null) {
                    mLicenseClusterOverlay.onDestroy();
                }
                boolean a = (flag & 0x80) == 0x80;

                mLicenseClusterOverlay = new ClusterOverlay(aMap, items2,
                        UiUtils.dip2px(clusterRadius, getApplicationContext()),
                        getApplicationContext(), a);
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
                                        Color.argb(toumingdu, 0x00, 0x78, 0x11)));
                                mLicenseDrawablse.put(2, bitmapDrawable);
                            }
                            return bitmapDrawable;
                        }
                    }
                });
                mLicenseClusterOverlay.setOnClusterClickListener(Main_Activity.this);
                checkDownLoad();
            }
        });
    }

    @Override
    public void onLoadFailure(final int code, final int firetype) {
        new Thread() {
            @Override
            public void run() {
                checkDownLoad();
            }
        }.start();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (code == MarkerHelper.ERROR_FAILED) {
                    String msg = "";
                    msg = "获取" + MarkerHelper.com.get(firetype) + "数据失败";
                    UiUtils.showToast(msg);

                }
            }
        });
    }

    private void dismissDialog() {

        if (getDataDialog.isShowing()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    getDataDialog.dismiss();
                }
            });
        }
    }


    MarkerOptions currentmarkerOptions = new MarkerOptions();

    @Override
    public void onClusterItemClick(Marker marker, final List<ClusterItem> clusterItems) {

        if (clusterItems.size() == 0) {
            if (marker.equals(smartFireMarker)) {
//                marker.showInfoWindow();
                startActivity(new Intent(Main_Activity.this, Smarfire_tActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return;
            }
        }
        if (clusterItems.size() == 1) {
            final String addr = ((RegionItem) clusterItems.get(0)).getAddr();
            String name1 = ((RegionItem) clusterItems.get(0)).getName();

            marker.setTitle(name1);
            marker.showInfoWindow();

            String id = ((RegionItem) clusterItems.get(0)).getId();
            LatLng po = ((RegionItem) clusterItems.get(0)).getPosition();
            int type = ((RegionItem) clusterItems.get(0)).getType();
            LogUtil.e("name=" + name1 + ",addr=" + addr + ",id=" + id + ",position=" + po.toString() + "type=" + type);
            String markerTitle = name1 + "#" + addr;
            String markerSnippet = id + "#" + type + "#" + "1";
//            currentmarkerOptions = new MarkerOptions();
            currentmarkerOptions.title(markerTitle)
                    .snippet(markerSnippet)
                    .position(po);
            name.setText(name1);
            distance = calculateLineDistance(mMyLocation, clusterItems.get(0).getPosition());
            DecimalFormat df = new DecimalFormat("######0.");
            distance = Float.valueOf(df.format(distance));

            showBottom2Menu();
//            UiUtils.showToast("我被点击了");
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
                                        showBottom2Menu();
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

    private void showBottom2Menu() {
        bottomMenu.setVisibility(View.VISIBLE);
        mUiSettings.setLogoBottomMargin(bottomMenu.getHeight() + UiUtils.dip2px(5));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) control.getLayoutParams();
        params.setMargins(UiUtils.dip2px(16), 0, 0, bottomMenu.getHeight() + UiUtils.dip2px(45));
        control.setLayoutParams(params);
//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.support.design.R.anim.abc_slide_in_bottom);
//        bottom2.startAnimation(animation);
    }

    private void closeBottomMenu() {
        mUiSettings.setLogoBottomMargin(UiUtils.dip2px(5));
        if (bottomMenu.getVisibility() == View.VISIBLE) {
            bottomMenu.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) control.getLayoutParams();
            params.setMargins(UiUtils.dip2px(16), 0, 0, UiUtils.dip2px(45));
            control.setLayoutParams(params);
        }
    }


    @Override
    public void onOptionsMenuClosed(Menu menu) {

        super.onOptionsMenuClosed(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        closeDanwei();
        return super.onMenuOpened(featureId, menu);
    }


    private void closeDanwei() {
        if (llMoreDanwei.getVisibility() == View.VISIBLE) {
            llMoreDanwei.setVisibility(View.GONE);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.support.design.R.anim.abc_slide_out_top);
            llMoreDanwei.startAnimation(animation);
            isShowDanweiMenu = false;
        }
    }

    private void openDanwei() {
        if (llMoreDanwei.getVisibility() == View.GONE) {
            llMoreDanwei.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.support.design.R.anim.abc_slide_in_top);
            llMoreDanwei.startAnimation(animation);
            isShowDanweiMenu = true;
        }
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


    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int type = intent.getIntExtra("type", -1);
            if (action.equals("net.suntrans.xiaofang.lp")) {
                closeBottomMenu();
                getData(2);

//                getData(1, type);
            }
        }
    };

    public void refresh(View view) {
        getData(2);
    }


    private int count = 0;
    private final int totalCount = 8;

    private synchronized void checkDownLoad() {
        count++;
        if (count >= totalCount) {
            isRefreshing = false;
            dismissDialog();
        }
        LogUtil.i("mainactivity", "count=" + count);
    }
}
