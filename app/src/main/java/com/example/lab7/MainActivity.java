package com.example.lab7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListInterface, BookSearchActivity.SearchListenerInterface, ControlFragment.ControlInterface {

    // FRAGMENTS
    BookDetailsFragment bdFragment;
    BookListFragment blFragment;
    ControlFragment cFragment;
    FragmentManager fragmentManager;

    boolean landscape;

    // SAVED STATE KEYS/VALUES
    public static final String SAVED_TITLE = "saved_title";
    public static final String SAVED_AUTHOR = "saved_author";
    public static final String SAVED_COVER_URL = "saved_cover_url";
    public static final String SAVED_DURATION = "saved_duration";
    public static final String SAVED_BOOKLIST = "saved_booklist";
    public static final String SAVED_ID = "saved_id";
    public static final String SAVED_PROGRESS = "saved_progress";
    String title;
    String author;
    String coverURL;
    int duration;
    int id;
    BookList bookList;

    // AUDIO PLAYER
    AudiobookService.MediaControlBinder mcBinder;
    ServiceConnection activeConnection = null;
    int progress = 0;

    // book progress handler
    Handler progressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            AudiobookService.BookProgress bookProgress = (AudiobookService.BookProgress) msg.obj;
            if(bookProgress != null) {
                progress = bookProgress.getProgress();
            }
            if(cFragment != null) {
                cFragment.setProgress(progress, duration);
            }
            return true;
        }
    });

    ServiceConnection playConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            activeConnection = playConnection;
            mcBinder = (AudiobookService.MediaControlBinder) service;

            // if partway through the book
            if(progress > 0) {
                mcBinder.play(id, progress);
            // if starting a new book
            } else {
                mcBinder.play(id);
                progress = 0;
                if(cFragment != null) {
                    cFragment.setProgress(progress, duration);
                }
            }
            // set up the progress handler to receive messages
            mcBinder.setProgressHandler(progressHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    ServiceConnection pauseConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            activeConnection = pauseConnection;
            mcBinder = (AudiobookService.MediaControlBinder) service;
            mcBinder.pause();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    ServiceConnection stopConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            activeConnection = stopConnection;
            mcBinder = (AudiobookService.MediaControlBinder) service;
            mcBinder.stop();
            progress = 0;
            if(cFragment != null) {
                cFragment.setProgress(progress, duration);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    ServiceConnection seekConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            activeConnection = seekConnection;
            mcBinder = (AudiobookService.MediaControlBinder) service;
            mcBinder.seekTo(progress);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onStart(){
        super.onStart();
        Intent serviceIntent = new Intent(this, AudiobookService.class);
        startService(serviceIntent);
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    // implemented from ControlFragment's interface
    @Override
    public void playAudioBook() {
        if(id == 0) {
            Toast.makeText(MainActivity.this, "You have not selected a book!", Toast.LENGTH_SHORT).show();
        } else {
            if(activeConnection != null) {
                unbindService(activeConnection);
            }
            //cFragment.setTitle(title);
            Intent serviceIntent = new Intent(this, AudiobookService.class);
            bindService(serviceIntent, playConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void pauseAudioBook() {
        if(activeConnection != null) {
            unbindService(activeConnection);
        }
        Intent serviceIntent = new Intent(this, AudiobookService.class);
        bindService(serviceIntent, pauseConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void stopAudioBook() {
        //cFragment.setTitle("");
        if(activeConnection != null) {
            unbindService(activeConnection);
        }
        Intent serviceIntent = new Intent(this, AudiobookService.class);
        bindService(serviceIntent, stopConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void seekAudioBook(int progress) {
        if(activeConnection != null) {
            unbindService(activeConnection);
        }
        this.progress = progress;
        Intent serviceIntent = new Intent(this, AudiobookService.class);
        bindService(serviceIntent, seekConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);

        Button searchButton = (Button) findViewById(R.id.button);

        landscape = findViewById(R.id.frame2) != null;

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            blFragment = BookListFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.frame1, blFragment).addToBackStack(null).commit();

            if (landscape) {
                bdFragment = BookDetailsFragment.newInstance(null, null, null);
                fragmentManager.beginTransaction().replace(R.id.frame2, bdFragment).addToBackStack(null).commit();
            }

            cFragment = ControlFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.control, cFragment).addToBackStack(null).commit();
        } else {
            // retrieve saved instance state
            title = savedInstanceState.getString(SAVED_TITLE);
            author = savedInstanceState.getString(SAVED_AUTHOR);
            coverURL = savedInstanceState.getString(SAVED_COVER_URL);
            duration = savedInstanceState.getInt(SAVED_DURATION);
            id = savedInstanceState.getInt(SAVED_ID);
            progress = savedInstanceState.getInt(SAVED_PROGRESS);
            System.out.println("id in mainactivity saved state: " + id);

            // retrieve booklist from saved instance state
            if(savedInstanceState.getString(SAVED_BOOKLIST) != null) {
                try {
                    String jsonString = savedInstanceState.getString(SAVED_BOOKLIST);
                    System.out.println("jsonstring in saved instance state: " + jsonString);
                    JSONArray jsonArr = new JSONArray(jsonString);
                    bookList = jsonToBooks(jsonArr);
                    if (blFragment != null) {
                        blFragment.updateDataset(bookList);
                    }
                    //Book book = bookList.getBook(0);
                    //System.out.println("first book in saved instance state: " + book.getTitle() + " " + book.getAuthor() + " " + book.getCoverURL());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                bookList = new BookList();
            }

            cFragment = ControlFragment.newInstance(id, duration, 0);
            cFragment.setAudioBook(id, duration, 0);

            // if landscape/tablet
            if (landscape) {
                // reuse bdFragment if possible
                if (bdFragment == null) {
                    bdFragment = BookDetailsFragment.newInstance(title, author, coverURL);
                } else {
                    bdFragment.setBookDetails(title, author, coverURL);
                }
                fragmentManager.beginTransaction().replace(R.id.frame2, bdFragment).addToBackStack(null).commit();

                // reuse blFragment if possible
                if (blFragment == null) {
                    blFragment = BookListFragment.newInstance(bookList);
                } else {
                    blFragment.updateDataset(bookList);
                }
                fragmentManager.beginTransaction().replace(R.id.frame1, blFragment).addToBackStack(null).commit();

            // else: small portrait
            } else {
                // reuse bdFragment if possible
                if(bdFragment == null) {
                    bdFragment = BookDetailsFragment.newInstance(title, author, coverURL);
                } else {
                    bdFragment.setBookDetails(title, author, coverURL);
                }
                fragmentManager.beginTransaction().replace(R.id.frame1, bdFragment).addToBackStack(null).commit();

                // reuse blFragment if possible
                if (blFragment == null) {
                    blFragment = BookListFragment.newInstance(bookList);
                } else {
                    blFragment.updateDataset(bookList);
                }
                // show booklist if no book has been clicked yet
                if(title == null) {
                    fragmentManager.beginTransaction().replace(R.id.frame1, blFragment).commit();
                } else {
                    fragmentManager.beginTransaction().replace(R.id.frame1, bdFragment).addToBackStack(null).commit();
                }
            }
        }
        // trigger the search dialog
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new BookSearchActivity();
                dialog.show(getSupportFragmentManager(), "BookSearchActivity");
            }
        });
    }

    // implemented from BookListFragment's interface
    @Override
    public void getClickedBook(String title, String author, String coverURL, int id, int duration) {
        this.title = title;
        this.author = author;
        this.coverURL = coverURL;
        this.id = id;
        this.duration = duration;
        progress = 0;
        if(!landscape) {
            BookDetailsFragment bdFragment = BookDetailsFragment.newInstance(title, author, coverURL);
            fragmentManager.beginTransaction().replace(R.id.frame1, bdFragment).addToBackStack(null).commit();
        } else {
            // check if bdfragment is null
            if(bdFragment == null) {
                bdFragment = BookDetailsFragment.newInstance(title, author, coverURL);
            } else {
                bdFragment.setBookDetails(title, author, coverURL);
            }
        }
    }

    // implemented from BookSearchActivity's interface
    @Override
    public void onSearch(DialogFragment dialog, BookList bookList) {

        this.bookList = bookList;
        if (blFragment == null || blFragment.adapter == null) {
            blFragment = BookListFragment.newInstance(bookList);
        } else {
            Book book = bookList.getBook(0);
            blFragment.updateDataset(bookList);
        }
        fragmentManager.beginTransaction().replace(R.id.frame1, blFragment).addToBackStack(null).commit();
    }

    @Override
    public void onCancel(DialogFragment dialog) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_TITLE, title);
        outState.putString(SAVED_AUTHOR, author);
        outState.putString(SAVED_COVER_URL, coverURL);
        outState.putInt(SAVED_DURATION, duration);
        outState.putInt(SAVED_ID, id);
        outState.putInt(SAVED_PROGRESS, progress);
        if(bookList != null) {
            // store json string version of booklist
            JSONArray books = booksToJson(this.bookList);
            outState.putString(SAVED_BOOKLIST, books.toString());
        }
    }

    public static JSONArray booksToJson(BookList bookList) {
        JSONArray jsonArray = new JSONArray();
        for (int i=0; i < bookList.getSize(); i++) {
            jsonArray.put(bookList.getBook(i).getJSONObject());
        }
        return jsonArray;
    }

    public static BookList jsonToBooks(JSONArray books) {
        BookList bookList = new BookList();
        for (int i = 0; i < books.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = books.getJSONObject(i);
                // add all book fields to a Book object and then add that Book to the BookList
                Book book = new Book(
                        Integer.parseInt(jsonObject.getString("id")),
                        jsonObject.getString("title"),
                        jsonObject.getString("author"),
                        jsonObject.getString("coverURL"),
                        Integer.parseInt(jsonObject.getString("duration")));
                bookList.addBook(book);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return bookList;
    }
}