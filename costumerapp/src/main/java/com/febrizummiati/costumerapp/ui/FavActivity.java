package com.febrizummiati.costumerapp.ui;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.febrizummiati.costumerapp.R;
import com.febrizummiati.costumerapp.adapter.AdapterFavorite;
import com.febrizummiati.costumerapp.model.User;
import com.febrizummiati.costumerapp.util.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.febrizummiati.costumerapp.database.DatabaseContract.UserColumns.CONTENT_URI;

public class FavActivity extends AppCompatActivity implements LoadUserCallback {

    private ProgressBar progressBar;
    private AdapterFavorite adapterFavorite;

    private static final String EXTRA_STATE = "EXTRA_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        getSupportActionBar().setTitle(getResources().getString(R.string.user_favorite));

        progressBar = findViewById(R.id.fav_progressBar);

        RecyclerView rvFavorite = findViewById(R.id.rv_favorite);

        rvFavorite.setLayoutManager(new LinearLayoutManager(this));
        rvFavorite.setHasFixedSize(true);

        adapterFavorite = new AdapterFavorite();
        rvFavorite.setAdapter(adapterFavorite);


        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver dataObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, dataObserver);

        if (savedInstanceState == null) {
            new LoadUserAsync(this, this).execute();
        } else {
            ArrayList<User> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapterFavorite.setListUser(list);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapterFavorite.getListUser());
    }


    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<User> userLocal) {
        progressBar.setVisibility(View.INVISIBLE);
        if (userLocal.size() > 0) {
            adapterFavorite.setListUser(userLocal);
        } else {
            adapterFavorite.setListUser(new ArrayList<User>());
            Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
        }
    }


    private static class LoadUserAsync extends AsyncTask<Void, Void, ArrayList<User>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadUserCallback> weakCallback;

        LoadUserAsync(Context context, LoadUserCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
            assert dataCursor != null;
            return MappingHelper.myCursor(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<User> userLocals) {
            super.onPostExecute(userLocals);
            weakCallback.get().postExecute(userLocals);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadUserAsync(context, (LoadUserCallback) context).execute();
        }
    }
}

interface LoadUserCallback {
    void preExecute();

    void postExecute(ArrayList<User> userLocal);
}