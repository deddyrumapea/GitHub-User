package com.romnan.githubuser.activity;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.romnan.githubuser.R;
import com.romnan.githubuser.adapter.UserRecyclerViewAdapter;
import com.romnan.githubuser.database.DatabaseContract;
import com.romnan.githubuser.helper.MappingHelper;
import com.romnan.githubuser.model.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

interface LoadFavUsersCallback {
    void preExecute();

    void postExecute(ArrayList<User> users);
}

public class FavUserActivity extends AppCompatActivity implements LoadFavUsersCallback {
    private static final String EXTRA_STATE = "extra_state";
    private UserRecyclerViewAdapter userRecyclerViewAdapter;
    private ProgressBar progressBar;
    private TextView tvNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.favorite_users);
        }

        progressBar = findViewById(R.id.loadingBar);
        tvNotFound = findViewById(R.id.tv_not_found);
        RecyclerView rvFavUsers = findViewById(R.id.rv_users);
        rvFavUsers.setLayoutManager(new LinearLayoutManager(this));
        rvFavUsers.setHasFixedSize(true);
        userRecyclerViewAdapter = new UserRecyclerViewAdapter();
        rvFavUsers.setAdapter(userRecyclerViewAdapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(DatabaseContract.FavUserColumns.CONTENT_URI,
                true, myObserver);

        if (savedInstanceState == null) {
            // Proses ambil data
            new LoadFavUsersAsync(this, this).execute();
        } else {
            ArrayList<User> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                userRecyclerViewAdapter.setData(list);
            }
        }

        //recyclerView item click callback method, open UserDetailActivity for specific user clicked
        userRecyclerViewAdapter.setOnItemClickCallback(new UserRecyclerViewAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(User data) {
                if (data != null) {
                    Intent intent = new Intent(FavUserActivity.this,
                            UserDetailActivity.class);
                    intent.putExtra(UserDetailActivity.EXTRA_USER, data);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, userRecyclerViewAdapter.getUsersList());
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
    public void postExecute(ArrayList<User> users) {
        progressBar.setVisibility(View.GONE);
        if (users.size() > 0) {
            userRecyclerViewAdapter.setData(users);
            tvNotFound.setVisibility(View.GONE);
        } else {
            userRecyclerViewAdapter.setData(new ArrayList<User>());
            tvNotFound.setText(getString(R.string.not_found));
            tvNotFound.setVisibility(View.VISIBLE);
        }
    }

    public static class DataObserver extends ContentObserver {

        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadFavUsersAsync(context, (LoadFavUsersCallback) context).execute();
        }
    }

    private static class LoadFavUsersAsync extends AsyncTask<Void, Void, ArrayList<User>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavUsersCallback> weakCallback;

        private LoadFavUsersAsync(Context context, LoadFavUsersCallback callback) {
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
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.FavUserColumns.CONTENT_URI,
                    null, null, null, null);
            assert dataCursor != null;
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);
            weakCallback.get().postExecute(users);
        }
    }
}















