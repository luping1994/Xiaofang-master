package com.suntrans.xiaofang.activity;

import android.content.Intent;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
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
import com.amap.api.maps.CoordinateConverter;
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
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.model.company.CompanyList;
import com.suntrans.xiaofang.model.company.CompanyListResult;
import com.suntrans.xiaofang.network.RetrofitHelper;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.SensorEventHelper;
import com.suntrans.xiaofang.utils.UiUtils;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Main_Activity extends AppCompatActivity implements LocationSource, View.OnClickListener, AMap.OnMarkerClickListener, AMapLocationListener {
    private Toolbar toolbar;
    private MapView mapView = null;
    private LinearLayout rootview;
    private AMap aMap;
    private UiSettings mUiSettings;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private OnLocationChangedListener mListener;
    private SensorEventHelper mSensorHelper;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initMap(savedInstanceState);
    }

    private void initView() {
        rootview = (LinearLayout) findViewById(R.id.rootview);
        spinner = (Spinner) findViewById(R.id.spinner);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("单位总览");
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
                    System.out.println("onCameraChangeFinish:" + cameraPosition.zoom);
                    if (mMyLocation != null)
                        showCompanyMarker();
                }
            });
            aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    LogUtil.i("点击的坐标为:" + latLng.latitude + "," + latLng.longitude);
                    if (currentMarker.getPosition().latitude != latLng.latitude &&
                            currentMarker.getPosition().longitude != latLng.longitude) {
//                        bottom1.setVisibility(View.VISIBLE);
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
                        if (showingType != 0&&CompanyMarkers!=null) {
                            for (Marker marker : CompanyMarkers) {
                                marker.setVisible(true);
                            }
                        }
                        showingType = 0;
                        break;
                    case 1:
                        showingType = 1;
                        UiUtils.showToast(Main_Activity.this, "获取服务器数据失败");
                        if (CompanyMarkers!=null){
                            for (Marker marker : CompanyMarkers) {
                                marker.setVisible(false);
                            }
                        }

                        break;
                    case 2:
                        showingType = 2;
                        UiUtils.showToast(Main_Activity.this, "获取服务器数据失败");
                        break;
                    case 3:
                        showingType = 3;
                        UiUtils.showToast(Main_Activity.this, "获取服务器数据失败");
                        break;

                }
                if (CompanyMarkers != null && showingType != 0) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    /**
     *社会单位makers、markeroptions集合
     */
    ArrayList<Marker> CompanyMarkers;
    ArrayList<MarkerOptions> CompanyMarkerOptions;
//    private ArrayList<Map<String, Object>> datas = new ArrayList<>();
//    private boolean isInit = false;
    private int showingType = 0;//当前展示的单位类型

    private void showCompanyMarker() {
        if (CompanyMarkerOptions == null) {
            return;
        }

        for (int i = 0; i < CompanyMarkerOptions.size(); i++) {
            if (isShow(CompanyMarkerOptions.get(i), mScreenPoint)) {
                boolean a = false;
                for (Marker ma : CompanyMarkers) {
                    if (ma.getOptions().equals(CompanyMarkerOptions.get(i))) {
                        a = true;
                        break;
                    }

                }
                if (!a) {
                    Marker mar = aMap.addMarker(CompanyMarkerOptions.get(i));
                    CompanyMarkers.add(mar);
                    if (showingType == 0)
                        mar.setVisible(true);
                    else
                        mar.setVisible(false);

                }

            } else {

            }
        }
    }

    //获取附近的社会单位位置
    private void getCompanyInfo(LatLng center, String lngFangwei, String latFangwei) {
        CompanyMarkerOptions = new ArrayList<>();
        CompanyMarkers = new ArrayList<>();
        LatLng location = new LatLng(30.547186, 114.342014);
        if (CompanyMarkers.size() < 5)
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
        RetrofitHelper.getApi().getCompanyList(String.valueOf(center.longitude), String.valueOf(center.latitude), "0.01", "0.001").enqueue(new Callback<CompanyListResult>() {
//        RetrofitHelper.getApi().getCompanyList("114.342014", "30.547186", "0.01", "0.001").enqueue(new Callback<CompanyListResult>() {
            @Override
            public void onResponse(Call<CompanyListResult> call, Response<CompanyListResult> response) {
                CompanyListResult result = response.body();
                CoordinateConverter converter = new CoordinateConverter(getApplicationContext());
                converter.from(CoordinateConverter.CoordType.GPS);
                if (result == null) {
                    UiUtils.showToast(Main_Activity.this, "获取服务器单位信息数据失败!");
                    return;
                }
                if (!result.status.equals("0") && result != null) {
                    List<CompanyList> lists = result.results;
                    for (CompanyList info : lists) {
                        if (info.lat == null || info.lng == null) {
                            continue;
                        }
                        HashMap<String, Object> map1 = new HashMap<String, Object>();
                        double lat = Double.valueOf(info.lat);
                        double lng = Double.valueOf(info.lng);
                        LatLng src = new LatLng(lat, lng);
                        map1.put("companyId", info.id);
                        map1.put("lat", src.latitude);
                        map1.put("lng", src.longitude);
                        map1.put("name", info.name);
                        map1.put("address", info.address);
//                        System.out.println("坐标为:" + src.longitude + "," + src.latitude);
//                        datas.add(map1);
                        MarkerOptions marker = new MarkerOptions().position(src).title(info.name + "#" + info.address).snippet(info.id + "#" + S0CIETY + "#" + "1");
                        CompanyMarkerOptions.add(marker);
                    }
                    showCompanyMarker();
                    showingType = 0;
                }
            }

            @Override
            public void onFailure(Call<CompanyListResult> call, Throwable t) {

            }
        });
    }

    private static int S0CIETY = 0;//标识社会单位
    private boolean mFirstFix = false;//是否第一次修展示我的位置图标
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                System.out.println("我当前的坐标为:(" + amapLocation.getLatitude() + ","
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
                UiUtils.showToast(Main_Activity.this, "定位失败,请确认打开GPS/WIFI和网络连接");
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
                    intent1.setClass(Main_Activity.this, CompanyInfo_activity.class);
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
        bottom1.setVisibility(View.GONE);
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
            UiUtils.showToast(this, result);
            Intent intent = new Intent();
            intent.putExtra("url", result);
            intent.setClass(this, CompanyInfo_activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            IntentResult result1 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result1 != null) {
                if (result1.getContents() == null) {

                } else {
                    LogUtil.i(result1.getContents());
                    UiUtils.showToast(this, result1.getContents());
                    Intent intent = new Intent();
                    intent.putExtra("url", result);
                    intent.setClass(this, CompanyInfo_activity.class);
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
                UiUtils.showToast(Main_Activity.this, "再按一次退出");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                } catch (Exception e) {
                }
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

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
}
