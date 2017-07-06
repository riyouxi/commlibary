package com.commlibary.http;


import com.commlibary.utils.Gsons;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonParser<T> implements Parser<T> {

    private Gson mGson = Gsons.gsonBuilder().create();
    private Type type;

    public GsonParser(Type type){
        this.type = type;
    }
    @Override
    public T parseResponse(String response) {
        return mGson.fromJson(response,type);
    }
}
