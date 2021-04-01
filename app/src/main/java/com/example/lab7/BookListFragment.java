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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookListFragment extends Fragment {

    private BookList bookList;
    // displays the BookList
    private ListView listView;
    Context context;

    // book title and author
    ArrayList<String> books;
    ArrayList<String> authors;

    public static final String TITLES = "titles";
    public static final String AUTHORS = "authors";

    public BookListFragment() {

    }

    @Override
    public void onAttach(Context mainContext) {
        super.onAttach(mainContext);
        this.context = mainContext;
    }

    // FACTORY METHOD
    public static BookListFragment newInstance() {
        return new BookListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the ListView layout
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        listView = view.findViewById(R.id.listView);

        // get title and author ArrayLists from strings.xml
//        books = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.book_titles)));
//        authors = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.book_authors)));

        ListAdapter adapter = new ListAdapter(context, books, authors);

        // create adapter to turn string-array into listView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pair book = (Pair) parent.getItemAtPosition(position);
                String title = (String) book.first;
                String author = (String) book.second;
                // use MainActivity's method in this context
                BookListInterface blInterface = (BookListInterface) context;
                blInterface.getClickedBook(title, author);
            }
        });

        return view;
    }

    interface BookListInterface {
        void getClickedBook(String title, String author);
    }
}