package com.example.webnovelreader;

public class BookItem {
    private String imgUrl;
    private String title;
    private String description;
    private String bookUrl;
    private String followers;
    private String views;
    private String words;
    private String chapters;
    private String rating;
    public BookItem() {

    }

    public BookItem(String imgUrl, String title, String description, String bookUrl, String followers, String views, String words, String chapters, String rating) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.description = description;
        this.bookUrl = bookUrl;
        this.followers = followers;
        this.views = views;
        this.words = words;
        this.chapters = chapters;
        this.rating = rating;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getChapters() {
        return chapters;
    }

    public void setChapters(String chapters) {
        this.chapters = chapters;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
