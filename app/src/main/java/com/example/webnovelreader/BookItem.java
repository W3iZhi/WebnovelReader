package com.example.webnovelreader;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class BookItem implements Parcelable {
    private ArrayList<String> tags;
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

    public BookItem(String imgUrl, String title, String description, String bookUrl,
                    String followers, String views, String words, String chapters,
                    String rating, ArrayList<String> tags) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.description = description;
        this.bookUrl = bookUrl;
        this.followers = followers;
        this.views = views;
        this.words = words;
        this.chapters = chapters;
        this.rating = rating;
        this.tags = tags;
    }

    protected BookItem(Parcel in) {
        tags = in.createStringArrayList();
        imgUrl = in.readString();
        title = in.readString();
        description = in.readString();
        bookUrl = in.readString();
        followers = in.readString();
        views = in.readString();
        words = in.readString();
        chapters = in.readString();
        rating = in.readString();
    }

    public static final Creator<BookItem> CREATOR = new Creator<BookItem>() {
        @Override
        public BookItem createFromParcel(Parcel in) {
            return new BookItem(in);
        }

        @Override
        public BookItem[] newArray(int size) {
            return new BookItem[size];
        }
    };

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeStringList(tags);
        dest.writeString(imgUrl);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(bookUrl);
        dest.writeString(followers);
        dest.writeString(views);
        dest.writeString(words);
        dest.writeString(chapters);
        dest.writeString(rating);
    }
}
