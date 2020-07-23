package com.romnan.githubuser.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbuserapp";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_FAV_USER = String.format("CREATE TABLE %s"
                    + " (%s TEXT PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s INTEGER NOT NULL," +
                    " %s INTEGER NOT NULL," +
                    " %s INTEGER NOT NULL)",
            TABLE_NAME,
            FavUserColumns._ID,
            FavUserColumns.AVATAR,
            FavUserColumns.NAME,
            FavUserColumns.USERNAME,
            FavUserColumns.LOCATION,
            FavUserColumns.COMPANY,
            FavUserColumns.REPOSITORIES,
            FavUserColumns.FOLLOWERS,
            FavUserColumns.FOLLOWING
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAV_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}











