package com.devrimtuncer.socialconnection.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A simple pojo for items
 *
 * Created by devrimtuncer on 12/03/16.
 */
public class Item implements Parcelable {
    private long id;
    private String createdAt;
    private String text;
    private String userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.createdAt);
        dest.writeString(this.text);
        dest.writeString(this.userId);
    }

    public Item() {
    }

    protected Item(Parcel in) {
        this.id = in.readLong();
        this.createdAt = in.readString();
        this.text = in.readString();
        this.userId = in.readString();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
