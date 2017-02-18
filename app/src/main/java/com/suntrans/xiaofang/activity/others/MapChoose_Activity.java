package com.suntrans.xiaofang.activity.others;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.adapter.SearchResultAdapter;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.suntrans.xiaofang.views.SegmentedGroup;

import java.util.ArrayList;
import java.util.List;

public class MapChoose_Activity extends AppCompatActivity implements LocationSource,
        AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener, PoiSearch.OnPoiSearchListener { // Inputtips.InputtipsListener


//    private ListView listView;
    //    private SegmentedGroup mSegmentedGroup;
//    private AutoCompleteTextView searchText;
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private String[] items = {"公司企业", "政府机构及社会团体", "学校", "商场"};

    private RecyclerView recyclerView;

    private Marker locationMarker;

    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;

    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private List<PoiItem> poiItems;// poi数据

    private String searchType = items[0];
    private String searchKey = "";
    private LatLonPoint searchLatlonPoint;

    private List<PoiItem> resultData;

    private SearchResultAdapter searchResultAdapter;

    private boolean isItemClickAction;


    private Toolbar toolbar;
    private LinearLayout llList;
    private ActionBar actionBar;
    private SearchView searchView;
    private MenuItem menuItem;
    private TextView title;
    private TextView subtitle;

    private PoiItem targetPoiItem;
    private LatLonPoint targetPoint;
    private MenuItem tijiaoItem;
    private ProgressDialog dialog;

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_choose);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();

        initView();

        resultData = new ArrayList<>();

    }

    private void initView() {

//        mSearchView = (SearchView) findViewById(R.id.searchView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("选择地址");
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

        llList = (LinearLayout) findViewById(R.id.ll_list);
        title = (TextView) findViewById(R.id.text_title);
        listView = (ListView) findViewById(R.id.listview);
        searchResultAdapter = new SearchResultAdapter(this);
        listView.setAdapter(searchResultAdapter);
        listView.setOnItemClickListener(onItemClickListener);

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        progDialog = new ProgressDialog(this);

//        hideSoftKey(searchText);
    }


    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                if (!isItemClickAction && !isInputKeySearch) {
                targetPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
                LogUtil.i(cameraPosition.target.toString());
                geoAddress(cameraPosition.target);
                startJumpAnimation();
//                }
//                targetPoiItem = new PoiItem()
//                searchLatlonPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
//                isInputKeySearch = false;
//                isItemClickAction = false;
            }
        });

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter(null);
            }
        });
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
//        Log.i("MY", "onLocationChanged");
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);

                LatLng curLatlng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());

                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));

                searchLatlonPoint = new LatLonPoint(curLatlng.latitude, curLatlng.longitude);
                targetPoint = new LatLonPoint(curLatlng.latitude, curLatlng.longitude);


                isInputKeySearch = false;


            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setOnceLocation(true);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    /**
     * 响应逆地理编码
     */
    public void geoAddress(LatLng searchKey) {
//        Log.i("MY", "geoAddress"+ searchLatlonPoint.toString());
        showDialog();
        LatLonPoint target = new LatLonPoint(searchKey.latitude, searchKey.longitude);
        RegeocodeQuery query = new RegeocodeQuery(target, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);
    }

    /**
     * 开始进行poi搜索
     */
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在搜索,请稍后..");
        dialog.setCancelable(false);
        dialog.show();
//        Log.i("MY", "doSearchQuery");
        currentPage = 0;
        query = new PoiSearch.Query(searchKey, "", "武汉");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setCityLimit(true);
        query.setPageSize(20);
        query.setPageNum(currentPage);

        if (searchLatlonPoint != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
//            poiSearch.setBound(new PoiSearch.SearchBound(searchLatlonPoint, 1000000, true));//
            poiSearch.searchPOIAsyn();
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                RegeocodeAddress addrs = result.getRegeocodeAddress();
                String address = addrs.getProvince() + addrs.getCity() + addrs.getDistrict() + addrs.getTownship()
                        + addrs.getStreetNumber().getStreet() + addrs.getStreetNumber().getNumber();
//                firstItem = new PoiItem("regeo", searchLatlonPoint, address, address);
                targetPoiItem = new PoiItem("regeo", targetPoint, address, address);
                LogUtil.i("查找结果:" + address);
                title.setText(address);
            }
        } else {
            Toast.makeText(MapChoose_Activity.this, "error code is " + rCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * POI搜索结果回调
     *
     * @param poiResult  搜索结果
     * @param resultCode 错误码
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int resultCode) {
        dialog.dismiss();
        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                if (poiResult.getQuery().equals(query)) {
                    poiItems = poiResult.getPois();
                    if (poiItems != null && poiItems.size() > 0) {
                        updateListview(poiItems);
                    } else {
                        UiUtils.showToast("无搜索结果");
                    }
                }
            } else {
                UiUtils.showToast("无搜索结果");
            }
        }
    }

    /**
     * 更新列表中的item
     *
     * @param poiItems
     */
    private void updateListview(List<PoiItem> poiItems) {
        LogUtil.i("MapChooseActivity","更新列表");
        resultData.clear();
        resultData.addAll(poiItems);
        searchResultAdapter.setData(resultData);
        searchResultAdapter.notifyDataSetChanged();
    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            if (position != searchResultAdapter.getSelectedPosition()) {
            PoiItem poiItem = (PoiItem) searchResultAdapter.getItem(position);
            LatLng curLatlng = new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude());
//                isItemClickAction = true;
//                searchView.onActionViewCollapsed();
            menuItem.collapseActionView();
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16f));

            searchResultAdapter.setSelectedPosition(position);
            searchResultAdapter.notifyDataSetChanged();


//            }
        }
    };


    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在加载...");
        progDialog.show();
    }

    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    private void addMarkerInScreenCenter(LatLng locationLatLng) {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        locationMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));
        //设置Marker在屏幕上,不跟随地图移动
        locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);

    }

    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {

        if (locationMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = locationMarker.getPosition();
            Point point = aMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(this, 125);
            LatLng target = aMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            locationMarker.setAnimation(animation);
            //开始动画
            locationMarker.startAnimation();

        } else {
            Log.e("ama", "screenMarker is null");
        }
    }

    //dip和px转换
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    private boolean isInputKeySearch;

    //    private String inputSearchKey;
    private void searchPoi(String result) {
        isInputKeySearch = true;
        searchKey = result;
//        inputSearchKey = result;//getAddress(); // + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict() + result.getRegeocodeAddress().getTownship();
//        searchLatlonPoint = result.getPoint();
//        firstItem = new PoiItem("tip", searchLatlonPoint, inputSearchKey, result.getAddress());
//        firstItem.setCityName(result.getDistrict());
        resultData.clear();
//        searchResultAdapter.setSelectedPosition(0);

//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(searchLatlonPoint.getLatitude(), searchLatlonPoint.getLongitude()), 16f));

//        hideSoftKey(searchText);
        doSearchQuery();
    }

    private void hideSoftKey(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    boolean isSearching = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_mapchoose, menu);
        //在菜单中找到对应控件的item
        menuItem = menu.findItem(R.id.search);

        tijiaoItem = menu.findItem(R.id.tijiao);
//        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menuItem.getActionView();
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
//        }
        searchView.setSubmitButtonEnabled(false);//设置是否显示搜索按钮
        searchView.setQueryHint("查找地点");//设置提示信息
        searchView.setIconifiedByDefault(true);//设置搜索默认为图标
//        searchView.setBackgroundColor(Color.parseColor("#FF4081"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String newText = query.toString().trim();
                searchPoi(newText);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                llList.setVisibility(View.VISIBLE);
                isSearching = true;
                tijiaoItem.setTitle("搜索");
                LogUtil.i("MapChooseActivity", "listView.setVisibility(View.VISIBLE)");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                isSearching = false;
                tijiaoItem.setTitle("提交");

                resultData.clear();
                searchResultAdapter.setData(resultData);
                searchResultAdapter.notifyDataSetChanged();
                llList.setVisibility(View.INVISIBLE);
                LogUtil.i("MapChooseActivity", "listView.setVisibility(View.INVISIBLE)");

                return true;
            }
        });
        return true; // false
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.tijiao:
                if (isSearching) {
                    String query= searchView.getQuery().toString();
                    if (!Utils.isVaild(query)){

                        return true;
                    }
                    searchPoi(query);
                } else {
                    Intent data = new Intent();
                    data.putExtra("addrinfo", targetPoiItem);
                    if (targetPoiItem != null)
                        setResult(RESULT_OK, data);
                    finish();
                }

//                mSearchView.open(true, item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
