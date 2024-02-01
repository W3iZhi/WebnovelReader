package com.example.webnovelreader.BookDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.webnovelreader.R;
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
    private TextView titleView, descriptionView;
    private ArrayList<ChapterItem> chapterItems = new ArrayList<>();
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.imageView);
        titleView = findViewById(R.id.titleView);
        descriptionView = findViewById(R.id.descriptionView);

        titleView.setText(getIntent().getStringExtra("title"));
        descriptionView.setText(getIntent().getStringExtra("description"));
        Picasso.get().load(getIntent().getStringExtra("image")).into(imageView);



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
                String bookUrl = getIntent().getStringExtra("bookUrl");
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