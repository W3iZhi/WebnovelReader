package com.example.webnovelreader.BookDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.webnovelreader.BookItem;
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
    private TextView titleView, descriptionView, chapters, words, followers, views, ratings;
    private ArrayList<ChapterItem> chapterItems = new ArrayList<>();
    private ProgressBar progressBar;
    private FlexboxLayout tagsLayout;
    private BookItem currentBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        currentBook = getIntent().getParcelableExtra("book");

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.imageView);
        titleView = findViewById(R.id.titleView);
        descriptionView = findViewById(R.id.descriptionView);
        tagsLayout = findViewById(R.id.tagsLayout);
        chapters = findViewById(R.id.chapters);
        words = findViewById(R.id.words);
        followers = findViewById(R.id.followers);
        views = findViewById(R.id.views);
        ratings = findViewById(R.id.ratings);



        titleView.setText(currentBook.getTitle());
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

        Content content = new Content();
        content.execute();

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
            try {
                String baseUrl = "https://www.royalroad.com";
                String bookUrl = currentBook.getBookUrl();
                String url = baseUrl + bookUrl;
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36(KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                        .header("Accept-Language", "*")
                        .get();
                Log.d("scrapping", "connected");

                Elements data = doc.select("table#chapters > tbody > tr");
                int size = data.size();
                for (int i = 0; i < size; i ++) {
                    String chapterName = data.eq(i).select("td > a").eq(0).text();
                    String chapterUrl = data.eq(i).select("td > a").attr("href");
                    chapterItems.add(new ChapterItem(chapterName, chapterUrl, i));
                    Log.d("chapters", "chapterName: " + chapterName + " , chapterUrl: " + chapterUrl);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}