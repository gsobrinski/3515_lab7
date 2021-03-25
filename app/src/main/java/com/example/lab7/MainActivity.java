package com.example.lab7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface {

    BookDetailsFragment bdFragment;
    BookListFragment blFragment;
    FragmentManager fragmentManager;

    boolean landscape;

    public static final String SAVED_TITLE = "saved_title";
    public static final String SAVED_AUTHOR = "saved_author";
    String title;
    String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);

        landscape = findViewById(R.id.frame2) != null;

        bdFragment = new BookDetailsFragment();
        blFragment = new BookListFragment();

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.frame1, blFragment).addToBackStack(null).commit();

            if (landscape) {
                fragmentManager.beginTransaction().replace(R.id.frame2, bdFragment).addToBackStack(null).commit();
            }
        } else {
            title = savedInstanceState.getString(SAVED_TITLE);
            author = savedInstanceState.getString(SAVED_AUTHOR);
            if(landscape) {
                bdFragment = BookDetailsFragment.newInstance(title, author);
                fragmentManager.beginTransaction().replace(R.id.frame2, bdFragment).addToBackStack(null).commit();
                fragmentManager.beginTransaction().replace(R.id.frame1, new BookListFragment()).commit();
            } else {
                fragmentManager.beginTransaction().replace(R.id.frame1, BookDetailsFragment.newInstance(title, author)).addToBackStack(null).commit();
            }
        }

    }

    // implemented from BookListFragment's interface
    @Override
    public void getClickedBook(String title, String author) {
        this.title = title;
        this.author = author;
        if(!landscape) {
            BookDetailsFragment bdFragment = BookDetailsFragment.newInstance(title, author);
            fragmentManager.beginTransaction().replace(R.id.frame1, bdFragment).addToBackStack(null).commit();
        } else {
            bdFragment.setBookDetails(title, author);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_TITLE, title);
        outState.putString(SAVED_AUTHOR, author);
    }
}