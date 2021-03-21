package com.example.lab7;

// BookList class that stores multiple Book objects in an ArrayList

import java.util.ArrayList;

public class BookList {

    private ArrayList<Book> books;

    public BookList() {
        books = new ArrayList<Book>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public Book getBook(int index) {
        return books.get(index);
    }

    public int getSize() {
        return books.size();
    }
}
