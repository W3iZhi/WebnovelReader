package com.example.webnovelreader.BookLibrary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_library, container, false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        libraryBooks = new LibraryBooks(getActivity());
        bookItems = libraryBooks.bookList();

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new LibraryAdapter(bookItems, getActivity());
        recyclerView.setAdapter(adapter);
    }
}