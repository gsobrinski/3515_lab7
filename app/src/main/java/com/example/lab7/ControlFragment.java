package com.example.lab7;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends Fragment {

    int id;
    int duration;
    int current;
    public static final String ID = "id";
    public static final String DURATION = "duration";
    public static final String CURRENT = "current";

    public ControlFragment() {
        // Required empty public constructor
    }

    // FACTORY METHODS
    public static ControlFragment newInstance() {
        ControlFragment fragment = new ControlFragment();
        return fragment;
    }
    public static ControlFragment newInstance(int id, int duration, int current) {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        // add arguments to bundle
        args.putInt(ID, id);
        args.putInt(DURATION, duration);
        args.putInt(CURRENT, current);
        // set args and return fragment
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ID);
            duration = getArguments().getInt(DURATION);
            current = getArguments().getInt(CURRENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_control, container, false);
    }
}