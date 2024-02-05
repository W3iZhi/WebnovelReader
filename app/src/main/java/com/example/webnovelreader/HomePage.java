package com.example.webnovelreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.webnovelreader.BookLibrary.BookLibrary;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomePage extends AppCompatActivity {
    BookLibrary library = new BookLibrary();
    SourceSelect search = new SourceSelect();
    AppSettings settings = new AppSettings();
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, library)
                .commit();
//        bottomNavigationView.setSelectedItemId(R.id.search);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragment = null;
                if(item.getItemId() == R.id.library) {
                    selectFragment = library;
                } else if (item.getItemId() == R.id.search) {
                    selectFragment = search;
                } else if (item.getItemId() == R.id.settings) {
                    selectFragment = settings;
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, selectFragment)
                        .commit();
                return true;
            }
        });
    }
    public void chooseSource (View source) {
        Intent intent = new Intent(this, BookSelect.class);
        if(source.getId() == R.id.royalroadSource) {
            intent.putExtra("source", "royalroad");
        }
        startActivity(intent);
    }
}