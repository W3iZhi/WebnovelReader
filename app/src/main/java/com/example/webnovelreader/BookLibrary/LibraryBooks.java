package com.example.webnovelreader.BookLibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.webnovelreader.BookItem;
import com.example.webnovelreader.PicassoDownload;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;

public class LibraryBooks extends SQLiteOpenHelper {
    Context context;
    private static final String DATABASE_NAME = "LibraryBooks.db";
    private static final int DATABASE_VERSION = 1;
    public LibraryBooks(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String TABLE_NAME = "mybooks";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_ITEM_JSON = "itemjson";
    private static final String COL_IMAGE_NAME = "imagename";
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT,"
                + COL_ITEM_JSON + " TEXT,"
                + COL_IMAGE_NAME + " TEXT)";
        db.execSQL(query);
    }

    public void addNewBook(BookItem bookItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        Gson gson = new Gson();
        String bookJson = gson.toJson(bookItem, BookItem.class);

        String imageUrl = "";
        if(Character.compare(bookItem.getImgUrl().charAt(0), '/') == 0) {
            imageUrl = "https://www.royalroad.com/" + bookItem.getImgUrl();
        } else {
            imageUrl = bookItem.getImgUrl();
        }
        String imageName = bookItem.getTitle().replaceAll("\\s", "_") + ".jpeg";
        //Picasso.get().load(imageUrl).into(PicassoDownload.picassoImageTarget(context, "bookImages", imageName));

        ContentValues values = new ContentValues();
        values.put(COL_NAME, bookItem.getTitle());
        values.put(COL_ITEM_JSON, bookJson);
        values.put(COL_IMAGE_NAME, imageName);
        Log.d("Library", "Book added: " + bookItem.getTitle());
        Log.d("Library", "Book json: " + bookJson);
        Log.d("Library", "book image: " + imageName);
        //db.insert(TABLE_NAME, null, values);
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
