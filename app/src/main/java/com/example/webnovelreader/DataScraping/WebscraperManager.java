package com.example.webnovelreader.DataScraping;

import android.util.Log;

import com.example.webnovelreader.BookDetails.ChapterItem;
import com.example.webnovelreader.BookDetails.ChaptersDatabase;
import com.example.webnovelreader.BookItem;
import com.example.webnovelreader.BookReader.ParagraphAdapter;
import com.example.webnovelreader.BookReader.ParagraphItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class WebscraperManager {
    public static IOException scrapeBooks(String source, int currentPage, String searchWord, String filterUrl, int[] maxPages, ArrayList<BookItem> bookItems) {
        try {
            if (source.equals("royalroad")) {
                Log.d("scrapping", "executed");
                String base = "https://www.royalroad.com/fictions/search?page=";
                int page = currentPage;
                String url = base + page + "&keyword=" + searchWord + filterUrl;
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36(KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                        .header("Accept-Language", "*")
                        .get();
                Log.d("scrapping", "connected");
                String maxPageNum = doc.select("ul.pagination > *").eq(6).select("a").attr("data-page");
                if (maxPages[0] < Integer.parseInt(maxPageNum)) {
                    maxPages[0] = Integer.parseInt(maxPageNum);
                }
                Log.d("maxPages", "Number: " + maxPages);
                WebscraperManager.scrapeRoyalroadBooks(doc, bookItems);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static IOException scrapeChapters(BookItem currentBook, ArrayList<ChapterItem> chapterItems) {
        try {
            if (currentBook.getSource().equals("royalroad")) {
                String baseUrl = "https://www.royalroad.com";
                String bookUrl = currentBook.getBookUrl();
                String url = baseUrl + bookUrl;
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36(KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                        .header("Accept-Language", "*")
                        .get();
                Log.d("scrapping", "connected");
                WebscraperManager.scrapeRoyalroadBookChapters(doc, chapterItems, currentBook.getTitle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static IOException scrapeChapterData(ParagraphItem transitionText, ArrayList<ParagraphItem> paragraphItems, ArrayList<ChapterItem> chapterItems, ChapterItem currentChapter, int selectedChapter) {
        try {
            if (transitionText.isTransition()) {
                paragraphItems.add(transitionText);
            }
            currentChapter = chapterItems.get(selectedChapter);
            String baseUrl = "https://www.royalroad.com";
            String chapterUrl = currentChapter.getChapterUrl();
            Log.d("chapterUrl", "url: " + chapterUrl);
            String url = baseUrl + chapterUrl;
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                            " AppleWebKit/537.36(KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                    .header("Accept-Language", "*")
                    .get();
            Log.d("scrapping", "connected");

            scrapeRoyalroadChapterData(doc, paragraphItems);
//            Elements data = doc.select("div.chapter-inner > *");
//            if (data.first().is("div")) {
//                data = data.select("> *");
//            }
//            int size = data.size();
//            Log.d("No. of Paragraphs", Integer.toString(size));
//
//            for (int i = 0; i < size; i ++) {
//                boolean isTable = false;
//                String paragraph = "";
//                Elements tableData = null;
//                Elements currentParagraph = data.eq(i);
//
//                if (currentParagraph.is("div")) {
//                    isTable = true;
//                    tableData = currentParagraph.select("table > tbody > *");
//                } else {
//                    paragraph = currentParagraph.text();
//                }
//                if (isTable) {
//                    paragraphItems.add(new ParagraphItem(paragraph, isTable, tableData, false));
//                } else {
//                    paragraphItems.add(new ParagraphItem(paragraph, isTable, false));
//                }
//                Log.d("paragraphs", "paragraph: " + paragraph + " , isTable: " + isTable);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static IOException scrapeChaptersToDatabase(BookItem currentBook, ChaptersDatabase chaptersDatabase) {
        try {
            if (currentBook.getSource().equals("royalroad")) {
                String baseUrl = "https://www.royalroad.com";
                String bookUrl = currentBook.getBookUrl();
                String url = baseUrl + bookUrl;
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)" +
                                " AppleWebKit/537.36(KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                        .header("Accept-Language", "*")
                        .get();
                Log.d("scrapping", "connected");
                WebscraperManager.scrapeRoyalroadBookChaptersToDatabase(doc, chaptersDatabase, currentBook.getTitle());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void scrapeRoyalroadBookChaptersToDatabase (Document doc, ChaptersDatabase chaptersDatabase, String bookName) {
        Elements data = doc.select("table#chapters > tbody > tr");
        int size = data.size();
        for (int i = 0; i < size; i ++) {
            String chapterName = data.eq(i).select("td > a").eq(0).text();
            String chapterUrl = data.eq(i).select("td > a").attr("href");
            chaptersDatabase.addChapter(new ChapterItem(chapterName, chapterUrl, i, bookName, 0));
            Log.d("chapters", "chapterName: " + chapterName + " , chapterUrl: " + chapterUrl);
        }
    }
    public static void scrapeRoyalroadBooks(Document doc, ArrayList<BookItem> bookItems) {
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
            bookItems.add(new BookItem(imgUrl, title, description, bookUrl, followers, views, words, chapters, rating, tags, "royalroad"));
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
    public static void scrapeRoyalroadBookChapters(Document doc, ArrayList<ChapterItem> chapterItems, String bookName) {
        Elements data = doc.select("table#chapters > tbody > tr");
        int size = data.size();
        for (int i = 0; i < size; i ++) {
            String chapterName = data.eq(i).select("td > a").eq(0).text();
            String chapterUrl = data.eq(i).select("td > a").attr("href");
            chapterItems.add(new ChapterItem(chapterName, chapterUrl, i, bookName, 0));
            Log.d("chapters", "chapterName: " + chapterName + " , chapterUrl: " + chapterUrl);
        }
    }
    public static void scrapeRoyalroadChapterData(Document doc, ArrayList<ParagraphItem> paragraphItems) {
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
    }

    public static void scrapeChaptersDataToDatabase(BookItem currenBook, ChaptersDatabase chaptersDatabase) {
    }
}
