package com.commlibary.http.request;




import com.commlibary.http.okHttp.OkHttpBaseRequest;

import java.util.Map;

import okhttp3.MultipartBody;
import rx.Observable;

public interface BaseRequest {

    /**
     * 设置http headers
     * @param headers
     * @return
     */
    BaseRequest addHeaders(Map<String,String> headers);

    /**
     * 设置http 参数
     * @param params
     * @return
     */
    BaseRequest addParams(Map<String,String> params);

    /**
     * 设置http header
     * @param key
     * @param value
     * @return
     */
    BaseRequest addHeader(String key, String value);

    /**
     * 设置http 参数
     * @param key
     * @param value
     * @return
     */
    BaseRequest addParam(String key, String value);

    /**
     * get请求
     * @param body
     * @return
     */
    BaseRequest setBody(String body);

    OkHttpBaseRequest setMultipartBody(MultipartBody body);

    /**
     * get请求
     * @param url
     * @return
     */
    BaseRequest get(String url);

    /**
     * post请求
     * @param url
     * @return
     */
    BaseRequest post(String url);


    /**
     * 执行请求
     * @return
     */
    <T extends Parser<T>> Observable<String> execute();

    /**
     * tag
     * */
    BaseRequest tag(Object tag);


}
