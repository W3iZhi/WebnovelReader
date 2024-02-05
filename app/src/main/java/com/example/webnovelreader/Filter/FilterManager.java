package com.example.webnovelreader.Filter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.webnovelreader.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterManager {

    //TODO: Keep track of the filter inside filter options
    public static void setupFilter(Context context, ArrayList<String> filterCategories, HashMap<String, ArrayList<FilterGroupItem>> filterChoices, ExpandableListAdapter filterOptionsAdapter, ExpandableListView filterOptions) {
        filterChoices = GenerateFilterOptions.generateFilterOptions("royalroad");
        filterCategories = new ArrayList<String>(filterChoices.keySet());
        Log.d("Filter Choices", filterChoices.get(filterCategories.get(0)).get(0).getFilterChoice());
        filterOptionsAdapter = new FilterExpandableListAdapter(context, filterCategories, filterChoices);
        filterOptions.setAdapter(filterOptionsAdapter);

    }
    public static String setFilter(ArrayList<String> filterCategories, HashMap<String, ArrayList<FilterGroupItem>> filterChoices) {
        String filterUrl = "";
        int categoriesNum = filterCategories.size();
        Log.d("Filter Categories", Integer.toString(categoriesNum));
        for (int i = 0; i < categoriesNum; i++) {
            ArrayList<FilterGroupItem> filterChoiceList = filterChoices.get(filterCategories.get(i));
            for (int j = 0; j < filterChoiceList.size(); j++) {
                FilterGroupItem choice = filterChoiceList.get(j);
                switch (choice.getCheckState()) {
                    default:
                    case 0:
                        //unchecked
                        break;
                    case 1:
                        //checked
                        filterUrl = filterUrl + "&tagsAdd=" + choice.getFilterUrl();
                        break;
                    case 2:
                        //removed
                        filterUrl = filterUrl + "&tagsRemove=" + choice.getFilterUrl();
                        break;
                }
            }
        }
        Log.d("Filter Url", filterUrl);
        return filterUrl;
    }

}
