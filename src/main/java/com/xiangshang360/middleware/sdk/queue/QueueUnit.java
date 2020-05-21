package com.xiangshang360.middleware.sdk.queue;

import java.math.BigDecimal;

/**
 * 队列单位
 * @author 段子彧
 * @date 20180810
 */
public class QueueUnit {
    /**
     * id
     */
    private String id;
    /**
     * 队列数据
     */
    private String data;
    /**
     * 优先级
     */
    private Integer priorityNum;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 前方金额
     */
    private BigDecimal beforeAmount;

    /**
     * 进度条
     */
    private Double progress;
    /**
     * 时间戳
     */
    private String timeStamp;

    public QueueUnit() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }




    public QueueUnit(String id, String data, Integer priorityNum,BigDecimal amount) {
        this.id = id;
        this.data = data;
        this.priorityNum = priorityNum;
        this.amount=amount;

    }

    public QueueUnit(String id, String data, Integer priorityNum,BigDecimal amount, Double progress, String timeStamp) {
        this.id = id;
        this.data = data;
        this.priorityNum = priorityNum;
        this.amount=amount;
        this.progress = progress;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getPriorityNum() {
        return priorityNum;
    }

    public void setPriorityNum(Integer priorityNum) {
        this.priorityNum = priorityNum;
    }


    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public BigDecimal getBeforeAmount() {
        return beforeAmount;
    }

    public void setBeforeAmount(BigDecimal beforeAmount) {
        this.beforeAmount = beforeAmount;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "QueueUnit{" +
                "id='" + id + '\'' +
                ", data='" + data + '\'' +
                ", priorityNum=" + priorityNum +
                ", amount=" + amount +
                ", beforeAmount=" + beforeAmount +
                ", progress=" + progress +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
