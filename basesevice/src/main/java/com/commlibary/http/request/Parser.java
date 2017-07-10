package com.commlibary.http.request;


public interface Parser<T> {

    T parseResponse(String response);
}
