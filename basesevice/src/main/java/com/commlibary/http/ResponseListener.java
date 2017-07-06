package com.commlibary.http;


public interface ResponseListener<T> {
    void onResponse(ResponseObject<T> reponse);
    void onError(Exception error);
}
