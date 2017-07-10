package com.commlibary.http;


import android.content.Context;

import com.commlibary.http.response.ResponseListener;
import com.commlibary.http.response.ResponseObject;

public class LoadingResponseListener<T> implements ResponseListener<T> {

    private ResponseListener<T> mListener;

    private Context mContext;

    private LoadingCustomDialog loadingCustomDialog;

    public  LoadingResponseListener(ResponseListener<T> listener,Context context){
        mListener = listener;
        mContext = context;

    }

    public void showLoadingDialog() {
       LoadingCustomDialog.showprogress(mContext,"loding");

    }

    public void dissmissLoadingDialog(){
        LoadingCustomDialog.dismissprogress();
    }
    @Override
    public void onResponse(ResponseObject reponse) {
        dissmissLoadingDialog();
        mListener.onResponse(reponse);
    }

    @Override
    public void onError(Exception error) {
        dissmissLoadingDialog();
        mListener.onError(error);
    }

    public static <T> ResponseListener<T> ensure(ResponseListener<T> listener, Context fragmentManager) {
        if (listener instanceof LoadingResponseListener) {
            return listener;
        }
        return new LoadingResponseListener<T>(listener, fragmentManager);
    }

}
