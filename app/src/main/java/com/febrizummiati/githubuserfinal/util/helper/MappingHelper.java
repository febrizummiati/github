package com.febrizummiati.githubuserfinal.util.helper;

import android.database.Cursor;

import com.febrizummiati.githubuserfinal.db.DatabaseContract;
import com.febrizummiati.githubuserfinal.model.User;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<User> mapCursorToArrayList(Cursor notesCursor) {
        ArrayList<User> userList = new ArrayList<>();
        while (notesCursor != null && notesCursor.moveToNext()) {
            int id = notesCursor.getInt(notesCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns._ID));
            String login = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.LOGIN));
            String name = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME));
            String url_avatar = notesCursor.getString(notesCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL));

            userList.add(new User(id, login, name, url_avatar));
        }

        return userList;
    }
}
