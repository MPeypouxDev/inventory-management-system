package com.stockmanager.util;

import java.util.List;

public class Pagination<T> {

    private int currentPage = 0;
    private final int pageSize;

    public Pagination(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getPage(List<T> allItems) {
        int fromIndex = currentPage * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allItems.size());
        return allItems.subList(fromIndex, toIndex);
    }

    public int getTotalPages(List<T> allItems) {
        if (allItems.isEmpty()) return 1;
        return (int) Math.ceil((double) allItems.size() / pageSize);
    }

    public void nextPage(List<T> allItems) {
        if (currentPage < getTotalPages(allItems) - 1) currentPage++;
    }

    public void previousPage() {
        if (currentPage > 0) currentPage--;
    }

    public int getCurrentPage() { return currentPage; }

    public boolean hasNext(List<T> allItems) {
        return currentPage < getTotalPages(allItems) - 1;
    }

    public boolean hasPrevious() { return currentPage > 0; }
}
