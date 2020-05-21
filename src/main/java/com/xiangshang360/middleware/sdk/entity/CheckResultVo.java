package com.xiangshang360.middleware.sdk.entity;

import java.io.Serializable;

/**
 * 检查结果统一处理对象，用于不能使用断言的场景
 * 1.需要根据检查结果来更新数据状态以及失败原因的场景
 * @author tanyuanpeng
 */
public class CheckResultVo implements Serializable {

    /**
     * 是否检查成功
     */
    private boolean success;

    /**
     * 检查失败原因/备注
     */
    private String remark;

    public CheckResultVo(){}

    public CheckResultVo(boolean success, String remark) {
        this.success = success;
        this.remark = remark;
    }

    /**
     * 处理成功
     * @return
     */
    public static CheckResultVo success(){
        return new CheckResultVo(true,"成功");
    }

    /**
     * 处理失败
     * @param remark
     * @return
     */
    public static CheckResultVo fail(String remark){
        return new CheckResultVo(false,remark);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
