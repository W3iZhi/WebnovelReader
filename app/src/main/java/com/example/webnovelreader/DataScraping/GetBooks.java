package com.example.webnovelreader.DataScraping;

import android.util.Log;

import com.example.webnovelreader.BookItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GetBooks {
    public static void scrapeRoyalroad(Document doc, ArrayList<BookItem> bookItems) {
        Elements data = doc.select("div.fiction-list-item");
        int size = data.size();
        for (int i = 0; i < size; i++) {
            String imgUrl = data.eq(i)
                    .select("img")
                    .attr("src");
            if(Character.compare(imgUrl.charAt(0), '/') == 0) {
                imgUrl = "https://www.royalroad.com/" + imgUrl;
            }
            String title = data.select("div.col-sm-10 > h2 > a")
                    .eq(i)
                    .text();
            Elements descriptions = data.select("div.margin-top-10")
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
            int pagesValues = Integer.parseInt(pages.replaceAll("\\s.*", "").replace(",", "")) * 275;
            DecimalFormat commas = new DecimalFormat("###,###,###");
            String words = commas.format(pagesValues) + " Words";
            String chapters = data.select("div.col-sm-10 > div.row")
                    .eq(i).select("span").eq(4).text();
            String rating = data.select("div.col-sm-10 > div.row")
                    .eq(i).select("span").eq(1).attr("title") + " Stars";
            Elements tagList = data.select("div.col-sm-10 > div.margin-bottom-10")
                    .eq(i).select("span.tags > a");
            Log.d("Tags", "Num: " + tagList.size());
            ArrayList<String> tags = new ArrayList<>();
            for (Element tag : tagList) {
                tags.add(tag.text());
            }
            bookItems.add(new BookItem(imgUrl, title, description, bookUrl, followers, views, words, chapters, rating, tags));
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
    }
}
