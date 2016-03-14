package com.devrimtuncer.socialconnection.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.devrimtuncer.socialconnection.socialapiclients.SocialApiClientType;

/**
 *
 * A simple pojo for user info
 *
 * Created by devrimtuncer on 12/03/16.
 */
public class UserInfo implements Parcelable {
    private SocialApiClientType socialApiClientType;
    private String userToken;
    private String userSecret;
    private long userId;
    private String userName;

    public SocialApiClientType getSocialApiClientType() {
        return socialApiClientType;
    }

    public void setSocialApiClientType(SocialApiClientType socialApiClientType) {
        this.socialApiClientType = socialApiClientType;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserSecret() {
        return userSecret;
    }

    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.socialApiClientType == null ? -1 : this.socialApiClientType.ordinal());
        dest.writeString(this.userToken);
        dest.writeString(this.userSecret);
        dest.writeLong(this.userId);
        dest.writeString(this.userName);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        int tmpSocialClientType = in.readInt();
        this.socialApiClientType = tmpSocialClientType == -1 ? null : SocialApiClientType.values()[tmpSocialClientType];
        this.userToken = in.readString();
        this.userSecret = in.readString();
        this.userId = in.readLong();
        this.userName = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
