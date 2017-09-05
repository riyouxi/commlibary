package com.commlibary.http.response;

import com.google.gson.annotations.SerializedName;


public class ResponseObject<T> {
    @SerializedName("status")
    private int code;
    private String message;
    private T result;

    /**
     * 返回状态码
     *
     * @return
     */
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    /**
     * 操作成功
     */
    public static final int OK = 0;


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
        return ResponseObject.OK == obj.getCode();
    }
}
