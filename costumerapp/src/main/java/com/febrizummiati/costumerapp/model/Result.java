package com.febrizummiati.costumerapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("items")
    @Expose
    private List<User> result;

    public List<User> getResult() {
        return result;
    }

    public Result(List<User> result) {
        this.result = result;
    }
}
