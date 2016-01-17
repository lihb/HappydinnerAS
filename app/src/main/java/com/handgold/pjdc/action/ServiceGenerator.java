package com.handgold.pjdc.action;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * 所有服务器协议的默认的统一的生成类。
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = "http://120.55.162.6/api/";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(SingleOkHttpClient.getInstance())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 这句一定要加
                    .addConverterFactory(GsonConverterFactory.create());

    public static <T> T createService(Class<T> serviceClass) {

        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
