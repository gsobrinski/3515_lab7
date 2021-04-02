package com.example.lab7;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookListFragment extends Fragment {

    private BookList bookList;
    // displays the BookList
    ListView listView;
    ListAdapter adapter;
    Context context;

    public static final String BOOKS = "books";

    public BookListFragment() {

    }

    @Override
    public void onAttach(@NonNull Context mainContext) {
        super.onAttach(mainContext);
        this.context = mainContext;
    }

    // FACTORY METHOD
    public static BookListFragment newInstance() {
        BookListFragment blFragment = new BookListFragment();
        blFragment.bookList = new BookList();
        Bundle args = new Bundle();
        JSONArray books = MainActivity.booksToJson(new BookList());
        args.putString(BOOKS, books.toString());
        blFragment.setArguments(args);
        return blFragment;
    }

    // factory method for updating booklist
    public static BookListFragment newInstance(BookList bookList) {
        System.out.println("booklist in blfragment: " + bookList);
        BookListFragment blFragment = new BookListFragment();
        blFragment.bookList = bookList;
        Bundle args = new Bundle();
        // store json string version of booklist
        JSONArray books = MainActivity.booksToJson(bookList);
        args.putString(BOOKS, books.toString());
        blFragment.setArguments(args);
        return blFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String books = getArguments().getString(BOOKS);
            // convert back to BookList
            try {
                JSONArray jsonArr = new JSONArray(books);
                bookList = MainActivity.jsonToBooks(jsonArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the ListView layout
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        listView = view.findViewById(R.id.listView);

        adapter = new ListAdapter(context, bookList);

        // create adapter to turn string-array into listView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) parent.getItemAtPosition(position);
                String title = (String) book.getTitle();
                String author = (String) book.getAuthor();
                String coverURL = (String) book.getCoverURL();
                // use MainActivity's method in this context
                BookListInterface blInterface = (BookListInterface) context;
                blInterface.getClickedBook(title, author, coverURL);
            }
        });

        return view;
    }

    public void updateDataset(BookList bookList) {
        System.out.println("booklist in updatedataset: " + bookList);
        this.bookList = bookList;
        adapter.updateDataset(bookList);
    }

    interface BookListInterface {
        void getClickedBook(String title, String author, String coverURL);
    }
}