package com.xiangshang360.middleware.sdk.entity;

import java.io.Serializable;

/****
 @description 幂等性校验请求体
 @author tanyuanpeng
 @date 2018/12/12    17:32 
 ****/
public class IdempotentReqBody<T> implements Serializable {

    private static final long serialVersionUID = -2036334287053364946L;

    /**
     * 序列号
     */
    private String serialNumber;

    private T data;
    /**
     * 透传参数，可以为json，被调用方不做处理，直接原样返回
     */
    private String reserveMsg;

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getReserveMsg() {
        return reserveMsg;
    }

    public void setReserveMsg(String reserveMsg) {
        this.reserveMsg = reserveMsg;
    }

    @Override
    public String toString() {
        return "IdempotentReqBody{" +
                "serialNumber='" + serialNumber + '\'' +
                ", data=" + data +
                ", reserveMsg='" + reserveMsg + '\'' +
                '}';
    }
}
