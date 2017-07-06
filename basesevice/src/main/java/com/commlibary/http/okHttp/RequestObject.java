package com.commlibary.http.okHttp;


/**
 * Author：Liuyang on 16/9/5.
 * Mail：liuy04@shishike.com
 */

public class RequestObject<T> {
    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public static <T> RequestObject<T> create(T content) {
        RequestObject<T> request = new RequestObject<T>();
        request.setContent(content);
        return request;
    }
}
