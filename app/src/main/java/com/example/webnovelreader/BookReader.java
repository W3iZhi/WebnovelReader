package com.example.webnovelreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;

public class BookReader extends AppCompatActivity implements OnClickListener, ParagraphListener {
    private RecyclerView recyclerView;
    private ParagraphAdapter adapter;
    private ArrayList<ChapterItem> chapterItems;
    private ChapterItem currentChapter;
    private ArrayList<ParagraphItem> paragraphItems = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView chapterName;
    private LinearLayout readerTop, readerBot;
    private Button previous, next;
    private ParagraphItem transitionText;
    int selectedChapter;
    int maxChapters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);

        transitionText = new ParagraphItem("", false);
        selectedChapter = getIntent().getIntExtra("selectedChapter", 1);
        chapterItems = getIntent().getParcelableArrayListExtra("chapterList");
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        readerTop = findViewById(R.id.readerTop);
        readerBot = findViewById(R.id.readerBot);
        chapterName = findViewById(R.id.chapterName);
        previous = findViewById(R.id.previousChapter);
        next = findViewById(R.id.nextChapter);
        maxChapters = chapterItems.size();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParagraphAdapter(paragraphItems, this);
        recyclerView.setAdapter(adapter);

        loadChapter();
    }

    private void loadChapter() {
        Content content = new Content();
        content.execute();
    }

    public void navigateChapter(View button) {
        if (button == previous) {
            if (selectedChapter != 0) {
                selectedChapter--;
                paragraphItems.clear();
                chapterName.setText(chapterItems.get(selectedChapter).getChapterName());
                Log.d("clicked", "previous");
                Log.d("currentPage", Integer.toString(selectedChapter));
                loadChapter();
            } else {
                Toast.makeText(this, "This are no earlier chapters", Toast.LENGTH_LONG).show();
            }
        } else if (button == next) {
            if (selectedChapter != maxChapters - 1) {
                selectedChapter++;
                paragraphItems.clear();
                chapterName.setText(chapterItems.get(selectedChapter).getChapterName());
                Log.d("clicked", "previous");
                Log.d("currentPage", Integer.toString(selectedChapter));
                loadChapter();
            } else {
                Toast.makeText(this, "There are no more new chapters", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick() {
        if(readerTop.getVisibility() == View.VISIBLE || readerBot.getVisibility() == View.VISIBLE) {
            //readerTop.setVisibility(View.GONE);
            readerBot.setVisibility(View.GONE);
        } else {
            chapterName.setText(chapterItems.get(selectedChapter).getChapterName());
            /* Needs to rework to show current chapter rather than latest chapter loaded
            readerTop.setVisibility(View.VISIBLE);
             */
            readerBot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadnextChapter() {
        if (selectedChapter != maxChapters - 1) {
            selectedChapter++;
            Content content = new Content();
            content.execute();
        }
    }

    @Override
    public void transitionChapter() {
        String currentChapter = chapterItems.get(selectedChapter).getChapterName();
        String nextChapter = selectedChapter != maxChapters -1 ? chapterItems.get(selectedChapter + 1).getChapterName() : "NIL";
        String transition = "Previous Chapter: " + currentChapter + "\n\n" + "Next Chapter: " + nextChapter;
        transitionText.setParagraph(transition);
        transitionText.setTransition(true);
    }

    private class Content extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(BookReader.this, android.R.anim.fade_in));
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(BookReader.this, android.R.anim.fade_out));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (transitionText.isTransition()) {
                    paragraphItems.add(transitionText);
                }
                currentChapter = chapterItems.get(selectedChapter);
                String baseUrl = "https://www.royalroad.com";
                //String chapterUrl = getIntent().getStringExtra("chapterUrl");
                String chapterUrl = currentChapter.getChapterUrl();
                Log.d("chapterUrl", "url: " + chapterUrl);
                String url = baseUrl + chapterUrl;
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36(KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                        .header("Accept-Language", "*")
                        .get();
                Log.d("scrapping", "connected");


                Elements data = doc.select("div.chapter-inner > *");
                if (data.first().is("div")) {
                    data = data.select("> *");
                }
                int size = data.size();
                Log.d("No. of Paragraphs", Integer.toString(size));
                
                for (int i = 0; i < size; i ++) {
                    boolean isTable = false;
                    String paragraph = "";
                    Elements tableData = null;
                    Elements currentParagraph = data.eq(i);
                    
                    if (currentParagraph.is("div")) {
                        isTable = true;
                        tableData = currentParagraph.select("table > tbody > *");
                    } else {
                        paragraph = currentParagraph.text();
                    }
                    if (isTable) {
                        paragraphItems.add(new ParagraphItem(paragraph, isTable, tableData, false));
                    } else {
                        paragraphItems.add(new ParagraphItem(paragraph, isTable, false));
                    }
                    Log.d("paragraphs", "paragraph: " + paragraph + " , isTable: " + isTable);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}