package com.assignment.googlebooksdemo.data.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    public static String BASE_URL = "https://www.googleapis.com/books/v1/";

    public static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(15, TimeUnit.SECONDS).readTimeout(15,TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).client(okHttpClient).build();
        }
        return retrofit;
    }

}
