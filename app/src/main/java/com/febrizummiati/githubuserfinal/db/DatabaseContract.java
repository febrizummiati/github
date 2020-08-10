package com.febrizummiati.githubuserfinal.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.febrizummiati.githubuserfinal";
    private static final String SCHEME = "content";
    public static final String TABLE_FAVORITE_NAME = "favorite";

    public DatabaseContract() {
    }

    public static final class UserColumns implements BaseColumns {

        public static String LOGIN = "login";
        public static String NAME = "name";
        public static String AVATAR_URL = "avatar_url";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITE_NAME)
                .build();
    }
}
