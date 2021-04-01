package com.example.lab7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class BookSearchActivity extends DialogFragment {
    // interface
    SearchListenerInterface listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // set the layout for the popup search dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_book_search, null))
        // create search button
        .setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // do search here
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
        // make sure the parent implements the searchListener interface
        try {
            // create the searchListener
            listener = (SearchListenerInterface) context;
        } catch (ClassCastException e) {}
    }

    // parent must implement this interface to know what is happening in the dialog
    public interface SearchListenerInterface {
        public void onSearch(DialogFragment dialog);
        public void onCancel(DialogFragment dialog);
    }

}