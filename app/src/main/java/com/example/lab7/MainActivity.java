package com.example.lab7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface, BookSearchActivity.SearchListenerInterface {

    BookDetailsFragment bdFragment;
    BookListFragment blFragment;
    FragmentManager fragmentManager;

    boolean landscape;

    public static final String SAVED_TITLE = "saved_title";
    public static final String SAVED_AUTHOR = "saved_author";
    public static final String SAVED_COVER_URL = "saved_cover_url";
    public static final String SAVED_BOOKLIST = "saved_booklist";
    String title;
    String author;
    String coverURL;
    BookList bookList;

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
            // retrieve booklist from saved instance state
            try {
                String jsonString = savedInstanceState.getString(SAVED_BOOKLIST);
                JSONArray jsonArr = new JSONArray(jsonString);
                bookList = jsonToBooks(jsonArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // check if title and author have been created
            if(title != null && author != null && coverURL != null) {
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
    public void getClickedBook(String title, String author, String coverURL) {
        this.title = title;
        this.author = author;
        this.coverURL = coverURL;
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
    public void onSearch(DialogFragment dialog, BookList bookList) {
        this.bookList = bookList;
        if (blFragment == null) {
            System.out.println("Creating new BookListFragment in onSearch");
            blFragment = BookListFragment.newInstance(bookList);
        } else {
            System.out.println("Updating booklist in onSearch");
            blFragment.updateDataset(bookList);
        }
    }

    @Override
    public void onCancel(DialogFragment dialog) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_TITLE, title);
        outState.putString(SAVED_AUTHOR, author);
        outState.putString(SAVED_COVER_URL, coverURL);
        // store json string version of booklist
        JSONArray books = booksToJson();
        outState.putString(SAVED_BOOKLIST, books.toString());
    }

    private JSONArray booksToJson() {
        JSONArray jsonArray = new JSONArray();
        for (int i=0; i < bookList.getSize(); i++) {
            jsonArray.put(bookList.getBook(i).getJSONObject());
        }
        return jsonArray;
    }

    private BookList jsonToBooks(JSONArray books) {
        BookList bookList = new BookList();
        for (int i = 0; i < books.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = books.getJSONObject(i);
                // add all book fields to a Book object and then add that Book to the BookList
                Book book = new Book(
                        Integer.parseInt(jsonObject.getString("id")),
                        jsonObject.getString("title"),
                        jsonObject.getString("author"),
                        jsonObject.getString("cover_url"));
                bookList.addBook(book);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bookList;
    }

//    // convert BookList to ArrayList<Book>
//    private ArrayList<Book> bookToArray(BookList bookList) {
//        ArrayList<Book> books = new ArrayList<>();
//        for(int i = 0; i < bookList.getSize(); i++) {
//            books.add(bookList.getBook(i));
//        }
//        return books;
//    }
//
//    // convert ArrayList<Book> to BookList
//    private BookList arrayToBook(ArrayList<Book> books) {
//        BookList bookList = new BookList();
//        for(Book book : books) {
//            bookList.addBook(book);
//        }
//        return bookList;
//    }
}