package com.commlibary.http.okHttp;


import com.commlibary.http.Parser;
import com.commlibary.utils.Gsons;
import com.google.gson.Gson;

import rx.Observable;
import rx.functions.Func1;

public class OkHttpGsonRequest extends OkHttpBaseRequest implements GsonRequest {

    private final Gson mGson;

    public OkHttpGsonRequest() {
        mGson = Gsons.gsonBuilder().create();
    }

    @Override
    public <T, P extends Parser<T>> Observable<T> execute(final P p) {
        return execute().flatMap(new Func1<String, Observable<T>>() {
            @Override
            public Observable<T> call(String reponse) {
                return Observable.just(p.parseResponse(reponse));
            }
        });
    }
}
