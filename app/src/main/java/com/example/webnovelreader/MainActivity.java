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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private ArrayList<BookItem> bookItems = new ArrayList<>();
    private ProgressBar progressBar;
    private EditText pageView;
    private Button previous, next;
    private static int currentPage;
    private int maxPages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentPage = 1;

        previous = findViewById(R.id.previousButton);
        next = findViewById(R.id.nextButton);
        pageView = findViewById(R.id.pageView);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookAdapter(bookItems, this);
        recyclerView.setAdapter(adapter);

        pageView.setText(Integer.toString(currentPage));


        loadBooks();
    }
    private void loadBooks() {
        Content content = new Content();
        content.execute();
    }

    public void navigate(View button) {
        if (button == previous) {
            if (currentPage != 1) {
                currentPage--;
                bookItems.clear();
                pageView.setText(Integer.toString(currentPage));
                Log.d("clicked", "previous");
                Log.d("currentPage", Integer.toString(currentPage));
                loadBooks();
            } else {
                Toast.makeText(this, "This is the first page", Toast.LENGTH_LONG).show();
            }
        } else if (button == next) {
            if (currentPage != maxPages) {
                currentPage++;
                bookItems.clear();
                pageView.setText(Integer.toString(currentPage));
                Log.d("clicked", "next");
                Log.d("currentPage", Integer.toString(currentPage));
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
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d("scrapping", "executed");
                String base = "https://www.royalroad.com/fictions/search?page=";
                int page = currentPage;
                String url = base + page + "&advanced=true";
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36(KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                        .header("Accept-Language", "*")
                        .get();
                Log.d("scrapping", "connected");

                Elements data = doc.select("div.fiction-list-item");
                String maxPageNum = doc.select("ul.pagination > *").eq(6).select("a").attr("data-page");
                maxPages = Integer.parseInt(maxPageNum);
                Log.d("maxPages", "Number: " + maxPages);
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    String imgUrl = data.eq(i)
                            .select("img")
                            .attr("src");
                    String title = data.select("div.col-sm-10 > h2 > a")
                            .eq(i)
                            .text();
                    Elements descriptions = data.select("div.margin-top-10" )
                            .eq(i)
                            .select("> *");
                    String description = "";
                    for (Element paragraph : descriptions) {
                        if (paragraph.select("> *").is("span")) {
                            Elements spans = paragraph.select("> *");
                            for (Element span : spans) {
                                description = description + span.text() + "\n";
                            }
                        } else {
                            description = description + paragraph.text() + "\n\n";
                        }
                    }
                    String bookUrl = data.select("div.col-sm-10 > h2 > a")
                            .eq(i)
                            .attr("href");
                    String followers = data.select("div.col-sm-10 > div.row")
                            .eq(i).select("span").eq(0).text();
                    String views = data.select("div.col-sm-10 > div.row")
                            .eq(i).select("span").eq(3).text();
                    String pages = data.select("div.col-sm-10 > div.row")
                            .eq(i).select("span").eq(2).text();
                    int pagesValues = Integer.parseInt(pages.replaceAll("\\s.*", "").replace(",","")) * 275;
                    DecimalFormat commas = new DecimalFormat("###,###,###");
                    String words = commas.format(pagesValues) + " Words";
                    String chapters = data.select("div.col-sm-10 > div.row")
                            .eq(i).select("span").eq(4).text();
                    String rating = data.select("div.col-sm-10 > div.row")
                            .eq(i).select("span").eq(1).attr("title") + " Stars";

                    bookItems.add(new BookItem(imgUrl, title, description, bookUrl, followers, views, words, chapters, rating));
                    Log.d("items"
                            , "img: " + imgUrl
                            + " , title: " + title
                            + " , description: " + description
                            + " , bookUrl: " + bookUrl
                            + " , followers: " + followers
                            + " , views: " + views
                            + " , words: " + words
                            + " , chapters: " + chapters
                            + " , rating: " + rating);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
        public String pagesToWord(String pages) {
            String pagesValues = pages.replaceAll("\\s.*", "").replace(",","");
            String calculatedWords = Integer.toString(Integer.parseInt(pagesValues)*275);
            StringBuilder temp = new StringBuilder(calculatedWords);
            String reverseWords = (temp.reverse().toString()).replaceAll("...", "$0,");
            temp = new StringBuilder(reverseWords);
            String wordCount = temp.toString();
            char first = wordCount.charAt(0);
            boolean test = first == ',';
            if(test) {

            }
            return "~" +  temp.reverse().toString() + " words";
        }
    }
}