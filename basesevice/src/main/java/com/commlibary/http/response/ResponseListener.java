package com.commlibary.http.response;


public interface ResponseListener<T> {
    void onResponse(ResponseObject<T> reponse);
    void onError(Exception error);
}
