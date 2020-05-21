package com.xiangshang360.middleware.sdk.timechanger;

import java.util.Date;

/**
 * 时间修改器
 * @author duanz
 * @date 2019-0627
 */
public interface TimeChanger {

    /**
     * 把当前时间设置为
     * @param targetDate 目标时间
     */
    void setCurrentTimeAs(Date targetDate);

    /**
     * 获取当前时间
     * @return
     */
    Date getCurrentTime();

    /**
     * 重置为当前时间
     */
    void reset();
}
