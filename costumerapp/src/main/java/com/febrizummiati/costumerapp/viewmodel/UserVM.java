package com.febrizummiati.costumerapp.viewmodel;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.febrizummiati.costumerapp.model.User;
import com.febrizummiati.costumerapp.util.MappingHelper;

import java.util.ArrayList;
import java.util.List;

import static com.febrizummiati.costumerapp.database.DatabaseContract.UserColumns.CONTENT_URI;

public class UserVM extends ViewModel {
    private MutableLiveData<List<User>> listUserDataLocal = new MutableLiveData<>();

    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MutableLiveData<List<User>> getUserLocal() {
        return listUserDataLocal;

    }

    public void setUserLocal(ContentResolver contentResolver) {
        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            ArrayList<User> userLocal = MappingHelper.myCursor(cursor);
            cursor.close();
            listUserDataLocal.postValue(userLocal);
        }
    }
}
