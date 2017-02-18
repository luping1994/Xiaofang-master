package com.suntrans.xiaofang.activity.mapnav;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStaticInfo;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.NaviStaticInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.R;
import com.suntrans.xiaofang.activity.BasedActivity;
import com.suntrans.xiaofang.model.StrategyBean;
import com.suntrans.xiaofang.utils.UiUtils;
import com.suntrans.xiaofang.utils.Utils;
import com.suntrans.xiaofang.views.WaitDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 驾车路径规划并展示对应的路线标签
 */
public class CalculateRoute_Activity extends BasedActivity implements AMapNaviListener, View.OnClickListener {
    private StrategyBean mStrategyBean;
    private static final float ROUTE_UNSELECTED_TRANSPARENCY = 0.3F;
    private static final float ROUTE_SELECTED_TRANSPARENCY = 1F;
    private ImageView imageViewCar;
    private ImageView imageViewWalk;
    private LinearLayout llCar;
    private LinearLayout llWalk;
    private ProgressDialog dialog1;
    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;
    private MapView mMapView;
    private AMap mAMap;
    private NaviLatLng endLatlng;
    private NaviLatLng startLatlng;
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();
    /*
            * strategyFlag转换出来的值都对应PathPlanningStrategy常量，用户也可以直接传入PathPlanningStrategy常量进行算路。
            * 如:mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList,PathPlanningStrategy.DRIVING_DEFAULT);
            */
    int strategyFlag = 0;

    private Button mStartNaviButton;
    private LinearLayout mRouteLineLayoutOne, mRouteLinelayoutTwo, mRouteLineLayoutThree, backLineLayout,driveLayout;
    private View mRouteViewOne, mRouteViewTwo, mRouteViewThree;
    private TextView mRouteTextStrategyOne, mRouteTextStrategyTwo, mRouteTextStrategyThree;
    private TextView mRouteTextTimeOne, mRouteTextTimeTwo, mRouteTextTimeThree;
    private TextView mRouteTextDistanceOne, mRouteTextDistanceTwo, mRouteTextDistanceThree;
    private TextView mCalculateRouteOverView;
    private ImageView mImageTraffic, mImageStrategy;
    private LatLng from;
    private LatLng to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_route);
        mMapView = (MapView) findViewById(R.id.navi_view);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initView();
        initNavi();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calculate_route_start_navi:
                startNavi();
                break;
            case R.id.route_line_one:
                focuseRouteLine(true, false, false);
                break;
            case R.id.route_line_two:
                focuseRouteLine(false, true, false);
                break;
            case R.id.route_line_three:
                focuseRouteLine(false, false, true);
                break;
            case R.id.map_traffic:
                setTraffic();
                break;
            case R.id.strategy_choose:
                strategyChoose();
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }

    }

    /**
     * 驾车路径规划计算
     */
    private void calculateDriveRoute(int i) {
        dialog1.show();
        try {
            strategyFlag = mAMapNavi.strategyConvert(mStrategyBean.isCongestion(), mStrategyBean.isCost(), mStrategyBean.isAvoidhightspeed(), mStrategyBean.isHightspeed(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (i == 1)
            mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
        else
            mAMapNavi.calculateWalkRoute(startLatlng, endLatlng);
    }

    /**
     * 多路径算路成功回调
     *
     * @param ints 路线id数组
     */
    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        cleanRouteOverlay();
        driveLayout.setVisibility(View.VISIBLE);
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
        setRouteLineTag(paths, ints);
        dialog1.dismiss();
    }


    private RouteOverLay walkOverlay;
    /**
     * 单路径算路成功回调
     */
    @Override
    public void onCalculateRouteSuccess() {
        AMapNaviPath path = mAMapNavi.getNaviPath();
        cleanRouteOverlay();
        mAMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        walkOverlay = new RouteOverLay(mAMap, path, this);
        walkOverlay.setTrafficLine(true);
        walkOverlay.addToMap();
        driveLayout.setVisibility(View.GONE);
        int dis=path.getAllLength();
        String timeCost;
        double time = (double) dis/5000;
        if (time<1){
            timeCost =  (int)(time*60)+"分";
        }else {
            timeCost =  (int)time+"小时";
        }
        mCalculateRouteOverView.setText("距离您"+dis+"米"+" 大约需要"+timeCost);
        dialog1.dismiss();
    }

    /**
     * 接收驾车偏好设置项
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (Utils.ACTIVITY_RESULT_CODE == resultCode) {
//            boolean congestion = data.getBooleanExtra(Utils.INTENT_NAME_AVOID_CONGESTION, false);
//            mStrategyBean.setCongestion(congestion);
//            boolean cost = data.getBooleanExtra(Utils.INTENT_NAME_AVOID_COST, false);
//            mStrategyBean.setCost(cost);
//            boolean avoidhightspeed = data.getBooleanExtra(Utils.INTENT_NAME_AVOID_HIGHSPEED, false);
//            mStrategyBean.setAvoidhightspeed(avoidhightspeed);
//            boolean hightspeed = data.getBooleanExtra(Utils.INTENT_NAME_PRIORITY_HIGHSPEED, false);
//            mStrategyBean.setHightspeed(hightspeed);
//            calculateDriveRoute(1);
//        }
    }

    /**
     * 导航初始化
     */
    private void initNavi() {
        from = ((LatLng) getIntent().getParcelableExtra("from"));
        to = ((LatLng) getIntent().getParcelableExtra("to"));
        mStrategyBean = new StrategyBean(false, false, false, false);
//        LogUtil.i("起始点坐标:" + from.latitude + "终点坐标:" + to.latitude);
        startLatlng = new NaviLatLng(from.latitude, from.longitude);
        endLatlng = new NaviLatLng(to.latitude, to.longitude);

        startList.add(startLatlng);
        endList.add(endLatlng);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        calculateDriveRoute(1);
    }

    private int navState = 1;//1为驾车模式,2为步行模式
    WaitDialog dialog ;
    private void initView() {
        dialog1 = new ProgressDialog(this);
        dialog1.setMessage("计算路径中,请稍候..");
        dialog1.setCancelable(false);
        dialog= new WaitDialog(CalculateRoute_Activity.this,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setWaitText(getString(R.string.loading));
        dialog.setCancelable(false);
        llCar = (LinearLayout) findViewById(R.id.ll_car);
        llWalk = (LinearLayout) findViewById(R.id.ll_walk);
        imageViewCar = (ImageView) findViewById(R.id.car);
        imageViewWalk = (ImageView) findViewById(R.id.walk);
        backLineLayout = (LinearLayout) findViewById(R.id.back);
        backLineLayout.setOnClickListener(this);
        llCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (navState == 2) {
                    dialog1.show();
                    imageViewCar.setImageResource(R.drawable.ic_car_press);
                    imageViewWalk.setImageResource(R.drawable.ic_walk);
                    navState = 1;
                    calculateDriveRoute(navState);
                } else {

                }

            }
        });
        llWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navState == 1) {
                    double distance = AMapUtils.calculateLineDistance(from, to);
                    if (distance>10000){
                        UiUtils.showToast(App.getApplication(),"路途太远建议驾车");
                        return;
                    }
                    dialog1.show();
                    imageViewCar.setImageResource(R.drawable.ic_car);
                    imageViewWalk.setImageResource(R.drawable.ic_walk_press);
                    navState = 2;
                    calculateDriveRoute(navState);
                } else {

                }
            }
        });
        mStartNaviButton = (Button) findViewById(R.id.calculate_route_start_navi);
        mStartNaviButton.setOnClickListener(this);

        mImageTraffic = (ImageView) findViewById(R.id.map_traffic);
        mImageTraffic.setOnClickListener(this);
        mImageStrategy = (ImageView) findViewById(R.id.strategy_choose);
        mImageStrategy.setOnClickListener(this);

        mCalculateRouteOverView = (TextView) findViewById(R.id.calculate_route_navi_overview);

        mRouteLineLayoutOne = (LinearLayout) findViewById(R.id.route_line_one);
        mRouteLineLayoutOne.setOnClickListener(this);
        mRouteLinelayoutTwo = (LinearLayout) findViewById(R.id.route_line_two);
        mRouteLinelayoutTwo.setOnClickListener(this);
        mRouteLineLayoutThree = (LinearLayout) findViewById(R.id.route_line_three);
        mRouteLineLayoutThree.setOnClickListener(this);

        driveLayout= (LinearLayout) findViewById(R.id.calculate_route_strategy_tab);


        mRouteViewOne = (View) findViewById(R.id.route_line_one_view);
        mRouteViewTwo = (View) findViewById(R.id.route_line_two_view);
        mRouteViewThree = (View) findViewById(R.id.route_line_three_view);

        mRouteTextStrategyOne = (TextView) findViewById(R.id.route_line_one_strategy);
        mRouteTextStrategyTwo = (TextView) findViewById(R.id.route_line_two_strategy);
        mRouteTextStrategyThree = (TextView) findViewById(R.id.route_line_three_strategy);

        mRouteTextTimeOne = (TextView) findViewById(R.id.route_line_one_time);
        mRouteTextTimeTwo = (TextView) findViewById(R.id.route_line_two_time);
        mRouteTextTimeThree = (TextView) findViewById(R.id.route_line_three_time);

        mRouteTextDistanceOne = (TextView) findViewById(R.id.route_line_one_distance);
        mRouteTextDistanceTwo = (TextView) findViewById(R.id.route_line_two_distance);
        mRouteTextDistanceThree = (TextView) findViewById(R.id.route_line_three_distance);
    }


    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.setTrafficEnabled(true);
            UiSettings uiSettings = mAMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);
        }
    }

    /**
     * 绘制路径规划结果
     *
     * @param routeId 路径规划线路ID
     * @param path    AMapNaviPath
     */
    private void drawRoutes(int routeId, AMapNaviPath path) {
        mAMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(mAMap, path, this);
        routeOverLay.setTrafficLine(true);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
    }


    /**
     * 开始导航
     */
    private void startNavi() {
      initGPS();
    }

    /**
     * 路线tag选中设置
     *
     * @param lineOne
     * @param lineTwo
     * @param lineThree
     */
    private void focuseRouteLine(boolean lineOne, boolean lineTwo, boolean lineThree) {
        Log.d("LG", "lineOne:" + lineOne + " lineTwo:" + lineTwo + " lineThree:" + lineThree);
        setLinelayoutOne(lineOne);
        setLinelayoutTwo(lineTwo);
        setLinelayoutThree(lineThree);
    }

    /**
     * 地图实时交通开关
     */
    private void setTraffic() {
        if (mAMap.isTrafficEnabled()) {
            mImageTraffic.setImageResource(R.drawable.map_traffic_white);
            mAMap.setTrafficEnabled(false);
        } else {
            mImageTraffic.setImageResource(R.drawable.map_traffic_hl_white);
            mAMap.setTrafficEnabled(true);
        }
    }

    private void cleanRouteOverlay() {
        if (walkOverlay!=null){
            walkOverlay.removeFromMap();
            walkOverlay.destroy();
            walkOverlay =null;
        }

        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            RouteOverLay overlay = routeOverlays.get(key);
            overlay.removeFromMap();
            overlay.destroy();
        }
        routeOverlays.clear();
    }

    /**
     * 跳转到驾车偏好设置页面
     */
    private void strategyChoose() {
//        Intent intent = new Intent(this, StrategyChooseActivity.class);
//        intent.putExtra(Utils.INTENT_NAME_AVOID_CONGESTION, mStrategyBean.isCongestion());
//        intent.putExtra(Utils.INTENT_NAME_AVOID_COST, mStrategyBean.isCost());
//        intent.putExtra(Utils.INTENT_NAME_AVOID_HIGHSPEED, mStrategyBean.isAvoidhightspeed());
//        intent.putExtra(Utils.INTENT_NAME_PRIORITY_HIGHSPEED, mStrategyBean.isHightspeed());
//        startActivityForResult(intent, Utils.START_ACTIVITY_REQUEST_CODE);
    }

    /**
     * @param paths 多路线回调路线
     * @param ints  多路线回调路线ID
     */
    private void setRouteLineTag(HashMap<Integer, AMapNaviPath> paths, int[] ints) {
        if (ints.length < 1) {
            visiableRouteLine(false, false, false);
            return;
        }
        int indexOne = 0;
        String stragegyTagOne = Utils.getStrategyDes(paths, ints, indexOne, mStrategyBean);
        setLinelayoutOneContent(ints[indexOne], stragegyTagOne);
        if (ints.length == 1) {
            visiableRouteLine(true, false, false);
            focuseRouteLine(true, false, false);
            return;
        }

        int indexTwo = 1;
        String stragegyTagTwo = Utils.getStrategyDes(paths, ints, indexTwo, mStrategyBean);
        setLinelayoutTwoContent(ints[indexTwo], stragegyTagTwo);
        if (ints.length == 2) {
            visiableRouteLine(true, true, false);
            focuseRouteLine(true, false, false);
            return;
        }

        int indexThree = 2;
        String stragegyTagThree = Utils.getStrategyDes(paths, ints, indexThree, mStrategyBean);
        setLinelayoutThreeContent(ints[indexThree], stragegyTagThree);
        if (ints.length >= 3) {
            visiableRouteLine(true, true, true);
            focuseRouteLine(true, false, false);
        }

    }

    private void visiableRouteLine(boolean lineOne, boolean lineTwo, boolean lineThree) {
        setLinelayoutOneVisiable(lineOne);
        setLinelayoutTwoVisiable(lineTwo);
        setLinelayoutThreeVisiable(lineThree);
    }

    private void setLinelayoutOneVisiable(boolean visiable) {
        if (visiable) {
            mRouteLineLayoutOne.setVisibility(View.VISIBLE);
        } else {
            mRouteLineLayoutOne.setVisibility(View.GONE);
        }
    }

    private void setLinelayoutTwoVisiable(boolean visiable) {
        if (visiable) {
            mRouteLinelayoutTwo.setVisibility(View.VISIBLE);
        } else {
            mRouteLinelayoutTwo.setVisibility(View.GONE);
        }
    }

    private void setLinelayoutThreeVisiable(boolean visiable) {
        if (visiable) {
            mRouteLineLayoutThree.setVisibility(View.VISIBLE);
        } else {
            mRouteLineLayoutThree.setVisibility(View.GONE);
        }
    }

    /**
     * 设置第一条线路Tab 内容
     *
     * @param routeID  路线ID
     * @param strategy 策略标签
     */
    private void setLinelayoutOneContent(int routeID, String strategy) {
        mRouteLineLayoutOne.setTag(routeID);
        RouteOverLay overlay = routeOverlays.get(routeID);
        overlay.zoomToSpan();
        AMapNaviPath path = overlay.getAMapNaviPath();
        mRouteTextStrategyOne.setText(strategy);
        String timeDes = Utils.getFriendlyTime(path.getAllTime());
        mRouteTextTimeOne.setText(timeDes);
        String disDes = Utils.getFriendlyDistance(path.getAllLength());
        mRouteTextDistanceOne.setText(disDes);
    }

    /**
     * 设置第二条路线Tab 内容
     *
     * @param routeID  路线ID
     * @param strategy 策略标签
     */
    private void setLinelayoutTwoContent(int routeID, String strategy) {
        mRouteLinelayoutTwo.setTag(routeID);
        RouteOverLay overlay = routeOverlays.get(routeID);
        AMapNaviPath path = overlay.getAMapNaviPath();
        mRouteTextStrategyTwo.setText(strategy);
        String timeDes = Utils.getFriendlyTime(path.getAllTime());
        mRouteTextTimeTwo.setText(timeDes);
        String disDes = Utils.getFriendlyDistance(path.getAllLength());
        mRouteTextDistanceTwo.setText(disDes);
    }

    /**
     * 设置第三条路线Tab 内容
     *
     * @param routeID  路线ID
     * @param strategy 策略标签
     */
    private void setLinelayoutThreeContent(int routeID, String strategy) {
        mRouteLineLayoutThree.setTag(routeID);
        RouteOverLay overlay = routeOverlays.get(routeID);
        AMapNaviPath path = overlay.getAMapNaviPath();
        mRouteTextStrategyThree.setText(strategy);
        String timeDes = Utils.getFriendlyTime(path.getAllTime());
        mRouteTextTimeThree.setText(timeDes);
        String disDes = Utils.getFriendlyDistance(path.getAllLength());
        mRouteTextDistanceThree.setText(disDes);
    }

    /**
     * 第一条路线是否focus
     *
     * @param focus focus为true 突出颜色显示，标示为选中状态，为false则标示非选中状态
     */
    private void setLinelayoutOne(boolean focus) {
        if (mRouteLineLayoutOne.getVisibility() != View.VISIBLE) {
            return;
        }
        try {
            int routeID = (int) mRouteLineLayoutOne.getTag();
            RouteOverLay overlay = routeOverlays.get(routeID);
            if (focus) {
                mCalculateRouteOverView.setText(Utils.getRouteOverView(overlay.getAMapNaviPath()));
                mAMapNavi.selectRouteId(routeID);
                overlay.setTransparency(ROUTE_SELECTED_TRANSPARENCY);
                mRouteViewOne.setVisibility(View.VISIBLE);
                mRouteTextStrategyOne.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextTimeOne.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextDistanceOne.setTextColor(getResources().getColor(R.color.colorBlue));
            } else {
                overlay.setTransparency(ROUTE_UNSELECTED_TRANSPARENCY);
                mRouteViewOne.setVisibility(View.INVISIBLE);
                mRouteTextStrategyOne.setTextColor(getResources().getColor(R.color.colorDark));
                mRouteTextTimeOne.setTextColor(getResources().getColor(R.color.colorBlack));
                mRouteTextDistanceOne.setTextColor(getResources().getColor(R.color.colorDark));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第二条路线是否focus
     *
     * @param focus focus为true 突出颜色显示，标示为选中状态，为false则标示非选中状态
     */
    private void setLinelayoutTwo(boolean focus) {
        if (mRouteLinelayoutTwo.getVisibility() != View.VISIBLE) {
            return;
        }
        try {
            int routeID = (int) mRouteLinelayoutTwo.getTag();
            RouteOverLay overlay = routeOverlays.get(routeID);
            if (focus) {
                mCalculateRouteOverView.setText(Utils.getRouteOverView(overlay.getAMapNaviPath()));
                mAMapNavi.selectRouteId(routeID);
                overlay.setTransparency(ROUTE_SELECTED_TRANSPARENCY);
                mRouteViewTwo.setVisibility(View.VISIBLE);
                mRouteTextStrategyTwo.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextTimeTwo.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextDistanceTwo.setTextColor(getResources().getColor(R.color.colorBlue));
            } else {
                overlay.setTransparency(ROUTE_UNSELECTED_TRANSPARENCY);
                mRouteViewTwo.setVisibility(View.INVISIBLE);
                mRouteTextStrategyTwo.setTextColor(getResources().getColor(R.color.colorDark));
                mRouteTextTimeTwo.setTextColor(getResources().getColor(R.color.colorBlack));
                mRouteTextDistanceTwo.setTextColor(getResources().getColor(R.color.colorDark));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第三条路线是否focus
     *
     * @param focus focus为true 突出颜色显示，标示为选中状态，为false则标示非选中状态
     */
    private void setLinelayoutThree(boolean focus) {
        if (mRouteLineLayoutThree.getVisibility() != View.VISIBLE) {
            return;
        }
        try {
            int routeID = (int) mRouteLineLayoutThree.getTag();
            RouteOverLay overlay = routeOverlays.get(routeID);
            if (overlay == null) {
                return;
            }
            if (focus) {
                mCalculateRouteOverView.setText(Utils.getRouteOverView(overlay.getAMapNaviPath()));
                mAMapNavi.selectRouteId(routeID);
                overlay.setTransparency(ROUTE_SELECTED_TRANSPARENCY);
                mRouteViewThree.setVisibility(View.VISIBLE);
                mRouteTextStrategyThree.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextTimeThree.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextDistanceThree.setTextColor(getResources().getColor(R.color.colorBlue));
            } else {
                overlay.setTransparency(ROUTE_UNSELECTED_TRANSPARENCY);
                mRouteViewThree.setVisibility(View.INVISIBLE);
                mRouteTextStrategyThree.setTextColor(getResources().getColor(R.color.colorDark));
                mRouteTextTimeThree.setTextColor(getResources().getColor(R.color.colorBlack));
                mRouteTextDistanceThree.setTextColor(getResources().getColor(R.color.colorDark));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mAMapNavi != null) {
            mAMapNavi.destroy();
        }
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onArriveDestination(NaviStaticInfo naviStaticInfo) {

    }

    @Override
    public void onArriveDestination(AMapNaviStaticInfo aMapNaviStaticInfo) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {
        Toast.makeText(this.getApplicationContext(), "错误码" + i, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }



    private void initGPS() {
        LocationManager locationManager = (LocationManager) this.getSystemService(getApplicationContext().LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(CalculateRoute_Activity.this, "请打开GPS", Toast.LENGTH_SHORT).show();
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("请打开GPS连接");
            dialog.setPositiveButton("设置", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    Toast.makeText(CalculateRoute_Activity.this, "打开后直接点击返回键即可，若不打开返回下次将再次出现", Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                }
            });
            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
        } else {
            Intent gpsintent = new Intent(getApplicationContext(), RouteNavi_Activity.class);
            gpsintent.putExtra("gps", true); // gps 为true为真实导航，为false为模拟导航
            startActivity(gpsintent);
//            searchRouteResult(startPoint, endPoint);//路径规划
            // 弹出Toast
//          Toast.makeText(TrainDetailsActivity.this, "GPS is ready",Toast.LENGTH_LONG).show();
//          // 弹出对话框
//          new AlertDialog.Builder(this).setMessage("GPS is ready").setPositiveButton("OK", null).show();
        }
    }
}
