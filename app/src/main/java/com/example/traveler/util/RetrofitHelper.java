package com.example.traveler.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 2018/7/17.
 */

public class RetrofitHelper {

    private static Retrofit.Builder retrofit;

    private RetrofitHelper() {
        retrofit = new Retrofit.Builder();
    }

    public static Retrofit getInstence() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder();
        }
        final OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(30, TimeUnit.SECONDS).
                readTimeout(30, TimeUnit.SECONDS).
                writeTimeout(30, TimeUnit.SECONDS).build();

        Retrofit mretrofit = retrofit.baseUrl("http://192.168.1.102:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return mretrofit;
    }
}
