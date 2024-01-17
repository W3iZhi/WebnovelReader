package com.example.webnovelreader.Filter;

import java.util.ArrayList;
import java.util.List;

public class FilterGroup {
    private String filterCategory;
    private ArrayList<FilterGroupItem> filterGroupItems;

    public FilterGroup(String filterCategory, ArrayList<FilterGroupItem> filterGroupItems) {
        this.filterCategory = filterCategory;
        this.filterGroupItems = filterGroupItems;
    }

    public String getFilterCategory() {
        return filterCategory;
    }

    public void setFilterCategory(String filterCategory) {
        this.filterCategory = filterCategory;
    }

    public List<FilterGroupItem> getFilterGroupItems() {
        return filterGroupItems;
    }

    public void setFilterGroupItems(ArrayList<FilterGroupItem> filterGroupItems) {
        this.filterGroupItems = filterGroupItems;
    }
}
