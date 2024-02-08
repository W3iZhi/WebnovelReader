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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.webnovelreader.DataScraping.WebscraperManager;
import com.example.webnovelreader.Filter.FilterExpandableListAdapter;
import com.example.webnovelreader.Filter.FilterGroupItem;
import com.example.webnovelreader.Filter.FilterManager;
import com.example.webnovelreader.Filter.GenerateFilterOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class BookSelect extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private ArrayList<BookItem> bookItems = new ArrayList<>();
    private ProgressBar progressBar;
    private EditText pageView, searchBar;
    private TextView maxPage;
    private Button previous, next, reset, filter;
    private ImageButton advancedSearch, search;
    private static int currentPage;
    private String searchWord, filterUrl, source;
    private int[] maxPages = new int[1];
    private BottomSheetDialog filterDialog;
    private HashMap<String, ArrayList<FilterGroupItem>> filterChoices;
    private ArrayList<String> filterCategories;
    private ExpandableListView filterOptions;
    private ExpandableListAdapter filterOptionsAdapter;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentPage = 1;

        source = getIntent().getStringExtra("source");
        previous = findViewById(R.id.previousButton);
        next = findViewById(R.id.nextButton);
        search = findViewById(R.id.search);
        advancedSearch = findViewById(R.id.advancedSearch);
        pageView = findViewById(R.id.pageView);
        searchBar = findViewById(R.id.searchBar);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        maxPage = findViewById(R.id.maxPage);

        searchWord = "";
        filterUrl = "";

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookAdapter(bookItems, this, source);
        recyclerView.setAdapter(adapter);

        pageView.setText(Integer.toString(currentPage));


        loadBooks();

        filterDialog = new BottomSheetDialog(this);
        createDialog();
    }
    private void loadBooks() {
        if (!bookItems.isEmpty()) {
            bookItems.clear();
        }
        pageView.setText(Integer.toString(currentPage));
        Content content = new Content();
        content.execute();
    }
    public void search(View button) {
        if (button == search) {
            searchWord = searchBar.getText().toString();
            loadBooks();
        } else if (button == advancedSearch) {
            filterDialog.show();
        }
    }
    private void createDialog() {

        Log.d("Dialog", "created");
        View dialogView = getLayoutInflater().inflate(R.layout.filter_dialog, null, false);
        filterDialog.setContentView(dialogView);
        BottomSheetBehavior dialogBehavior = filterDialog.getBehavior();
        dialogBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        dialogBehavior.setDraggable(false);
        filterOptions = filterDialog.findViewById(R.id.filterOptions);

        FilterManager.setupFilter(this, filterCategories, filterChoices, filterOptionsAdapter, filterOptions);

        reset = filterDialog.findViewById(R.id.reset);
        filter = filterDialog.findViewById(R.id.filter);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dialog", "reset");
                createDialog();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Dialog", "filter");
                filterUrl = FilterManager.setFilter(filterCategories, filterChoices);
                filterDialog.dismiss();
                loadBooks();
            }
        });
    }

    public void navigate(View button) {
        if (button == previous) {
            if (currentPage != 1) {
                currentPage--;
                Log.d("clicked", "previous");
                loadBooks();
            } else {
                Toast.makeText(this, "This is the first page", Toast.LENGTH_LONG).show();
            }
        } else if (button == next) {
            if (currentPage != maxPages[0]) {
                currentPage++;
                Log.d("clicked", "next");
                loadBooks();
            } else {
                Toast.makeText(this, "This is the last page", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class Content extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(BookSelect.this, android.R.anim.fade_in));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(BookSelect.this, android.R.anim.fade_out));
            maxPage.setText("/" + maxPages[0]);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            WebscraperManager.scrapeBooks(source, currentPage, searchWord, filterUrl, maxPages, bookItems);
            return null;

        }
    }
}