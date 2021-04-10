package com.example.lab7;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ControlFragment extends Fragment {
    Context context;
    View view;
    ImageButton pauseButton;
    ImageButton playButton;
    ImageButton stopButton;
    TextView nowPlaying;
    SeekBar progressSeekBar;

    int id;
    int duration;
    int current;
    public static final String ID = "id";
    public static final String DURATION = "duration";
    public static final String CURRENT = "current";

    ControlInterface controlInterface;

    public ControlFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context mainContext) {
        super.onAttach(mainContext);
        this.context = mainContext;
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
        view = inflater.inflate(R.layout.fragment_control, container, false);
        initViews();

        // create interface to interact with MainActivity
        controlInterface = (ControlInterface) context;

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlInterface.playAudioBook();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlInterface.pauseAudioBook();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlInterface.stopAudioBook();
            }
        });

        progressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    //progressSeekBar.setProgress(progress);
                    controlInterface.seekAudioBook(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    public void setAudioBook(int id, int duration, int current) {
        this.id = id;
        this.duration = duration;
        this.current = current;
    }

    public void setTitle(String title) {
        nowPlaying.setText(title);
    }

    public void setProgress(int progress, int duration){
        progressSeekBar.setMax(duration);
        progressSeekBar.setProgress(progress);
    }

    private void initViews() {
        pauseButton = view.findViewById(R.id.pauseButton);
        playButton = view.findViewById(R.id.playButton);
        stopButton = view.findViewById(R.id.stopButton);
        nowPlaying = view.findViewById(R.id.nowPlaying);
        progressSeekBar = view.findViewById(R.id.seekBar);
    }

    interface ControlInterface {
        void playAudioBook();
        void pauseAudioBook();
        void stopAudioBook();
        void seekAudioBook(int progress);
    }
}