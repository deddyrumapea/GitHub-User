package com.romnan.githubuser.helper;

import android.database.Cursor;

import com.romnan.githubuser.model.User;

import java.util.ArrayList;

import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.AVATAR;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.COMPANY;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.FOLLOWERS;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.FOLLOWING;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.LOCATION;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.NAME;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.REPOSITORIES;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.USERNAME;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns._ID;

public class MappingHelper {
    public static ArrayList<User> mapCursorToArrayList(Cursor cursor) {
        ArrayList<User> usersList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String avatar = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(USERNAME));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(LOCATION));
            String company = cursor.getString(cursor.getColumnIndexOrThrow(COMPANY));
            int repositories = cursor.getInt(cursor.getColumnIndexOrThrow(REPOSITORIES));
            int followers = cursor.getInt(cursor.getColumnIndexOrThrow(FOLLOWERS));
            int following = cursor.getInt(cursor.getColumnIndexOrThrow(FOLLOWING));

            usersList.add(new User(id, avatar, name, username, location, company, repositories,
                    followers, following));
        }
        return usersList;
    }

    public static User mapCursorToObject(Cursor cursor) {
        cursor.moveToFirst();

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
        String avatar = cursor.getString(cursor.getColumnIndexOrThrow(AVATAR));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
        String username = cursor.getString(cursor.getColumnIndexOrThrow(USERNAME));
        String location = cursor.getString(cursor.getColumnIndexOrThrow(LOCATION));
        String company = cursor.getString(cursor.getColumnIndexOrThrow(COMPANY));
        int repositories = cursor.getInt(cursor.getColumnIndexOrThrow(REPOSITORIES));
        int followers = cursor.getInt(cursor.getColumnIndexOrThrow(FOLLOWERS));
        int following = cursor.getInt(cursor.getColumnIndexOrThrow(FOLLOWING));

        return new User(id, avatar, name, username, location, company, repositories, followers, following);
    }

}



















