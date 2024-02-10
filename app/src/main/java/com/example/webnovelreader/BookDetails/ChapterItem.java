package com.example.webnovelreader.BookDetails;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ChapterItem implements Parcelable {
    private String chapterName;
    private String chapterUrl;
    private int chapterIndex;
    private String bookName;
    private int isRead;
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public ChapterItem(String chapterName, String chapterUrl, int chapterIndex, String bookName, int isRead) {
        this.chapterName = chapterName;
        this.chapterUrl = chapterUrl;
        this.chapterIndex = chapterIndex;
        this.bookName = bookName;
        this.isRead = isRead;
    }

    protected ChapterItem(Parcel in) {
        chapterName = in.readString();
        chapterUrl = in.readString();
        chapterIndex = in.readInt();
        bookName = in.readString();
    }

    public static final Creator<ChapterItem> CREATOR = new Creator<ChapterItem>() {
        @Override
        public ChapterItem createFromParcel(Parcel in) {
            return new ChapterItem(in);
        }

        @Override
        public ChapterItem[] newArray(int size) {
            return new ChapterItem[size];
        }
    };

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterUrl() {
        return chapterUrl;
    }

    public void setChapterUrl(String chapterUrl) {
        this.chapterUrl = chapterUrl;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(int chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(chapterName);
        dest.writeString(chapterUrl);
        dest.writeInt(chapterIndex);
        dest.writeString(bookName);
    }

    public int isRead() {
        return isRead;
    }

    public void setRead(int read) {
        isRead = read;
    }
}
