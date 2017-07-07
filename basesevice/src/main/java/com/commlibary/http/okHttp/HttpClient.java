package com.commlibary.http.okHttp;

import android.app.Activity;
import android.app.Fragment;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class HttpClient {

    //默认超时时间,5s
    private final static int VALUE_DEFAULT_TIME_OUT = 10 * 1000;

    private okhttp3.OkHttpClient client;

    private static final HttpClient instance = new HttpClient();

    private Map<Object,List<Call>> queue = new HashMap<Object,List<Call>>();

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


    /**
     * 将请求添加到请求队列中
     * */
    public void putCall(Object tag, Call call){
        if(tag != null){
            List<Call> calls = queue.get(tag);
            if(calls == null){
                calls = new LinkedList<>();
                calls.add(call);
                queue.put(tag,calls);
            }else{
                calls.add(call);
            }
        }
    }

    /**
     * 将请求取消
     * */
    public void cancelCall(Object tag){
        Object newTag;
        if(tag instanceof Activity){
            Activity activity = (Activity) tag;
            newTag = activity.getClass();
        }else if(tag instanceof Fragment){
            Fragment fragment = (Fragment) tag;
            newTag = fragment.getActivity().getClass();
        }else{
            newTag = tag;
        }
        List<Call> calls = queue.get(tag);
        if(null != calls){
            for(Call call : calls){
                if(!call.isCanceled()){
                    call.cancel();
                }
            }
            calls.remove(newTag);
        }
    }

}
