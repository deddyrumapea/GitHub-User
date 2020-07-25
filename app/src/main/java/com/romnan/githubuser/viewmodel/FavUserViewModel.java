package com.romnan.githubuser.viewmodel;

import android.database.Cursor;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.romnan.githubuser.database.FavUserHelper;
import com.romnan.githubuser.helper.MappingHelper;
import com.romnan.githubuser.model.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

interface LoadFavUserCallback {
    void preExecute();

    void postExecute(ArrayList<User> favUsers);
}

public class FavUserViewModel extends ViewModel implements LoadFavUserCallback {
    MutableLiveData<ArrayList<User>> favUsersList = new MutableLiveData<>();

    public void loadFavUser(FavUserHelper favUserHelper) {
        new LoadFavUserAsync(favUserHelper, this).execute();
    }

    @Override
    public void preExecute() {
    }

    @Override
    public void postExecute(ArrayList<User> favUsers) {
        if (favUsers.size() > 0) {
            favUsersList.postValue(favUsers);
        } else {
            favUsersList.postValue(new ArrayList<User>());
        }
    }

    public LiveData<ArrayList<User>> getFavUsers() {
        return favUsersList;
    }

    private static class LoadFavUserAsync extends AsyncTask<Void, Void, ArrayList<User>> {

        private final WeakReference<FavUserHelper> weakFavUserHelper;
        private final WeakReference<LoadFavUserCallback> weakCallback;

        private LoadFavUserAsync(FavUserHelper favUserHelper, LoadFavUserCallback callback) {
            weakFavUserHelper = new WeakReference<>(favUserHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            Cursor dataCursor = weakFavUserHelper.get().queryAll();
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);
            weakCallback.get().postExecute(users);
        }
    }
}
