package com.example.webnovelreader.Filter;

import com.example.webnovelreader.Filter.FilterGroupItem;

import java.util.ArrayList;
import java.util.HashMap;

public class GenerateFilterOptions {
    public static HashMap<String, ArrayList<FilterGroupItem>> generateFilterOptions(String website) {
        HashMap<String, ArrayList<FilterGroupItem>> filterOptions = new HashMap<String, ArrayList<FilterGroupItem>>();
        switch (website) {
            case "royalroad":
                filterOptions.put("Genre", generateGenre(website));
                filterOptions.put("Content Warnings", generateContentWarnings(website));
                break;
        }
        return filterOptions;
    }
    public static ArrayList<FilterGroupItem> generateGenre(String website) {
        ArrayList<FilterGroupItem> genres = new ArrayList<FilterGroupItem>();
        switch (website) {
            case "royalroad":
                genres.add(new FilterGroupItem("Action", "action", 0));
                genres.add(new FilterGroupItem("Adventure", "adventure", 0));
                genres.add(new FilterGroupItem("Comedy", "comedy", 0));
                genres.add(new FilterGroupItem("Contemporary", "action", 0));
                genres.add(new FilterGroupItem("Drama", "action", 0));
                genres.add(new FilterGroupItem("Fantasy", "action", 0));
                genres.add(new FilterGroupItem("Historical", "historical", 0));
                genres.add(new FilterGroupItem("Horror", "horror", 0));
                genres.add(new FilterGroupItem("Mystery", "mystery", 0));
                genres.add(new FilterGroupItem("Psychological", "psychological", 0));
                genres.add(new FilterGroupItem("Romance", "romance", 0));
                genres.add(new FilterGroupItem("Satire", "satire", 0));
                genres.add(new FilterGroupItem("Sci-Fi", "sci_fi", 0));
                genres.add(new FilterGroupItem("Short Story", "one_shot", 0));
                genres.add(new FilterGroupItem("Tragedy", "tragedy", 0));
                break;
        }
        return genres;
    }

    public static ArrayList<FilterGroupItem> generateContentWarnings(String website) {
        ArrayList<FilterGroupItem> contentWarnings = new ArrayList<FilterGroupItem>();
        switch (website) {
            case "royalroad":
                contentWarnings.add(new FilterGroupItem("Profanity", "profanity", 0));
                contentWarnings.add(new FilterGroupItem("Sexual Content", "sexuality", 0));
                contentWarnings.add(new FilterGroupItem("Graphic Violence", "graphic_violence", 0));
                contentWarnings.add(new FilterGroupItem("Sensitive Content", "sensitive", 0));
                contentWarnings.add(new FilterGroupItem("AI-Assisted Content", "ai_assisted", 0));
                contentWarnings.add(new FilterGroupItem("AI-Generated Content", "ai_generated", 0));
                break;
        }
        return contentWarnings;
    }
}
