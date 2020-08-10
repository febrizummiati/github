package com.febrizummiati.githubuserfinal.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.febrizummiati.githubuserfinal.api.ApiClient;
import com.febrizummiati.githubuserfinal.api.ApiInterface;
import com.febrizummiati.githubuserfinal.model.User;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class FollowerVM extends ViewModel {
    private final MutableLiveData<List<User>> listFollower = new MutableLiveData<>();
    private Context context;

    public MutableLiveData<List<User>> getListFollower() {
        return listFollower;
    }

    public void setListFollower(String username, Context context){
        ApiInterface Service;
        retrofit2.Call<List<User>> Call;
        try {
            Service = ApiClient.getApi().create(ApiInterface.class);
            Call = Service.getListFollower(username);
            Call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(retrofit2.Call<List<User>> call, Response<List<User>> response) {
                    Log.d("Response", "" + " " + response.body());
                    List<User> listUser;
                    listUser = response.body();
                    listFollower.postValue(listUser);
                }

                @Override
                public void onFailure(retrofit2.Call<List<User>> call, Throwable t) {
                    Log.d("Message", t.getMessage());
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
