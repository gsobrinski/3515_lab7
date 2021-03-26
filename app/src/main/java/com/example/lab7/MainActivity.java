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

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            blFragment = BookListFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.frame1, blFragment).addToBackStack(null).commit();

            if (landscape) {
                bdFragment = BookDetailsFragment.newInstance(null, null);
                fragmentManager.beginTransaction().replace(R.id.frame2, bdFragment).addToBackStack(null).commit();
            }
        } else {
            // retrieve saved instance state
            title = savedInstanceState.getString(SAVED_TITLE);
            author = savedInstanceState.getString(SAVED_AUTHOR);
            // check if title and author have been created
            if(title != null && author != null) {
                // if landscape/tablet
                if (landscape) {
                    // reuse bdFragment if possible
                    if (bdFragment == null) {
                        bdFragment = BookDetailsFragment.newInstance(title, author);
                    } else {
                        bdFragment.setBookDetails(title, author);
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame2, bdFragment).addToBackStack(null).commit();

                    // reuse blFragment if possible
                    if (blFragment == null) {
                        blFragment = BookListFragment.newInstance();
                        System.out.println("NEW BOOKLIST");
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame1, blFragment).commit();

                // else: small portrait
                } else {
                    // reuse bdFragment if possible
                    if(bdFragment == null) {
                        bdFragment = BookDetailsFragment.newInstance(title, author);
                    } else {
                        bdFragment.setBookDetails(title, author);
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame1, bdFragment).addToBackStack(null).commit();
                }
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
            // check if bdfragment is null
            if(bdFragment == null) {
                bdFragment = BookDetailsFragment.newInstance(title, author);
            } else {
                bdFragment.setBookDetails(title, author);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_TITLE, title);
        outState.putString(SAVED_AUTHOR, author);
    }
}