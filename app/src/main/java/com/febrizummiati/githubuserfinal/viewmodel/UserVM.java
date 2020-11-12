package com.febrizummiati.githubuserfinal.viewmodel;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.febrizummiati.githubuserfinal.R;
import com.febrizummiati.githubuserfinal.api.Api;
import com.febrizummiati.githubuserfinal.api.ApiInterface;
import com.febrizummiati.githubuserfinal.model.Result;
import com.febrizummiati.githubuserfinal.model.User;
import com.febrizummiati.githubuserfinal.util.MappingHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

import static com.febrizummiati.githubuserfinal.database.DatabaseContract.UserColumns.CONTENT_URI;

public class UserVM extends ViewModel {
    private MutableLiveData<List<User>> listUsers = new MutableLiveData<>();
    private MutableLiveData<List<User>> listUserDataLocal = new MutableLiveData<>();
    private MutableLiveData<User> userDataApi = new MutableLiveData<>();

    private User user;
    private Context context;

    public MutableLiveData<List<User>> getListUsers() {
        return listUsers;
    }

    public void setListUsers(String username, final Context context) {
        ApiInterface Service;
        retrofit2.Call<Result> Call;
        try {
            Service = Api.getApi().create(ApiInterface.class);
            Call = Service.getList(username);
            Call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(retrofit2.Call<Result> call, Response<Result> response) {
                    Log.d("Response", "" + " " + response.body());
                    List<User> listUser;
                    assert response.body() != null;
                    listUser = response.body().getResult();
                    listUsers.postValue(listUser);
                    if (listUser.isEmpty()) {
                        Toast.makeText(context, R.string.user_not_found, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Result> call, Throwable t) {
                    Log.d("Message", t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public MutableLiveData<User> getUserDataApi() {
        return userDataApi;
    }

    public void setUserDataApi(String login) {
        ApiInterface Service;
        retrofit2.Call<User> Call;
        try {
            Service = Api.getApi().create(ApiInterface.class);
            Call = Service.getDetail(login);
            Call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull retrofit2.Call<User> call, @NonNull Response<User> response) {
                    user = response.body();
                    userDataApi.postValue(user);
                }

                @Override
                public void onFailure(@NonNull retrofit2.Call<User> call, @NonNull Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
