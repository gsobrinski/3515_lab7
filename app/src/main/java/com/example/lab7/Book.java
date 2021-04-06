package com.example.lab7;

// Book class that returns a Book object

import org.json.JSONException;
import org.json.JSONObject;

public class Book {
    private String title;
    private String author;
    private int id;
    private String coverURL;
    private int duration;

    public Book(int id, String title, String author, String coverURL, int duration) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.coverURL = coverURL;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() { return id; }

    public String getCoverURL() { return coverURL; }

    public int getDuration() { return duration; }

    public void setDuration(int duration) { this.duration = duration; }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("title", title);
            obj.put("author", author);
            obj.put("id", id);
            obj.put("coverURL", coverURL);
            obj.put("duration", duration);
        } catch (JSONException e) { e.printStackTrace(); }
        return obj;
    }
}
