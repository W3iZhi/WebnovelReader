package com.example.webnovelreader;

import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webnovelreader.BookDetails.BookDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private ArrayList<BookItem> bookItems;
    private Context context;

    public BookAdapter(ArrayList<BookItem> bookItems, Context context) {
        this.bookItems = bookItems;
        this.context = context;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        BookItem bookItem = bookItems.get(position);
        holder.titleView.setText(bookItem.getTitle());
        holder.descriptionView.setText(bookItem.getDescription());
        holder.chaptersView.setText(bookItem.getChapters());
        holder.wordsView.setText(bookItem.getWords());
        holder.followersView.setText(bookItem.getFollowers());
        holder.viewsView.setText(bookItem.getViews());
        holder.ratingsView.setText(bookItem.getRating());
        String imageUrl = "";
        Log.d("index check", bookItem.getTitle() + ", " + bookItem.getImgUrl());
        if(Character.compare(bookItem.getImgUrl().charAt(0), '/') == 0) {
            imageUrl = "https://www.royalroad.com/" + bookItem.getImgUrl();
        } else {
            imageUrl = bookItem.getImgUrl();
        }
        Picasso.get().load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return bookItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView titleView, descriptionView, chaptersView, wordsView, followersView,viewsView,ratingsView;
        ScrollView scrollView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            titleView = itemView.findViewById(R.id.titleView);
            scrollView = itemView.findViewById(R.id.scrollView);
            descriptionView = itemView.findViewById((R.id.descriptionView));
            chaptersView = itemView.findViewById(R.id.chaptersView);
            wordsView = itemView.findViewById(R.id.wordsView);
            followersView = itemView.findViewById(R.id.followersView);
            viewsView = itemView.findViewById(R.id.viewsView);
            ratingsView = itemView.findViewById(R.id.ratingsView);

            descriptionView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            descriptionView.setMovementMethod(new ScrollingMovementMethod());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getBindingAdapterPosition();
            BookItem bookItem = bookItems.get(position);

            Intent intent = new Intent(context, BookDetails.class);

            intent.putExtra("book", bookItem);

            context.startActivity(intent);
        }
    }
}
