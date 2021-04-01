package com.example.lab7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface, BookSearchActivity.SearchListenerInterface {

    BookDetailsFragment bdFragment;
    BookListFragment blFragment;
    FragmentManager fragmentManager;

    boolean landscape;

    public static final String SAVED_TITLE = "saved_title";
    public static final String SAVED_AUTHOR = "saved_author";
    public static final String SAVED_COVER_URL = "saved_cover_url";
    String title;
    String author;
    String coverURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);

        Button searchButton = (Button) findViewById(R.id.button);

        landscape = findViewById(R.id.frame2) != null;

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            blFragment = BookListFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.frame1, blFragment).addToBackStack(null).commit();

            if (landscape) {
                bdFragment = BookDetailsFragment.newInstance(null, null, null);
                fragmentManager.beginTransaction().replace(R.id.frame2, bdFragment).addToBackStack(null).commit();
            }
        } else {
            // retrieve saved instance state
            title = savedInstanceState.getString(SAVED_TITLE);
            author = savedInstanceState.getString(SAVED_AUTHOR);
            coverURL = savedInstanceState.getString(SAVED_COVER_URL);
            // check if title and author have been created
            if(title != null && author != null) {
                // if landscape/tablet
                if (landscape) {
                    // reuse bdFragment if possible
                    if (bdFragment == null) {
                        bdFragment = BookDetailsFragment.newInstance(title, author, coverURL);
                    } else {
                        bdFragment.setBookDetails(title, author, coverURL);
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
                        bdFragment = BookDetailsFragment.newInstance(title, author, coverURL);
                    } else {
                        bdFragment.setBookDetails(title, author, coverURL);
                    }
                    fragmentManager.beginTransaction().replace(R.id.frame1, bdFragment).addToBackStack(null).commit();
                }
            }
        }

        // trigger the search dialog
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new BookSearchActivity();
                dialog.show(getSupportFragmentManager(), "BookSearchActivity");
            }
        });
    }

    // implemented from BookListFragment's interface
    @Override
    public void getClickedBook(String title, String author) {
        this.title = title;
        this.author = author;
        if(!landscape) {
            BookDetailsFragment bdFragment = BookDetailsFragment.newInstance(title, author, coverURL);
            fragmentManager.beginTransaction().replace(R.id.frame1, bdFragment).addToBackStack(null).commit();
        } else {
            // check if bdfragment is null
            if(bdFragment == null) {
                bdFragment = BookDetailsFragment.newInstance(title, author, coverURL);
            } else {
                bdFragment.setBookDetails(title, author, coverURL);
            }
        }
    }

    // implemented from BookSearchActivity's interface
    @Override
    public void onSearch(DialogFragment dialog) {
        System.out.println("SEARCH");
    }

    @Override
    public void onCancel(DialogFragment dialog) {
        System.out.println("CANCEL");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_TITLE, title);
        outState.putString(SAVED_AUTHOR, author);
        outState.putString(SAVED_COVER_URL, coverURL);
    }
}