package com.example.webnovelreader.BookLibrary;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.example.webnovelreader.BookItem;
import com.example.webnovelreader.R;


import java.util.ArrayList;


public class BookLibrary extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<BookItem> bookItems;
    private LibraryBooks libraryBooks;
    private LibraryAdapter adapter;
    private Toolbar toolbar;
    private SearchView searchView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_book_library, container, false);
        return view;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.libraryToolbar);
        toolbar.inflateMenu(R.menu.library_menu);
        toolbar.setTitle("Library");
        searchView = toolbar.findViewById(R.id.librarySearch);


        libraryBooks = new LibraryBooks(getActivity());
        bookItems = libraryBooks.bookList();

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new LibraryAdapter(bookItems, getActivity());
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return false;
            }
        });
    }

    private void filterBooks(String query) {
        ArrayList<BookItem> filteredBooks = new ArrayList<>();
        for (BookItem book : bookItems) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredBooks.add(book);
            }
        }
        if (!filteredBooks.isEmpty()) {
            recyclerView.setAdapter(new LibraryAdapter(filteredBooks, getActivity()));
        }
    }



}