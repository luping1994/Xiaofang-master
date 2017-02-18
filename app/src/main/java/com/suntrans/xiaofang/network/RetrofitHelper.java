package com.suntrans.xiaofang.network;

import com.suntrans.xiaofang.App;
import com.suntrans.xiaofang.api.Api;
import com.suntrans.xiaofang.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
                LogUtil.i(header);
                Request original = chain.request();
                Request request= original.newBuilder()
                        .header("Authorization", header)
                        .method(original.method(), original.body())
                        .build();
                Response response = chain.proceed(request);
//                InputStream stream = response.body().byteStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//                StringBuilder sb = new StringBuilder();
//                String line="";
//                while ((line = reader.readLine())!=null){
//                    sb.append(line);
//                }
//                System.out.println(sb.toString());
                return response;
            }
        };



        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient==null){
                    mOkHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(netInterceptor)
                            .connectTimeout(8, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

}
