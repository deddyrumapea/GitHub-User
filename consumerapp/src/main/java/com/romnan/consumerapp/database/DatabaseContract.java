package com.romnan.consumerapp.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.romnan.githubuser";
    public static final String SCHEME = "content";

    private DatabaseContract() {
    }

    public static final class FavUserColumns implements BaseColumns {
        public static final String TABLE_NAME = "FavUser";
        public static final String AVATAR = "avatar";
        public static final String NAME = "name";
        public static final String USERNAME = "username";
        public static final String LOCATION = "location";
        public static final String COMPANY = "company";
        public static final String REPOSITORIES = "repositories";
        public static final String FOLLOWERS = "followers";
        public static final String FOLLOWING = "following";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(FavUserColumns.TABLE_NAME)
                .build();
    }
}
