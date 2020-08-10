package com.febrizummiati.githubuserfinal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("avatar_url")
    @Expose
    private String urlAvatar;
    @SerializedName("html_url")
    @Expose
    private String urlHtml;
    @SerializedName("followers")
    @Expose
    private Integer follower;
    @SerializedName("following")
    @Expose
    private Integer following;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("public_repos")
    @Expose
    private Integer repository;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getUrlHtml() {
        return urlHtml;
    }

    public void setUrlHtml(String urlHtml) {
        this.urlHtml = urlHtml;
    }

    public Integer getFollower() {
        return follower;
    }

    public void setFollower(Integer follower) {
        this.follower = follower;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getRepository() {
        return repository;
    }

    public void setRepository(Integer repository) {
        this.repository = repository;
    }

    public User(int id, String login, String name, String avatar_url){

    }
    public User(){

    }

    public User(String login, String urlAvatar, String urlHtml, Integer follower, Integer following, String location, String company, Integer repository) {
        this.login = login;
        this.urlAvatar = urlAvatar;
        this.urlHtml = urlHtml;
        this.follower = follower;
        this.following = following;
        this.location = location;
        this.company = company;
        this.repository = repository;
    }

    public User(Integer id, String login, String name, String urlAvatar){
        this.id = id;
        this.login = login;
        this.name = name;
        this.urlAvatar = urlAvatar;
    }

    protected User(Parcel in) {
        id = in.readInt();
        login = in.readString();
        urlAvatar = in.readString();
        urlHtml = in.readString();
        company = in.readString();
        location = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(login);
        dest.writeString(urlAvatar);
        dest.writeString(urlHtml);
        dest.writeString(company);
        dest.writeString(location);
    }

}
