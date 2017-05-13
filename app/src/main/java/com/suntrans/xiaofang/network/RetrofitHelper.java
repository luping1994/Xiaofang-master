package com.suntrans.xiaofang.network;

import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.api.Api;
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

//    public static final String BASE_URL = "http://api.91yunpan.com";
//    public static final String BASE_URL = "http://api.whxfwgh.wang";
    public static final String BASE_URL = "http://whxfapp.suntrans-cloud.com";

    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
    }


    public static Api Login() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
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
                String header = App.getSharedPreferences().getString("access_token","-1");
                header = "Bearer "+header;
//                LogUtil.i(header);
                Request original = chain.request();
                Request request= original.newBuilder()
                        .header("Authorization", header)
                        .method(original.method(), original.body())
                        .build();
                Response response = chain.proceed(request);

                return response;
            }
        };

        Interceptor LoggingInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                long t1 = System.nanoTime();
                LogUtil.i(TAG,String.format("Sending request %s on %s%n%s", request.url(),  chain.connection(), request.headers()));
                Response response = chain.proceed(request);
                long t2 = System.nanoTime();
                LogUtil.i(TAG,String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
                return response;
            }
        };

//         Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
//
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                String header = App.getSharedPreferences().getString("access_token","-1");
//                header = "Bearer "+header;
//                Request request = chain.request();
//                if(!UiUtils.isNetworkAvailable()){
//                    request = request.newBuilder()
//                            .cacheControl(CacheControl.FORCE_CACHE)
//                            .build();
//                    LogUtil.w(TAG,"no network");
//                }
//                Response originalResponse = chain.proceed(request);
//                if(UiUtils.isNetworkAvailable()){
//                    //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
//                    String cacheControl = request.cacheControl().toString();
//                    return originalResponse.newBuilder()
//                            .header("Cache-Control", cacheControl)
//                            .header("Authorization", header)
//                            .removeHeader("Pragma")
//                            .build();
//                }else{
//                    return originalResponse.newBuilder()
//                            .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
//                            .removeHeader("Pragma")
//                            .build();
//                }
//            }
//        };

        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient==null){
                    mOkHttpClient = new OkHttpClient.Builder()
//                            .addInterceptor(LoggingInterceptor)
                            .addInterceptor(netInterceptor)
//                            .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                            .connectTimeout(8, TimeUnit.SECONDS)
                            .build();
                }
            }
        }



    }



   static final String TAG = "RetrofitHelper";

}
