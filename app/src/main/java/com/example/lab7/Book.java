package com.example.lab7;

// Book class that returns a Book object

import org.json.JSONException;
import org.json.JSONObject;

public class Book {
    private String title;
    private String author;
    private int id;
    private String coverURL;

    public Book(int id, String title, String author, String coverURL) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverURL = coverURL;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() { return id; }

    public String getCoverURL() { return coverURL; }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("title", title);
            obj.put("author", author);
            obj.put("id", id);
            obj.put("coverURL", coverURL);
        } catch (JSONException e) { e.printStackTrace(); }
        return obj;
    }
}
