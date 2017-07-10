package com.commlibary.http.okHttp;


import android.app.Activity;
import android.app.Fragment;

import com.commlibary.http.request.BaseRequest;
import com.commlibary.http.request.Parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

public class OkHttpBaseRequest implements BaseRequest {

    protected HttpClient mClient;
    protected Map<String,String> mHeaders;
    protected Map<String,String> mParams;
    protected String mBody;
    protected String mMethod;
    protected String mUrl;
    protected MultipartBody multipartBody;
    protected Object tag;


    public OkHttpBaseRequest(){
        mClient = HttpClient.getInstance();
        mHeaders = new HashMap<>();
        mParams = new HashMap<>();
    }

    @Override
    public OkHttpBaseRequest addHeaders(Map<String, String> headers) {
        mHeaders.putAll(headers);
        return this;
    }

    @Override
    public OkHttpBaseRequest addParams(Map<String, String> params) {
        mParams.putAll(params);
        return this;
    }

    @Override
    public OkHttpBaseRequest addHeader(String key, String value) {
        mHeaders.put(key,value);
        return this;
    }

    @Override
    public OkHttpBaseRequest addParam(String key, String value) {
        mParams.put(key,value);
        return this;
    }

    @Override
    public OkHttpBaseRequest setBody(String body) {
        mBody = body;
        return this;
    }

    @Override
    public OkHttpBaseRequest setMultipartBody(MultipartBody body) {
        multipartBody = body;
        return this;
    }

    @Override
    public OkHttpBaseRequest get(String url) {
        mUrl = url;
        mMethod = "GET";
        return this;
    }

    @Override
    public OkHttpBaseRequest post(String url) {
        mUrl = url;
        mMethod = "POST";
        return this;
    }

    @Override
    public <T extends Parser<T>> Observable<String> execute() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Request.Builder requestBuilder = new Request.Builder().url(mUrl);
                if(mMethod.equals("POST")){
                    if(multipartBody!=null){
                        requestBuilder.method(mMethod,multipartBody);
                    }else{
                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),mBody);
                        requestBuilder.method(mMethod,requestBody);
                    }
                }
                Request request = requestBuilder.build();
                try {
                    Response response = mClient.getHttpClient().newCall(request).execute();
                    if(!response.isSuccessful()){
                        subscriber.onError(new IOException("Unexpected code:" +response));
                    }
                    subscriber.onNext(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public BaseRequest tag(Object tag) {
        if(tag instanceof Activity){
            Activity activity = (Activity) tag;
            this.tag = activity.getClass();
        }else if(tag instanceof Fragment){
            Fragment fragment = (Fragment) tag;
            this.tag = fragment.getActivity().getClass();
        }else{
            this.tag = tag;
        }
        return this;
    }
}
