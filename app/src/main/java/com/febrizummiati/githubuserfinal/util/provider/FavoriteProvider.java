package com.febrizummiati.githubuserfinal.util.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.febrizummiati.githubuserfinal.db.FavoriteHelper;

import java.util.Objects;

import static com.febrizummiati.githubuserfinal.db.DatabaseContract.AUTHORITY;
import static com.febrizummiati.githubuserfinal.db.DatabaseContract.TABLE_FAVORITE_NAME;
import static com.febrizummiati.githubuserfinal.db.DatabaseContract.UserColumns.CONTENT_URI;

public class FavoriteProvider extends ContentProvider {

    private static final int FAV = 1;
    private static final int FAV_ID = 2;
    private FavoriteHelper favoriteHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE_NAME, FAV);

        sUriMatcher.addURI(AUTHORITY,
                TABLE_FAVORITE_NAME + "/#",
                FAV_ID);
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        favoriteHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAV:
                cursor = favoriteHelper.queryAll();
                Log.d("Data Provider", "Data Size : " + cursor.getCount());
                break;

            case FAV_ID:
                cursor = favoriteHelper.queryByLogin(uri.getLastPathSegment());
                break;

            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        if (sUriMatcher.match(uri) == FAV_ID) {
            deleted = favoriteHelper.deleteById(uri.getLastPathSegment());
        } else {
            deleted = 0;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long added;
        if (sUriMatcher.match(uri) == FAV) {
            added = favoriteHelper.insert(values);
        } else {
            added = 0;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updated;
        if (sUriMatcher.match(uri) == FAV_ID) {
            updated = favoriteHelper.update(uri.getLastPathSegment(), values);
        } else {
            updated = 0;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }
}
