package com.example.webnovelreader.Filter;

public class FilterGroupItem {
    private String filterChoice;
    private String filterUrl;

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
}
