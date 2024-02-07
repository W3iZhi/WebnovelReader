package com.example.webnovelreader.BookLibrary;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.example.webnovelreader.BookItem;
import com.example.webnovelreader.R;


import java.util.ArrayList;


public class BookLibrary extends Fragment implements SelectionListener{

    private RecyclerView recyclerView;
    private static ArrayList<BookItem> bookItems, selectedBooks;
    private LibraryBooks libraryBooks;
    private LibraryAdapter adapter;
    private Toolbar toolbar;
    private SearchView searchView;
    private static boolean selectionMode;
    private MenuItem delete;

    @Override
    public void selection(int selected) {
        if (selected != 0) {
            this.toolbar.getMenu().findItem(R.id.libraryDelete).setVisible(true);
            this.toolbar.getMenu().findItem(R.id.librarySearch).setVisible(false);
            this.toolbar.setTitle(Integer.toString(selected));
            getActivity().invalidateOptionsMenu();
            selectionMode = true;
        } else {
            Log.d("Library", "Selection Exited");
            this.toolbar.getMenu().findItem(R.id.libraryDelete).setVisible(false);
            this.toolbar.getMenu().findItem(R.id.librarySearch).setVisible(true);
            this.toolbar.setTitle("Library");
            Log.d("Library", toolbar.getTitle().toString());
            selectionMode = false;
            getActivity().invalidateOptionsMenu();
        }
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                selection(0);
                adapter.reset();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_book_library, container, false);
        return view;
    }

    @Override
    public void onResume() {
        Log.d("Library Fragment", "Resumed");
        adapter = new LibraryAdapter(bookItems, getActivity(), this);
        recyclerView.setAdapter(adapter);
        super.onResume();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.libraryToolbar);
        toolbar.inflateMenu(R.menu.library_menu);
        toolbar.setTitle("Library");
        searchView = toolbar.findViewById(R.id.librarySearch);
        delete = toolbar.getMenu().findItem(R.id.libraryDelete);

        libraryBooks = new LibraryBooks(getActivity());
        bookItems = libraryBooks.bookList();

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new LibraryAdapter(bookItems, getActivity(), this);
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
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                deleteBooks();
                return true;
            }
        });
    }
    private void deleteBooks() {
        Log.d("Library", "Deleted");

        selectedBooks = adapter.getSelectedBooks();
        Log.d("Library", "Deleted book: " + selectedBooks.get(0));
        for (BookItem bookItem : selectedBooks) {
            Log.d("Library", "Removed book: " + bookItem.getTitle());
            libraryBooks.removeBook(bookItem);
            bookItems.remove(bookItem);
        }

        selection(0);
        adapter.resetSelected();
        adapter.notifyDataSetChanged();
    }

    private void filterBooks(String query) {
        ArrayList<BookItem> filteredBooks = new ArrayList<>();
        for (BookItem book : bookItems) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredBooks.add(book);
            }
        }
        if (!filteredBooks.isEmpty()) {
            recyclerView.setAdapter(new LibraryAdapter(filteredBooks, getActivity(), this));
        }
    }



}