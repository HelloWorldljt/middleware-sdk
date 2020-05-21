package com.xiangshang360.middleware.sdk.util;

/**
 * 计算分页参数工具
 *
 * @author chenrg
 * @date 2019/5/10
 */
public class PageUtils {

    /**
     * 计算分页开始索引位置
     *
     * @param page     当前页
     * @param pageSize 每页大小
     * @return 分页开始索引位置，第一页时开始索引为0
     */
    public static int getPageStartIndex(int page, int pageSize) {
        int index = (page - 1) * pageSize;
        return index < 0 ? 0 : index;
    }

    /**
     * 计算总页数
     *
     * @param totalCount 总记录数
     * @param pageSize   每页大小
     * @return 总页数
     * @author chenrg
     * @date 2019/5/10
     */
    public static int getTotalPage(int totalCount, int pageSize) {
        return (int) Math.ceil((double) totalCount / (double) pageSize);
    }
}
