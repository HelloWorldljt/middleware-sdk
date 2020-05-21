package com.xiangshang360.middleware.sdk.queue.redis;

/**
 * 刷新进度条行为接口
 * @author 段子彧
 * @date 20180915
 */
public interface RefreshableProgress {
    /**
     * 定时刷新进度
     */
    void refreshProgress();
}
