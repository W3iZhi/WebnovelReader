package com.example.webnovelreader.BookDetails;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.webnovelreader.BookItem;

import java.util.ArrayList;

public class ChaptersDatabase extends SQLiteOpenHelper {
    Context context;
    private static final String DATABASE_NAME = "ChaptersDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public ChaptersDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    private static final String COL_CHAPTER_ID = "chapter_id";
    private static final String COL_CHAPTER_INDEX = "chapter_index";
    private static final String COL_CHAPTER_NAME = "chapter_name";
    private static final String COL_CHAPTER_URL = "chapter_url";
    private static final String COL_IS_READ = "isRead";
    private static final String COL_CHAPTER_DATA = "chapter_data";
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void createChaptersTable(BookItem bookItem) {
        Log.d("Chapters Table", "Creation Called");
        if (containsTable(bookItem.getTitle())) {
            Log.d("Chapters Table", "Already in Table");
            return;
        }
        Log.d("Chapters Table", "Not in Table");
        String bookName = bookItem.getTitle().replaceAll("\\s", "_").replaceAll("\\p{P}", "_");
        String query = "CREATE TABLE " + bookName + " ("
                + COL_CHAPTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_CHAPTER_INDEX + " INTEGER, "
                + COL_CHAPTER_NAME + " TEXT, "
                + COL_CHAPTER_URL + " TEXT, "
                + COL_IS_READ + " TEXT, "
                + COL_CHAPTER_DATA + " TEXT);";
        this.getWritableDatabase().execSQL(query);
    }
    public void createChaptersTable(ChapterItem chapterItem) {
        if (containsTable(chapterItem.getBookName())) {
            return;
        }
        String bookName = chapterItem.getBookName().replaceAll("\\s", "_").replaceAll("\\p{P}", "_");
        String query = "CREATE TABLE " + bookName + " ("
                + COL_CHAPTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_CHAPTER_INDEX + " INTEGER, "
                + COL_CHAPTER_NAME + " TEXT, "
                + COL_CHAPTER_URL + " TEXT, "
                + COL_IS_READ + " INTEGER, "
                + COL_CHAPTER_DATA + " TEXT);";
        this.getWritableDatabase().execSQL(query);
    }
    public boolean containsTable(String bookName) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return false;
        }
        String tableName = bookName.replaceAll("\\s", "_").replaceAll("\\p{P}", "_");
        boolean contains = false;
        String[] args = {"table", tableName};
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type=? AND name=?",args,null);
        Log.d("Chapters Table", "Already contains: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            contains = true;
        }

        cursor.close();
        db.close();
        return contains;
    }
    public boolean containsChapter(ChapterItem chapterItem) {
        String bookName = chapterItem.getBookName().replaceAll("\\s", "_").replaceAll("\\p{P}", "_");
        boolean contains = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + bookName + " where " + COL_CHAPTER_NAME + " =?";
        Cursor cursor = db.rawQuery(query, new String[] {chapterItem.getChapterName()});
        if (cursor.getCount() > 0) {
            contains = true;
        }
        cursor.close();
        db.close();
        return contains;
    }
    public void addChapter(ChapterItem chapterItem) {
        createChaptersTable(chapterItem);
        Log.d("Chapters Database", "Adding: " + chapterItem.getChapterName());
        if (containsChapter(chapterItem)) {
            return;
        }
        String tableName = chapterItem.getBookName().replaceAll("\\s", "_").replaceAll("\\p{P}", "_");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CHAPTER_INDEX, chapterItem.getChapterIndex());
        values.put(COL_CHAPTER_NAME, chapterItem.getChapterName());
        values.put(COL_CHAPTER_URL, chapterItem.getChapterUrl());
        values.put(COL_IS_READ, chapterItem.isRead());
        long success = db.insert(tableName, null, values);
        if (success == -1) {
            Log.d("Chapters Database Insertion", "No Success");
        } else {
            Log.d("Chapters Database Insertion", "Success");
        }
        db.close();
    }
    public void removeChapterTable(BookItem bookItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        String tableName = bookItem.getTitle().replaceAll("\\s", "_").replaceAll("\\p{P}", "_");
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        Log.d("Chapters Database", "Removed: " + bookItem.getTitle());
        db.close();
    }

    public ArrayList<ChapterItem> chaptersList (String bookName) {
        if (!containsTable(bookName)) {
            return null;
        }

        String tableName = bookName.replaceAll("\\s", "_").replaceAll("\\p{P}", "_");
        ArrayList<ChapterItem> chaptersList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + tableName, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String chapterName = cursor.getString(cursor.getColumnIndexOrThrow(COL_CHAPTER_NAME));
                int chapterIndex = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CHAPTER_INDEX));
                String chapterUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_CHAPTER_URL));
                int isRead = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_READ));
                ChapterItem chapterItem = new ChapterItem(chapterName, chapterUrl, chapterIndex, bookName, isRead);
                chaptersList.add(chapterItem);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return chaptersList;
    }
}
