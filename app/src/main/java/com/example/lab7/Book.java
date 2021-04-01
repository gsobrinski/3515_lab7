package com.example.lab7;

// Book class that returns a Book object

public class Book {
    private String title;
    private String author;
    private int id;
    private String coverURL;

    public Book(String title, String author, int id, String coverURL) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() { return id; }

    public String getCoverURL() { return coverURL; }
}
