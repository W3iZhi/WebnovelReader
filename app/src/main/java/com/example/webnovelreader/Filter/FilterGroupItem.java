package com.example.webnovelreader.Filter;

import java.util.logging.Filter;

public class FilterGroupItem {
    private String filterChoice;
    private String filterUrl;
    private int checkState;
    public FilterGroupItem(String filterChoice, String filterUrl, int checkState) {
        this.filterChoice = filterChoice;
        this.filterUrl = filterUrl;
        this.checkState = checkState;
    }
    public FilterGroupItem(String filterChoice, String filterUrl) {
        this.filterChoice = filterChoice;
        this.filterUrl = filterUrl;
    }

    public String getFilterChoice() {
        return filterChoice;
    }

    public void setFilterChoice(String filterChoice) {
        this.filterChoice = filterChoice;
    }

    public String getFilterUrl() {
        return filterUrl;
    }

    public void setFilterUrl(String filterUrl) {
        this.filterUrl = filterUrl;
    }

    public int getCheckState() {
        return checkState;
    }

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }
}
