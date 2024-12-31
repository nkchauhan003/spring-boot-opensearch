package com.cb.service;

import java.util.List;

public class SearchResult<T> {
    private List<T> results;
    private long totalRecords;
    private int totalPages;

    public SearchResult(List<T> results, long totalRecords, int totalPages) {
        this.results = results;
        this.totalRecords = totalRecords;
        this.totalPages = totalPages;
    }

    public List<T> getResults() {
        return results;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public int getTotalPages() {
        return totalPages;
    }
}