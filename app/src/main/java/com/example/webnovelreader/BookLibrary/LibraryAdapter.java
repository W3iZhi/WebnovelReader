package com.example.webnovelreader.BookLibrary;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webnovelreader.BookAdapter;
import com.example.webnovelreader.BookDetails.BookDetails;
import com.example.webnovelreader.BookDetails.ChapterItem;
import com.example.webnovelreader.BookDetails.ChaptersDatabase;
import com.example.webnovelreader.BookItem;
import com.example.webnovelreader.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder>{
    static ArrayList<BookItem> bookItems;
    ArrayList<BookItem> selectedBooks = new ArrayList<>();
    Context context;
    boolean selectMode = false;
    SelectionListener selectionListener;
    static int selected = 0;

    public void resetSelected() {
        selected = 0;
    }
    public ArrayList<BookItem> getSelectedBooks () {
        return selectedBooks;
    }
    public void reset() {
        selected = 0;
        selectedBooks.clear();

        notifyDataSetChanged();
    }

    public LibraryAdapter (ArrayList<BookItem> bookItems, Context context, SelectionListener selectionListener) {
        this.bookItems = bookItems;
        this.context = context;
        this.selectionListener = selectionListener;
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
        holder.bookFrame.setForeground(null);
        holder.bookTitle.setText(bookItem.getTitle());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectMode = true;

                if (selectedBooks.contains(bookItem)) {
                    selectedBooks.remove(bookItem);
                    selected--;
                    v.findViewById(R.id.bookFrame).setForeground(null);
                    Log.d("Library", "Deselected: " + bookItem.getTitle());
                } else {
                    selectedBooks.add(bookItem);
                    Log.d("Library", "Selected Book Added: " + selectedBooks.get(selected).getTitle());
                    selected++;
                    v.findViewById(R.id.bookFrame).setForeground(context.getDrawable(R.drawable.selected));
                    Log.d("Library", "Selected: " + bookItem.getTitle());

                }
                selectMode = selected == 0 ? false : true;
                Log.d("Library", "Selection Mode = " + String.valueOf(selectMode));
                selectionListener.selection(selected);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectMode == true) {

                    if (selectedBooks.contains(bookItem)) {
                        selectedBooks.remove(bookItem);
                        selected--;
                        v.findViewById(R.id.bookFrame).setForeground(null);
                        Log.d("Library", "Deselected: " + bookItem.getTitle());
                    } else {
                        selectedBooks.add(bookItem);
                        Log.d("Library", "Selected Book Added: " + selectedBooks.get(selected));
                        selected++;
                        v.findViewById(R.id.bookFrame).setForeground(context.getDrawable(R.drawable.selected));
                        Log.d("Library", "Selected: " + bookItem.getTitle());

                    }
                    selectMode = selected == 0 ? false : true;
                    Log.d("Library", "Selection Mode = " + String.valueOf(selectMode));
                    selectionListener.selection(selected);
                } else {
                    Intent intent = new Intent(context, BookDetails.class);

                    ChaptersDatabase chaptersDatabase = new ChaptersDatabase(context);
                    intent.putExtra("book", bookItem);


                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView bookImage;
        TextView bookTitle;
        View bookFrame;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.bookImage);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookFrame = itemView.findViewById(R.id.bookFrame);
        }

    }
}
