package com.commlibary.http;


import android.animation.TypeEvaluator;
import android.text.AndroidCharacter;

import com.commlibary.http.okHttp.GsonRequest;
import com.commlibary.http.okHttp.OkHttpGsonRequest;
import com.commlibary.http.okHttp.RequestObject;
import com.commlibary.utils.Gsons;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class OpsRequest<Q,E> {

    protected GsonRequest request;
    protected Gson mGson;
    protected Q mRequestContent;
    protected Type mResponseType;


    protected interface Method{
        int DEPREACATED_GET_OR_POST =-1;
        int GET = 0;
        int POST = 1;
    }
    protected OpsRequest(String url,int method){
        request = new OkHttpGsonRequest();
        mGson = Gsons.gsonBuilder().create();
        switch (method){
            case Method.GET:
                request.get(url);
                break;
            case Method.POST:
                request.post(url);
                break;
            default:
                request.get(url);
                break;
        }
    }

    public static <Q,E> OpsRequest<Q,E> createGet(String url){
        return new OpsRequest<>(url,Method.GET);
    }

    public static <Q,E> OpsRequest<Q,E> createPost(String url){
        return new OpsRequest<>(url,Method.POST);
    }

    public OpsRequest<Q,E> requestValue(Q requestContent){
        this.mRequestContent = requestContent;
        request.setBody(mGson.toJson(RequestObject.create(this.mRequestContent)));
        return this;
    }

    public OpsRequest<Q,E> responseType(Type responseContetType){
        this.mResponseType = responseContetType;
        return this;
    }

    public OpsRequest<Q,E> responseClass(Class<E> responseContentClass){
        return responseType(getResponseType(Type.class.cast(responseContentClass)));
    }

    public OpsRequest<Q,E> addParam(String key,String value){
        request.addParam(key,value);
        return this;
    }

    public void execute(final ResponseListener<E> listener,Object tag){
        request.execute(new GsonParser<ResponseObject<E>>(mResponseType))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResponseObject<E>>() {
            @Override
            public void call(ResponseObject<E> eResponseObject) {
                listener.onResponse(eResponseObject);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                listener.onError(new Exception(throwable));
            }
        });
    }

    /**
     * 获取指定Content类型的ResponseObject对象的类型
     *
     * @param contentType
     * @return
     */
    public static Type getResponseType(Type contentType) {
        return com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null,
                Type.class.cast(ResponseObject.class),
                contentType);
    }

    /**
     * 获取当ResponseObject的content是List<?>时的Type
     *
     * @param classType content为List时列表元素的对象类型
     * @return
     */
    public static <T> Type getListContentResponseType(Class<T> classType) {
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, List.class, classType);
        return getResponseType(type);
    }

    static <Q, E> Type getContentResponseType(Class<Q> rawType, Class<E> classType){
        Type type = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, rawType, classType);
        return getResponseType(type);
    }



}
