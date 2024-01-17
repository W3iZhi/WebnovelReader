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
                genres.add(new FilterGroupItem("Action", "action"));
                genres.add(new FilterGroupItem("Adventure", "adventure"));
                genres.add(new FilterGroupItem("Comedy", "comedy"));
                genres.add(new FilterGroupItem("Contemporary", "action"));
                genres.add(new FilterGroupItem("Drama", "action"));
                genres.add(new FilterGroupItem("Fantasy", "action"));
                genres.add(new FilterGroupItem("Historical", "historical"));
                genres.add(new FilterGroupItem("Horror", "horror"));
                genres.add(new FilterGroupItem("Mystery", "mystery"));
                genres.add(new FilterGroupItem("Psychological", "psychological"));
                genres.add(new FilterGroupItem("Romance", "romance"));
                genres.add(new FilterGroupItem("Satire", "satire"));
                genres.add(new FilterGroupItem("Sci-Fi", "sci_fi"));
                genres.add(new FilterGroupItem("Short Story", "one_shot"));
                genres.add(new FilterGroupItem("Tragedy", "tragedy"));
                break;
        }
        return genres;
    }

    public static ArrayList<FilterGroupItem> generateContentWarnings(String website) {
        ArrayList<FilterGroupItem> contentWarnings = new ArrayList<FilterGroupItem>();
        switch (website) {
            case "royalroad":
                contentWarnings.add(new FilterGroupItem("Profanity", "profanity"));
                contentWarnings.add(new FilterGroupItem("Sexual Content", "sexuality"));
                contentWarnings.add(new FilterGroupItem("Graphic Violence", "graphic_violence"));
                contentWarnings.add(new FilterGroupItem("Sensitive Content", "sensitive"));
                contentWarnings.add(new FilterGroupItem("AI-Assisted Content", "ai_assisted"));
                contentWarnings.add(new FilterGroupItem("AI-Generated Content", "ai_generated"));
                break;
        }
        return contentWarnings;
    }
}
