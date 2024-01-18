package com.example.webnovelreader.Filter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.webnovelreader.R;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.HashMap;
//TODO: Prevent dialog from scrolling when scrolling expandablelist
public class FilterExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> filterCategories;
    private HashMap<String, ArrayList<FilterGroupItem>> filterChoices;

    public FilterExpandableListAdapter(Context context, ArrayList<String> filterCategories, HashMap<String, ArrayList<FilterGroupItem>> filterChoices) {
        this.context = context;
        this.filterCategories = filterCategories;
        this.filterChoices = filterChoices;
    }
    @Override
    public int getGroupCount() {
        return filterCategories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return filterChoices.get(filterCategories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return filterCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return filterChoices.get(filterCategories.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String filterOptionName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.filter_group,null);
        }
        TextView filterOption = (TextView) convertView.findViewById(R.id.filterOption);
        filterOption.setText(filterOptionName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        FilterGroupItem filterItemObject = (FilterGroupItem) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.filter_item, null);
        }
        MaterialCheckBox filterItem = (MaterialCheckBox) convertView.findViewById(R.id.filterItem);
        filterItem.setOnClickListener(new View.OnClickListener() {
            int state = filterItem.getCheckedState();
            @Override
            public void onClick(View v) {

                if (state == MaterialCheckBox.STATE_CHECKED) {
                    Log.d("Checkbox", "Checked");
                    filterItem.setCheckedState(MaterialCheckBox.STATE_INDETERMINATE);
                    Log.d("Checkbox", "Set Indeterminate");
                    state = MaterialCheckBox.STATE_INDETERMINATE;
                    filterItemObject.setCheckState(state);
                    Log.d("FilterItemObject", "Filter Choice " + filterItemObject.getFilterChoice() + " State: " + filterItemObject.getCheckState());
                } else if (state == MaterialCheckBox.STATE_UNCHECKED) {
                    Log.d("Checkbox", "Unchecked");
                    filterItem.setCheckedState(MaterialCheckBox.STATE_CHECKED);
                    Log.d("Checkbox", "Set Checked");
                    state =MaterialCheckBox.STATE_CHECKED;
                    filterItemObject.setCheckState(state);
                    Log.d("FilterItemObject", "Filter Choice: " + filterItemObject.getFilterChoice() + " State: " + filterItemObject.getCheckState());
                } else if (state == MaterialCheckBox.STATE_INDETERMINATE) {
                    Log.d("Checkbox", "Indeterminate");
                    filterItem.setCheckedState(MaterialCheckBox.STATE_UNCHECKED);
                    Log.d("Checkbox", "Set Unchecked");
                    state = MaterialCheckBox.STATE_UNCHECKED;
                    filterItemObject.setCheckState(state);
                    Log.d("FilterItemObject", "Filter Choice " + filterItemObject.getFilterChoice() + " State: " + filterItemObject.getCheckState());
                }
            }
        });
        filterItem.setText(filterItemObject.getFilterChoice());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
