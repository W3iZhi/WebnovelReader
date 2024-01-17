package com.example.webnovelreader.BookReader;

import org.jsoup.select.Elements;

public class ParagraphItem {
    private String paragraph;
    private boolean isTable;
    private boolean isTransition;
    private Elements tableData;

    public ParagraphItem(String paragraph, boolean isTransition) {
        this.paragraph = paragraph;
    }

    public ParagraphItem(String paragraph, boolean isTable, boolean isTransition) {
        this.paragraph = paragraph;
        this.isTable = isTable;
    }

    public ParagraphItem(String paragraph, boolean isTable, Elements tableData, boolean isTransition) {
        this.paragraph = paragraph;
        this.isTable = isTable;
        this.tableData = tableData;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public boolean isTable() {
        return isTable;
    }

    public void setTable(boolean table) {
        isTable = table;
    }

    public Elements getTableData() {
        return tableData;
    }

    public void setTableData(Elements tableData) {
        this.tableData = tableData;
    }

    public boolean isTransition() {
        return isTransition;
    }

    public void setTransition(boolean transition) {
        isTransition = transition;
    }
}
