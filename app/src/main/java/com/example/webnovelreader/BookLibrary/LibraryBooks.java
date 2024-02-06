package com.example.webnovelreader.BookLibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.webnovelreader.BookItem;
import com.example.webnovelreader.PicassoDownload;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;

public class LibraryBooks extends SQLiteOpenHelper {
    Context context;
    private static final String DATABASE_NAME = "LibraryBooks.db";
    private static final int DATABASE_VERSION = 1;
    public LibraryBooks(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    private static final String TABLE_NAME = "my_books";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "book_name";
    private static final String COL_ITEM_JSON = "book_json";
    private static final String COL_IMAGE_NAME = "book_image";
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT, "
                + COL_ITEM_JSON + " TEXT, "
                + COL_IMAGE_NAME + " TEXT);";
        db.execSQL(query);

    }

    public boolean containsBook(BookItem bookItem) {
        boolean contains = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from " + TABLE_NAME + " where " + COL_NAME + " =?";
        Cursor cursor = db.rawQuery(query, new String[] {bookItem.getTitle()});
        if (cursor.getCount() > 0) {
            contains = true;
        }
        cursor.close();
        db.close();
        return contains;
    }

    public void addNewBook(BookItem bookItem) {
        if (containsBook(bookItem)) {
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        Gson gson = new Gson();
        String bookJson = gson.toJson(bookItem, BookItem.class);

        String imageUrl = "";
        if(Character.compare(bookItem.getImgUrl().charAt(0), '/') == 0) {
            imageUrl = "https://www.royalroad.com/" + bookItem.getImgUrl();
        } else {
            imageUrl = bookItem.getImgUrl();
        }
        // imageName = bookItem.getTitle().replaceAll("\\s", "_");
        String imageName = bookItem.getTitle().replaceAll("\\s", "_").replaceAll("/", "\\\\") + ".png";
        Picasso.get().load(imageUrl).into(PicassoDownload.picassoImageTarget(context, "bookImages", imageName));

        ContentValues values = new ContentValues();
        values.put(COL_NAME, bookItem.getTitle());
        values.put(COL_ITEM_JSON, bookJson);
        values.put(COL_IMAGE_NAME, imageName);
        Log.d("Library", "Book added: " + bookItem.getTitle());
        Log.d("Library", "Book json: " + bookJson);
        Log.d("Library", "book image: " + imageName);
        long success = db.insert(TABLE_NAME, null, values);
        if (success == -1) {
            Log.d("Library Insertion", "No Success");
        } else {
            Log.d("Library Insertion", "Success");
        }
        db.close();
    }

    public void removeBook(BookItem bookItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        long success = db.delete(TABLE_NAME, "book_name=?", new String[]{bookItem.getTitle()});
        if (success == -1) {
            Log.d("Library Deletion", "No Success");
        } else {
            Log.d("Library Deletion", "Success");
        }
        db.close();
    }
    public ArrayList<BookItem> bookList() {
        ArrayList<BookItem> bookList = new ArrayList<BookItem>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_ITEM_JSON}, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            Gson gson = new Gson();
            bookList.add(
                    gson.fromJson(
                            cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_JSON))
                            , BookItem.class));
            while (cursor.moveToNext()) {
                String bookJson = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_JSON));
                bookList.add(
                        gson.fromJson(
                                cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_JSON))
                                , BookItem.class));
            }
        }
        db.close();
        return bookList;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
