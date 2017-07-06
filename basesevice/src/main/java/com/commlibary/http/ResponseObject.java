package com.commlibary.http;

import com.google.gson.annotations.SerializedName;


public class ResponseObject<T> {
    @SerializedName("status")
    private int status;
    private String message;
    private T content;

    /**
     * 返回状态码
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 返回附加信息
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 返回回复的内容，状态不是成功时可能返回null
     *
     * @return
     */
    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    /**
     * 操作成功
     */
    public static final int OK = 0;

    /**
     * 未知错误[UNKNOWN ERROR]
     */
    public static final int UNKNOWN_ERROR = 100;

    /**
     * 服务器内部错误
     */
    public static final int SERVER_ERROR = 101;

    /**
     * 服务器内部异常
     */
    public static final int SERVER_EXCEPTION = 101;


    /**
     * 判断指定的回复信息是否是成功状态，是成功状态返回true
     *
     * @param obj
     * @return
     */
    public static boolean isOk(ResponseObject<?> obj) {
        if (obj == null) {
            return false;
        }
        return ResponseObject.OK == obj.getStatus();
    }
}
