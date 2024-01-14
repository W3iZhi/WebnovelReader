package com.example.webnovelreader;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.select.Elements;

import java.util.ArrayList;

public class ParagraphAdapter extends RecyclerView.Adapter<ParagraphAdapter.ViewHolder> {
    private ArrayList<ParagraphItem> paragraphItems;
    private Context context;

    public ParagraphAdapter(ArrayList<ParagraphItem> paragraphItems, Context context) {
        this.paragraphItems = paragraphItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paragraph_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked", "Yes");
                ((BookReader)context).onClick();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParagraphItem paragraphItem = paragraphItems.get(position);
        Context tableContext = holder.tableView.getContext();
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        holder.transitionChapter.setVisibility(View.GONE);
        holder.tableView.setVisibility(View.GONE);

        if(paragraphItem.isTransition()) {
            holder.transitionChapter.setVisibility(View.VISIBLE);
            holder.paragraphView.setVisibility(View.GONE);
            holder.transitionChapter.setText(paragraphItem.getParagraph());
        } else if(paragraphItem.isTable()) {
            holder.tableView.setVisibility(View.VISIBLE);
            holder.paragraphView.setVisibility(View.GONE);
            Elements tableData = paragraphItem.getTableData();
            Log.d("tableData", tableData.text());
            int tableRows = tableData.size();
            for (int i = 0; i < tableRows; i++){
                Elements tableRow = tableData.eq(i).select("td");
                Log.d("tableRow", tableRow.text());
                int tableCols = tableRow.size();
                TableRow tr_head = new TableRow(tableContext);
                TextView rows = new TextView(tableContext);
                rows.setTextColor(Color.WHITE);
                rows.setLayoutParams(params);
                rows.setPadding(20,0,20,0);
                if (tableCols == 1) {
                    String text = "";
                    for(int p = 0; p < tableRow.select("p").size(); p++) {
                            text = text + "\n" + tableRow.select("p").eq(p).text() + "\n";
                        Log.d("tr text", tableRow.select("p").eq(p).text());
                    }
                    rows.setText(text);
                    tr_head.addView(rows);
                    tr_head.setBackgroundResource(R.drawable.border);
                } else {
                    for (int j = 0; j < tableCols; j++){
                        TableRow td = new TableRow(tableContext);
                        td.setBackgroundResource(R.drawable.border);
                        td.setLayoutParams(params);
                        TextView cols = new TextView(tableContext);
                        cols.setLayoutParams(params);
                        cols.setPadding(20,20,20,20);
                        cols.setTextColor(Color.WHITE);
                        cols.setGravity(Gravity.CENTER);
                        cols.setText(tableRow.select("p").eq(j).text());
                        Log.d("td text", tableRow.select("p").eq(j).text());
                        td.addView(cols);
                        tr_head.addView(td);
                    }
                }
                holder.tableView.addView(tr_head);
            }

        } else {
            holder.paragraphView.setText(paragraphItem.getParagraph());
        }
        if (position == (getItemCount()-1)) {
            ((BookReader)context).transitionChapter();
            ((BookReader)context).loadnextChapter();
        }
    }

    @Override
    public int getItemCount() {
        return paragraphItems.size();
    }
    public void colTheme (TextView textView) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView paragraphView, transitionChapter;
        TableLayout tableView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            paragraphView = itemView.findViewById(R.id.paragraphView);
            tableView = itemView.findViewById(R.id.tableView);
            transitionChapter = itemView.findViewById(R.id.transitionChapter);


        }

        @Override
        public void onClick(View v) {
        }
    }
}
