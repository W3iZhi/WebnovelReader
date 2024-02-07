package com.example.webnovelreader.BookLibrary;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webnovelreader.BookAdapter;
import com.example.webnovelreader.BookDetails.BookDetails;
import com.example.webnovelreader.BookItem;
import com.example.webnovelreader.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder>{
    ArrayList<BookItem> bookItems;
    Context context;

    public LibraryAdapter (ArrayList<BookItem> bookItems, Context context) {
        this.bookItems = bookItems;
        this.context = context;
    }
    @NonNull
    @Override
    public LibraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryAdapter.ViewHolder holder, int position) {
        BookItem bookItem = bookItems.get(position);

        String imageName = bookItem.getTitle().replaceAll("\\s", "_").replaceAll("/", "\\\\") + ".png";
        ContextWrapper cw = new ContextWrapper(holder.itemView.getContext());
        final File directory = cw.getDir("bookImages", Context.MODE_PRIVATE);
        File image = new File(directory, imageName);
        Picasso.get().load(image).into(holder.bookImage);

        holder.bookTitle.setText(bookItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return bookItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView bookImage;
        TextView bookTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.bookImage);
            bookTitle = itemView.findViewById(R.id.bookTitle);
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
