package com.example.webnovelreader.BookDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.webnovelreader.BookItem;
import com.example.webnovelreader.DataScraping.WebscraperManager;
import com.example.webnovelreader.R;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class BookDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChapterAdapter adapter;
    private ImageView imageView;
    private TextView descriptionView, chapters, words, followers, views, ratings;
    private MenuItem download;
    private ArrayList<ChapterItem> chapterItems = new ArrayList<>();
    private ProgressBar progressBar;
    private FlexboxLayout tagsLayout;
    private BookItem currentBook;
    private Toolbar toolbar;
    private ChaptersDatabase chaptersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        chaptersDatabase = new ChaptersDatabase(this);
        currentBook = getIntent().getParcelableExtra("book");
        chapterItems = getIntent().getParcelableArrayListExtra("chaptersList");
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.imageView);
        descriptionView = findViewById(R.id.descriptionView);
        tagsLayout = findViewById(R.id.tagsLayout);
        chapters = findViewById(R.id.chapters);
        words = findViewById(R.id.words);
        followers = findViewById(R.id.followers);
        views = findViewById(R.id.views);
        ratings = findViewById(R.id.ratings);
        toolbar = findViewById(R.id.bookToolbar);

        toolbar.setTitle(currentBook.getTitle());
        toolbar.inflateMenu(R.menu.book_details_menu);
        download = toolbar.getMenu().findItem(R.id.chaptersDownload);

        Picasso.get().load(currentBook.getImgUrl()).into(imageView);

        ArrayList<String> tags = currentBook.getTags();
        for (int i = 0; i < tags.size(); i++) {
            TextView tv = new TextView(this);
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,20,10,0);
            tv.setText(tags.get(i));
            tv.setBackgroundResource(R.color.blue_tags);
            tv.setTextColor(getResources().getColor(R.color.white));
            tv.setPadding(10,10,10,10);
            tv.setLayoutParams(params);
            tagsLayout.addView(tv);
        }

        chapters.setText(currentBook.getChapters());
        words.setText(currentBook.getWords());
        followers.setText(currentBook.getFollowers());
        views.setText(currentBook.getViews());
        ratings.setText("Rating\n" + currentBook.getRating());


        descriptionView.setText(currentBook.getDescription());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChapterAdapter(chapterItems, this);
        recyclerView.setAdapter(adapter);

        if (chapterItems == null) {
            Content content = new Content();
            content.execute();
        }
        download.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Log.d("Chapters Database", "Downloading Chapter Data");
                //TODO: Implement thread to scrape chapterData for offline reading
//                Thread thread = new Thread() {
//                    @Override
//                    public void run() {
//                    }
//                }
                return false;
            }
        });
    }

    private class Content extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(BookDetails.this, android.R.anim.fade_in));
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(BookDetails.this, android.R.anim.fade_out));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            WebscraperManager.scrapeChapters(currentBook, chapterItems);
            return null;
        }
    }
}