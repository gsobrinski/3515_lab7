package com.example.lab7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookDetailsFragment extends Fragment {

    String title = "";
    String author = "";
    String coverURL = "";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String COVER_URL = "coverURL";

    TextView titleText;
    TextView authorText;
    ImageView coverImage;

    Context context;

    public BookDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context mainContext) {
        super.onAttach(mainContext);
        this.context = mainContext;
    }

    // FACTORY METHOD
    public static BookDetailsFragment newInstance(String title, String author, String coverURL) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(AUTHOR, author);
        args.putString(COVER_URL, coverURL);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            author = getArguments().getString(AUTHOR);
            coverURL = getArguments().getString(COVER_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);

        titleText = view.findViewById(R.id.bookTitle);
        authorText = view.findViewById(R.id.bookAuthor);
        coverImage = view.findViewById(R.id.imageView);
        titleText.setGravity(Gravity.CENTER);
        authorText.setGravity(Gravity.CENTER);
        setBookDetails(title, author, coverURL);
        return view;
    }

    public void setBookDetails(String title, String author, String coverURL) {
        titleText.setText(title);
        authorText.setText(author);
        // convert URL to Bitmap
        Picasso.get().load(coverURL).into(coverImage);
    }

}