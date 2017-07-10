package com.commlibary.http.okHttp;


import com.commlibary.http.request.BaseRequest;
import com.commlibary.http.request.Parser;

import rx.Observable;

public interface GsonRequest extends BaseRequest {

    <T,P extends Parser<T>>Observable<T> execute(final P p);
}
