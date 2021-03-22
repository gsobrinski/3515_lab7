package com.example.lab7;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BookDetailsFragment extends Fragment {

    String title = "";
    String author = "";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    TextView textView;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(String title, String author) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(AUTHOR, author);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            author = getArguments().getString(AUTHOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);

        textView = view.findViewById(R.id.textView);
        textView.setGravity(Gravity.CENTER);
        return view;
    }

    public void setBookDetails(String author, String title) {
        textView.setText(title + "\n" + author);
    }
}