package com.xiangshang360.middleware.sdk.entity;

import java.io.Serializable;

/**
 * 响应基础信息
 *
 * @author chenrg
 * Created at 2019/7/5 11:39
 **/
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = -8646072981344283047L;

    private int code;

    private String msg;

    public BaseResponse() {
    }

    public BaseResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean ok() {
        return this.code == HttpStatusCode.SUCCESS;
    }

    public boolean processing() {
        return this.code == HttpStatusCode.SERVICE_PROCESSING;
    }

    public boolean failed() {
        return !ok() && !processing();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
