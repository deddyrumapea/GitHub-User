package com.romnan.githubuser.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.romnan.githubuser.database.FavUserHelper;

import java.util.Objects;

import static com.romnan.githubuser.database.DatabaseContract.AUTHORITY;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.CONTENT_URI;
import static com.romnan.githubuser.database.DatabaseContract.FavUserColumns.TABLE_NAME;

public class FavUserProvider extends ContentProvider {
    private static final int USER = 1;
    private static final int USER_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content://com.romnan.githubuser/favuser
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER);

        // content://com.romnan.githubuser/favuser/id
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", USER_ID);
    }

    private FavUserHelper favUserHelper;

    @Override
    public boolean onCreate() {
        favUserHelper = FavUserHelper.getInstance(getContext());
        favUserHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case USER:
                cursor = favUserHelper.queryAll();
                break;
            case USER_ID:
                cursor = favUserHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;
        if (sUriMatcher.match(uri) == USER) {
            added = favUserHelper.insert(values);
        } else {
            added = 0;
        }
        Objects.requireNonNull(getContext()).getContentResolver()
                .notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int updated;
        if (sUriMatcher.match(uri) == USER_ID) {
            updated = favUserHelper.update(uri.getLastPathSegment(), values);
        } else {
            updated = 0;
        }
        Objects.requireNonNull(getContext())
                .getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        if (sUriMatcher.match(uri) == USER_ID) {
            deleted = favUserHelper.deleteById(uri.getLastPathSegment());
        } else {
            deleted = 0;
        }
        Objects.requireNonNull(getContext()).getContentResolver()
                .notifyChange(CONTENT_URI, null);
        return deleted;
    }
}
