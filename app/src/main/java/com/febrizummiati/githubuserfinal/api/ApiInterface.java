package com.febrizummiati.githubuserfinal.api;

import com.febrizummiati.githubuserfinal.model.Result;
import com.febrizummiati.githubuserfinal.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/search/users")
    Call<Result> getList(@Query("q") String username);

    @GET("/users/{username}")
    Call<User> getDetail(@Path("username") String username);

    @GET("/users/{username}/followers")
    Call<List<User>> getListFollower(@Path("username") String username);

    @GET("/users/{username}/following")
    Call<List<User>> getListFollowing(@Path("username") String username);
}
