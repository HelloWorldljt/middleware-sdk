package com.xiangshang360.middleware.sdk.entity;

import java.util.List;

/**
 * 用于响应可分页的列表数据
 *
 * @author chenrg
 * Created at 2019/7/5 11:31
 **/
public class GlobalResponsePageableEntity<T> extends BaseResponse {

    private static final long serialVersionUID = -1466490772342840410L;

    /**
     * 数据列表
     */
    public List<T> list;

    /**
     * 分页属性
     */
    public Page page;

    public GlobalResponsePageableEntity(){
        super();
    }

    public GlobalResponsePageableEntity(int code, String msg) {
        super(code, msg);
    }

    public GlobalResponsePageableEntity(int code, String msg, List<T> list) {
        super(code, msg);
        this.list = list;
    }

    public GlobalResponsePageableEntity(int code, String msg, List<T> list, Page page) {
        this(code, msg, list);
        this.page = page;
    }

    public static GlobalResponsePageableEntity success(String msg) {
        return new GlobalResponsePageableEntity(HttpStatusCode.SUCCESS, msg);
    }


    public static GlobalResponsePageableEntity success(String msg, List<?> list) {
        return new GlobalResponsePageableEntity(HttpStatusCode.SUCCESS, msg, list);
    }

    public static GlobalResponsePageableEntity success(String msg, List<?> list, Page page) {
        return new GlobalResponsePageableEntity(HttpStatusCode.SUCCESS, msg, list, page);
    }

    public static GlobalResponsePageableEntity failure(String msg) {
        return new GlobalResponsePageableEntity(HttpStatusCode.SERVICE_ERROR, msg);
    }

    public static GlobalResponsePageableEntity failure(int code, String msg) {
        return new GlobalResponsePageableEntity(code, msg);
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public static class Page {
        /**
         * 数据总记录数（指按条件查询的满足条件的记录数）
         */
        public int totalCount;

        /**
         * 数据总页数
         */
        public int totalPage;

        /**
         * 当前页
         */
        public int currentPage;

        /**
         * 每页数据大小
         */
        public int pageSize;

        public Page() {
        }

        public Page(int totalCount, int totalPage, int currentPage, int pageSize) {
            this.totalCount = totalCount;
            this.totalPage = totalPage;
            this.currentPage = currentPage;
            this.pageSize = pageSize;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }
}
