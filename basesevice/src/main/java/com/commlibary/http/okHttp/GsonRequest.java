package com.commlibary.http.okHttp;


import com.commlibary.http.BaseRequest;
import com.commlibary.http.Parser;

import rx.Observable;

public interface GsonRequest extends BaseRequest {

    <T,P extends Parser<T>>Observable<T> execute(final P p);
}
