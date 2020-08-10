package com.febrizummiati.githubuserfinal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.febrizummiati.githubuserfinal.db.DatabaseContract.TABLE_FAVORITE_NAME;
import static com.febrizummiati.githubuserfinal.db.DatabaseContract.UserColumns.LOGIN;

public class FavoriteHelper {
    private final String DATABASE_TABLE = TABLE_FAVORITE_NAME;
    private static DatabaseHelper databaseHelper;
    private static FavoriteHelper INSTANCE;
    private static SQLiteDatabase database;

    private FavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
        if (database.isOpen()) {
            database.close();
        }
    }

    public Cursor queryAll() {
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " ASC");
    }

    //Ambil Data dengan ID
    public Cursor queryByLogin(String login) {
        return database.query(
                DATABASE_TABLE,
                null,
                LOGIN + " = ?",
                new String[]{login},
                null,
                null,
                null,
                null);
    }

    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int update(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[] {id});
    }

    public int deleteById(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
