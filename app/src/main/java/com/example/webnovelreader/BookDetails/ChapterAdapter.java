package com.example.webnovelreader.BookDetails;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webnovelreader.BookReader.BookReader;
import com.example.webnovelreader.R;

import java.util.ArrayList;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {
    private ArrayList<ChapterItem> chapterItems;
    private Context context;
    public ChapterAdapter(ArrayList<ChapterItem> chapterItems, Context context) {
        this.chapterItems = chapterItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ChapterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item, parent, false);
        return new ChapterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.ViewHolder holder, int position) {
        ChapterItem chapterItem = chapterItems.get(position);
        holder.chapterView.setText(chapterItem.getChapterName());
    }

    @Override
    public int getItemCount() { return chapterItems.size();}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView chapterView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterView = itemView.findViewById(R.id.chapterView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("Chapter Clicked", "Yes");
            int position = getBindingAdapterPosition();
            ChapterItem chapterItem = chapterItems.get(position);

            Intent intent = new Intent(context, BookReader.class);

            intent.putExtra("chapterUrl", chapterItem.getChapterUrl());
            intent.putParcelableArrayListExtra("chapterList", chapterItems);
            intent.putExtra("selectedChapter", chapterItem.getChapterIndex());

            context.startActivity(intent);
        }
    }
}
