package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface {

    BookListFragment blFragment;
    FragmentManager fragmentManager;

    boolean landscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);

        // create the BookListFragment - this always appears on the first page regardless of orientation
        blFragment = new BookListFragment();

        landscape = findViewById(R.id.frame2) == null;


        if (landscape) {
            fragmentManager.beginTransaction().replace(R.id.frame2, blFragment).commit();
        }

    }

    // implemented from BookListFragment's interface
    @Override
    public void getClickedBook(String bookTitle) {

    }
}