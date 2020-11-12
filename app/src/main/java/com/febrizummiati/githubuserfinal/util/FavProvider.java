package com.febrizummiati.githubuserfinal.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.febrizummiati.githubuserfinal.database.FavHelper;

import java.util.Objects;

import static com.febrizummiati.githubuserfinal.database.DatabaseContract.AUTHORITY;
import static com.febrizummiati.githubuserfinal.database.DatabaseContract.TABLE_NAME;
import static com.febrizummiati.githubuserfinal.database.DatabaseContract.UserColumns.CONTENT_URI;

public class FavProvider extends ContentProvider {

    private static final int CODE = 1;
    private static final int CODE_FAV = 2;
    private FavHelper favoriteHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, CODE);

        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", CODE_FAV);
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavHelper.getInstance(getContext());
        favoriteHelper.open();
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CODE:
                cursor = favoriteHelper.queryAll();
                Log.d("Data Provider", "Data Size : " + cursor.getCount());
                break;

            case CODE_FAV:
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
        if (uriMatcher.match(uri) == CODE_FAV) {
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
        if (uriMatcher.match(uri) == CODE) {
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
        if (uriMatcher.match(uri) == CODE_FAV) {
            updated = favoriteHelper.update(uri.getLastPathSegment(), values);
        } else {
            updated = 0;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }
}
