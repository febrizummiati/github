package com.febrizummiati.costumerapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LocalUser implements Parcelable {
    private Integer id;
    private String username;


    public LocalUser(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    private LocalUser(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        username = in.readString();
    }

    public static final Creator<LocalUser> CREATOR = new Creator<LocalUser>() {
        @Override
        public LocalUser createFromParcel(Parcel in) {
            return new LocalUser(in);
        }

        @Override
        public LocalUser[] newArray(int size) {
            return new LocalUser[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(username);
    }
}
