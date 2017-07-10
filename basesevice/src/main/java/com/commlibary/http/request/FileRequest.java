package com.commlibary.http.request;

import com.commlibary.http.response.ResponseListener;
import com.commlibary.http.response.ResponseObject;
import com.commlibary.http.okHttp.OkhttpStringRequest;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class FileRequest {
    private BaseRequest request;

    private FileRequest(String url) {
        request = new OkhttpStringRequest();
        request.post(url);
    }

    public static FileRequest create(String url) {
        return new FileRequest(url);
    }

    public FileRequest addParam(String key, String value) {
        request.addParam(key, value);
        return this;
    }

    public FileRequest addHeader(String key, String value) {
        request.addHeader(key, value);
        return this;
    }

    public void addFile(File file) {
        MultipartBody.Builder builder =  new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("fileData", file.getName(), RequestBody.create(MediaType.parse("text/plain"),file));
        request.setMultipartBody(builder.build());

    }

    public void execute(final ResponseListener<String> listener) {
        request.execute().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String str) {
                        ResponseObject<String> response = new ResponseObject<>();
                        response.setContent(str);
                        listener.onResponse(response);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        listener.onError(new Exception(throwable));
                    }
                });
    }
}
