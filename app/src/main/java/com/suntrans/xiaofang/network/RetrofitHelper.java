package com.suntrans.xiaofang.network;

import android.util.Log;

import com.suntrans.xiaofang.BaseApplication;
import com.suntrans.xiaofang.service.Api;
import com.suntrans.xiaofang.utils.LogUtil;
import com.suntrans.xiaofang.utils.UiUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Looney on 2016/12/15.
 */

public class RetrofitHelper {

    public static final String BASE_URL = "http://api.91yunpan.com";

    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
    }


    public static Api Login() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }

    public static Api getUserInfoApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);

    };

    public static Api getApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }

    public static Api getApiNoRx(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .build();
        return retrofit.create(Api.class);
    }

    private static void initOkHttpClient() {

        Interceptor netInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String header = BaseApplication.getSharedPreferences().getString("access_token","-1");
                header = "Bearer "+header;
                LogUtil.i(header);
                Request original = chain.request();
                Request request= original.newBuilder()
                        .header("Authorization", header)
                        .method(original.method(), original.body())
                        .build();
                Response response = chain.proceed(request);
//                LogUtil.i("请求头==>"+request.headers().toString());
//                LogUtil.i("响应头==>"+response.headers().toString());
                return response;
            }
        };



        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient==null){
                    mOkHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(netInterceptor)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    static class CacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String header = BaseApplication.getSharedPreferences().getString("access_token","-1");
            header = "Bearer "+header;
            Request request = chain.request();//获取请求
            //这里就是说判读我们的网络条件，要是有网络的话我么就直接获取网络上面的数据，要是没有网络的话我么就去缓存里面取数据
            if(!UiUtils.isNetworkAvailable()){
                request = request.newBuilder()
                        .addHeader("Authorization",header)
                        .method(request.method(), request.body())
                        //这个的话内容有点多啊，大家记住这么写就是只从缓存取，想要了解这个东西我等下在
                        // 给大家写连接吧。大家可以去看下，获取大家去找拦截器资料的时候就可以看到这个方面的东西反正也就是缓存策略。
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Log.d("CacheInterceptor","no network");
            }else {
                request= request.newBuilder()
                        .header("Authorization", header)
                        .method(request.method(), request.body())
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if(UiUtils.isNetworkAvailable()){
                //这里大家看点开源码看看.header .removeHeader做了什么操作很简答，就是的加字段和减字段的。
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        //这里设置的为0就是说不进行缓存，我们也可以设置缓存时间
                        .header("Cache-Control", "public, max-age=" + 0)
                        .removeHeader("Pragma")
                        .build();
            }else{
                int maxTime = 4*24*60*60;
                return originalResponse.newBuilder()
                        //这里的设置的是我们的没有网络的缓存时间，想设置多少就是多少。
                        .header("Cache-Control", "public, only-if-cached, max-stale="+maxTime)
                        .removeHeader("Pragma")
                        .build();
            }
        }

    }

}
