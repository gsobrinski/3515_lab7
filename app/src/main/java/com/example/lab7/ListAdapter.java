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
    ArrayList<String> titles;
    ArrayList<String> authors;

    LayoutInflater inflater;

    public ListAdapter(Context context, ArrayList titles, ArrayList authors) {
        this.context = context;
        this.titles = titles;
        this.authors = authors;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        Pair book = new Pair(titles.get(position), authors.get(position));
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

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
            title = (TextView) convertView.findViewById(R.id.title);
            author = (TextView) convertView.findViewById(R.id.author);

            title.setText(titles.get(position));
            author.setText(authors.get(position));

        }

        return convertView;
    }
}
