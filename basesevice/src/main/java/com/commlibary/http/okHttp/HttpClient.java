package com.commlibary.http.okHttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class HttpClient {

    //默认超时时间,5s
    private final static int VALUE_DEFAULT_TIME_OUT = 10 * 1000;

    private okhttp3.OkHttpClient client;

    private static final HttpClient instance = new HttpClient();

    private OkHttpClient gengericClient(){

        OkHttpClient httpClient;
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(VALUE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)//连接超时
                    .readTimeout(VALUE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)//读取超时
                    .writeTimeout(VALUE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)//写入超时
                    .build();
        return httpClient;
    }

    private HttpClient(){
        client = gengericClient();
    }

    public static HttpClient getInstance(){
        return instance;
    }

    public OkHttpClient getHttpClient(){
        return client;
    }

}
