package com.commlibary.http.okHttp;



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
