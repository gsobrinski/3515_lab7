package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface {

    BookDetailsFragment bdFragment;
    FragmentManager fragmentManager;

    boolean landscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);

        if (findViewById(R.id.frame2) == null) {
            landscape = false;
        } else {
            landscape = true;
        }
        bdFragment = new BookDetailsFragment();

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.frame1, new BookListFragment()).commit();

        if (landscape) {
            fragmentManager.beginTransaction().replace(R.id.frame2, bdFragment).commit();
        }

    }

    // implemented from BookListFragment's interface
    @Override
    public void getClickedBook(String title, String author) {
        if(!landscape) {
            BookDetailsFragment bdFragment = BookDetailsFragment.newInstance(title, author);
            fragmentManager.beginTransaction().replace(R.id.frame1, bdFragment).addToBackStack(null).commit();
        } else {

        }
    }
}