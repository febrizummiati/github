package com.febrizummiati.costumerapp.util;

import android.database.Cursor;

import com.febrizummiati.costumerapp.database.DatabaseContract;
import com.febrizummiati.costumerapp.model.User;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<User> myCursor(Cursor cursor) {
        ArrayList<User> users = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns._ID));
            String login = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.LOGIN));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME));
            String avatar_url = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL));

            users.add(new User(id, login, name, avatar_url));
        }

        return users;
    }
}
