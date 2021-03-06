package com.suntrans.xiaofang.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStep;
import com.google.gson.JsonObject;
import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.model.StrategyBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    private static DecimalFormat fnum = new DecimalFormat("##0.0");
    public static final int AVOID_CONGESTION = 4;  // 躲避拥堵
    public static final int AVOID_COST = 5;  // 避免收费
    public static final int AVOID_HIGHSPEED = 6; //不走高速
    public static final int PRIORITY_HIGHSPEED = 7; //高速优先

    public static final int START_ACTIVITY_REQUEST_CODE = 1;
    public static final int ACTIVITY_RESULT_CODE = 2;

    public static final String INTENT_NAME_AVOID_CONGESTION = "AVOID_CONGESTION";
    public static final String INTENT_NAME_AVOID_COST = "AVOID_COST";
    public static final String INTENT_NAME_AVOID_HIGHSPEED = "AVOID_HIGHSPEED";
    public static final String INTENT_NAME_PRIORITY_HIGHSPEED = "PRIORITY_HIGHSPEED";


    public static String getFriendlyTime(int s) {
        String timeDes = "";
        int h = s / 3600;
        if (h > 0) {
            timeDes += h + "小时";
        }
        int min = (int) (s % 3600) / 60;
        if (min > 0) {
            timeDes += min + "分";
        }
        return timeDes;
    }

    public static String getFriendlyDistance(int m) {
        float dis = m / 1000f;
        String disDes = fnum.format(dis) + "公里";
        return disDes;
    }

    /**
     * 计算path对应的标签
     *
     * @param paths        多路径规划回调的所有路径
     * @param ints         多路径线路ID
     * @param pathIndex    当前路径索引
     * @param strategyBean 封装策略bean
     * @return path对应标签描述
     */
    public static String getStrategyDes(HashMap<Integer, AMapNaviPath> paths, int[] ints, int pathIndex, StrategyBean strategyBean) {
        int StrategyTAGIndex = pathIndex + 1;
        String StrategyTAG = "方案" + StrategyTAGIndex;

        int minTime = Integer.MAX_VALUE;
        int minDistance = Integer.MAX_VALUE;
        int minTrafficLightNumber = Integer.MAX_VALUE;
        int minCost = Integer.MAX_VALUE;
        for (int i = 0; i < ints.length; i++) {
            if (pathIndex == i) {
                continue;
            }
            AMapNaviPath path = paths.get(ints[i]);
            if (path == null) {
                continue;
            }
            int trafficListNumber = getTrafficNumber(path);
            if (trafficListNumber < minTrafficLightNumber) {
                minTrafficLightNumber = trafficListNumber;
            }

            if (path.getTollCost() < minCost) {
                minCost = path.getTollCost();
            }

            if (path.getAllTime() < minTime) {
                minTime = path.getAllTime();
            }
            if (path.getAllLength() < minDistance) {
                minDistance = path.getAllLength();
            }
        }
        AMapNaviPath indexPath = paths.get(ints[pathIndex]);
        int indexTrafficLightNumber = getTrafficNumber(indexPath);
        int indexCost = indexPath.getTollCost();
        int indexTime = indexPath.getAllTime();
        int indexDistance = indexPath.getAllLength();
        if (indexTrafficLightNumber < minTrafficLightNumber) {
            StrategyTAG = "红绿灯少";
        }
        if (indexCost < minCost) {
            StrategyTAG = "收费较少";
        }

        if (Math.round(indexDistance / 100f) < Math.round(minDistance / 100f)) {   // 展示距离精确到千米保留一位小数，比较时按照展示数据处理
            StrategyTAG = "距离最短";
        }
        if (indexTime / 60 < minTime / 60) {    //展示时间精确到分钟，比较时按照展示数据处理
            StrategyTAG = "时间最短";
        }
        boolean isMulti = isMultiStrategy(strategyBean);
        if (strategyBean.isCongestion() && pathIndex == 0 && !isMulti) {
            StrategyTAG = "躲避拥堵";
        }
        if (strategyBean.isAvoidhightspeed() && pathIndex == 0 && !isMulti) {
            StrategyTAG = "不走高速";
        }
        if (strategyBean.isCost() && pathIndex == 0 && !isMulti) {
            StrategyTAG = "避免收费";
        }
        if (pathIndex == 0 && StrategyTAG.startsWith("方案")) {
            StrategyTAG = "推荐";
        }
        return StrategyTAG;
    }


    /**
     * 判断驾车偏好设置是否同时选中多个策略
     *
     * @param strategyBean 驾车策略bean
     * @return
     */
    public static boolean isMultiStrategy(StrategyBean strategyBean) {
        boolean isMultiStrategy = false;
        if (strategyBean.isCongestion()) {
            if (strategyBean.isAvoidhightspeed() || strategyBean.isCost() || strategyBean.isHightspeed()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        if (strategyBean.isCost()) {
            if (strategyBean.isCongestion() || strategyBean.isAvoidhightspeed()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        if (strategyBean.isAvoidhightspeed()) {
            if (strategyBean.isCongestion() || strategyBean.isCost()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        if (strategyBean.isHightspeed()) {
            if (strategyBean.isCongestion()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        return isMultiStrategy;
    }

    public static Spanned getRouteOverView(AMapNaviPath path) {
        String routeOverView = "";
        if (path == null) {
            Html.fromHtml(routeOverView);
        }

        int cost = path.getTollCost();
        if (cost > 0) {
            routeOverView += "过路费约<font color=\"red\" >" + cost + "</font>元";
        }
        int trafficLightNumber = getTrafficNumber(path);
        if (trafficLightNumber > 0) {
            routeOverView += "红绿灯" + trafficLightNumber + "个";
        }
        return Html.fromHtml(routeOverView);
    }

    public static int getTrafficNumber(AMapNaviPath path) {
        int trafficLightNumber = 0;
        if (path == null) {
            return trafficLightNumber;
        }
        List<AMapNaviStep> steps = path.getSteps();
        for (AMapNaviStep step : steps) {
            trafficLightNumber += step.getTrafficLightNumber();
        }
        return trafficLightNumber;

    }

    public static void setStateBarTranslucent(Activity activity) {
        Window window =activity. getWindow();
        WindowManager.LayoutParams layoutParams =window.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        layoutParams.flags|=bits;
        window.setAttributes(layoutParams);
    }

    ///判断网络是否可用，如果连接到了网络就返回true（无论是否真正可以上网），如果没有连接，则返回false。表示无可用网络
    public static boolean IsNetWork()
    {
        Context context = App.getApplication();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    //System.out.println(i + "===状态===" + networkInfo[i].getState());
                    //System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isVaild(String value){
        if (value != null) {
            value = value.replace(" ", "");
            if (!TextUtils.equals("", value))
                return true;
        }
        return false;
    }

    public static String parseJson(String s){
        StringBuilder sb =  new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray names = jsonObject.names();
            if (names.length()!=0){
                for (int i=0;i<names.length();i++){
                    sb.append(names.getString(i))
                            .append(":")
                            .append(jsonObject.getString(names.getString(i)))
                            .append("\n");
                }
                return sb.toString();
            }else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
