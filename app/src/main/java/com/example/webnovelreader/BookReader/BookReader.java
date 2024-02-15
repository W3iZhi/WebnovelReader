package com.example.webnovelreader.BookReader;

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

import com.example.webnovelreader.BookDetails.ChapterItem;
import com.example.webnovelreader.DataScraping.WebscraperManager;
import com.example.webnovelreader.OnClickListener;
import com.example.webnovelreader.R;

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
                recyclerView.scrollToPosition(0);
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
                recyclerView.scrollToPosition(0);
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
    public void loadNextChapter() {
        if (selectedChapter != maxChapters - 1) {
            transitionChapter(paragraphItems);
            selectedChapter++;
            //TODO: Check if next chapter is downloaded
            Content content = new Content();
            content.execute();
        }
    }

    private void transitionChapter(ArrayList<ParagraphItem> paragraphItems) {
        String currentChapter = chapterItems.get(selectedChapter).getChapterName();
        String nextChapter = selectedChapter != maxChapters -1 ? chapterItems.get(selectedChapter + 1).getChapterName() : "NIL";
        String transitionText = "Previous Chapter:\n " + currentChapter + "\n\n" + "Next Chapter:\n " + nextChapter;
        ParagraphItem transition = new ParagraphItem(transitionText, true);
        paragraphItems.add(transition);
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
            WebscraperManager.scrapeChapterData(transitionText, paragraphItems, chapterItems, currentChapter, selectedChapter);
            return null;
        }
    }
}