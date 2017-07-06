package com.commlibary.http;


public interface Parser<T> {

    T parseResponse(String response);
}
