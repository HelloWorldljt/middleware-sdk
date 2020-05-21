package com.xiangshang360.middleware.sdk.entity;

import java.io.Serializable;

/**
 * 统一的异常处理响应对象
 *
 * @author typ
 */
public class GlobalResponseEntity<T> extends BaseResponse {

    private static final long serialVersionUID = -7138912839325217911L;

    private T data;

    public GlobalResponseEntity() {

    }

    public GlobalResponseEntity(int code, String msg, T data) {
        super(code, msg);
        this.data = data;
    }

    /**
     * 响应成功
     *
     * @return
     */
    public static GlobalResponseEntity success() {
        return new GlobalResponseEntity(HttpStatusCode.SUCCESS, "ok", null);
    }

    /**
     * 响应成功，并返回说明文字以及数据
     *
     * @param msg
     * @param data
     * @return
     */
    public static GlobalResponseEntity success(String msg, Object data) {
        return new GlobalResponseEntity(HttpStatusCode.SUCCESS, msg, data);
    }

    /**
     * 响应失败
     *
     * @return
     */
    public static GlobalResponseEntity failure(String msg) {
        return failure(HttpStatusCode.SERVICE_ERROR, msg);
    }

    /**
     * 响应失败，并返回说明文字以及数据
     *
     * @param msg
     * @param data
     * @return
     */
    public static GlobalResponseEntity failure(String msg, Object data) {
        return new GlobalResponseEntity(HttpStatusCode.SERVICE_ERROR, msg, data);
    }

    public static GlobalResponseEntity failure(Integer code, String msg) {
        return new GlobalResponseEntity(code, msg, null);
    }

    /**
     * 处理中
     *
     * @param msg
     * @return
     */
    public static GlobalResponseEntity processing(String msg) {
        return failure(HttpStatusCode.SERVICE_PROCESSING, msg);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
