package com.example.lab7;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class BookListFragment extends Fragment {

    private BookList bookList;
    // displays the BookList
    private ListView listView;
    Context context;

    ArrayList<String> books;

    public BookListFragment() {

    }

    @Override
    public void onAttach(Context mainContext) {
        super.onAttach(mainContext);
        this.context = mainContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the ListView layout
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        listView = view.findViewById(R.id.listView);

        // get String array as ArrayList
        books = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.book_titles)));

        // create adapter to turn string-array into listView
        listView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, books));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bookTitle = (String) parent.getItemAtPosition(position);

                // use MainActivity's method in this context
                BookListInterface blInterface = (BookListInterface) context;
                blInterface.getClickedBook(bookTitle);
            }
        });

        return view;
    }

    interface BookListInterface {
        void getClickedBook(String bookTitle);
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment BookListFragment.
//     */

//    public static BookListFragment newInstance(String param1, String param2) {
//        BookListFragment fragment = new BookListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
}