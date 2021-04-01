package com.example.lab7;

import android.content.Context;
import android.text.Layout;
import androidx.core.util.Pair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    Context context;
    BookList bookList;

    LayoutInflater inflater;

    public ListAdapter(Context context, BookList bookList) {
        this.context = context;
        this.bookList = bookList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bookList.getSize();
    }

    @Override
    public Object getItem(int position) {
        Book book = bookList.getBook(position);
        return book;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView title;
        TextView author;

        View view = inflater.inflate(R.layout.list_item_layout, parent, false);
        title = (TextView) view.findViewById(R.id.title);
        author = (TextView) view.findViewById(R.id.author);

        Book book = (Book) getItem(position);

        title.setText(book.getTitle());
        author.setText(book.getAuthor());

        return view;
    }

    public void updateDataset(BookList bookList) {
        this.bookList = bookList;
        this.notifyDataSetChanged();
    }
}
