package com.example.lab7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.StringBufferInputStream;
import java.util.ArrayList;

public class BookSearchActivity extends DialogFragment {
    Context context;
    // interface
    SearchListenerInterface listener;

    EditText editText;
    RequestQueue requestQueue;

    BookList bookList = new BookList();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set the layout for the popup search dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_book_search, null);
        editText = (EditText) view.findViewById(R.id.searchEditText);
        requestQueue = Volley.newRequestQueue(context);
        builder.setView(view)
        // create search button
        .setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // do search here
                String searchTerm = editText.getText().toString();
                String urlSearch = "https://kamorris.com/lab/cis3515/search.php?term=" + searchTerm;

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlSearch, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        bookList = new BookList();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject(i);
                                // add all book fields to a Book object and then add that Book to the BookList
                                Book book = new Book(
                                        Integer.parseInt(jsonObject.getString("id")),
                                        jsonObject.getString("title"),
                                        jsonObject.getString("author"),
                                        jsonObject.getString("cover_url"),
                                        Integer.parseInt(jsonObject.getString("duration")));
                                bookList.addBook(book);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // send the booklist to mainactivity using the interface
                        listener.onSearch(BookSearchActivity.this, bookList);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println("ERROR: " + error.toString());
                    }
                });
                requestQueue.add(jsonArrayRequest);

            }
        })
                // create cancel button
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // cancel the search - go back to previous fragment
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        // make sure the parent implements the searchListener interface
        try {
            // create the searchListener
            listener = (SearchListenerInterface) context;
        } catch (ClassCastException e) {}
    }

    // parent must implement this interface to know what is happening in the dialog
    public interface SearchListenerInterface {
        public void onSearch(DialogFragment dialog, BookList bookList);
        public void onCancel(DialogFragment dialog);
    }

}