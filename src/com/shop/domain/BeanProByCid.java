package com.shop.domain;

import java.util.List;

public class BeanProByCid <T>{

    private int currentPage;
    private int currentCount;
    private int totalCount;
    private int totalPage;

    @Override
    public String toString() {
        return "BeanProByCid{" +
                "currentPage=" + currentPage +
                ", currentCount=" + currentCount +
                ", totalCount=" + totalCount +
                ", totalPage=" + totalPage +
                ", list=" + list +
                '}';
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
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

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    private List<T> list;
}
