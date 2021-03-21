package com.example.lab7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    BookList books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);

        // create BookList
        books = new BookList();

        // populate the BookList
        for(int i = 0; i < 10; i++) {
            Book book = new Book(String.valueOf(i), String.valueOf(i));
            books.addBook(book);
        }
    }
}