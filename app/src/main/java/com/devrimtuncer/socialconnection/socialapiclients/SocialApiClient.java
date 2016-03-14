package com.devrimtuncer.socialconnection.socialapiclients;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.devrimtuncer.socialconnection.application.BaseApplication;
import com.devrimtuncer.socialconnection.pojo.Item;
import com.devrimtuncer.socialconnection.pojo.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *
 * Simple wrapper for social api clients.
 *
 * Created by devrimtuncer on 12/03/16.
 */
public abstract class SocialApiClient {
    private static final String SHARED_PREFERENCES_FILE = "USER_INFO";

    public static final String TAG_SOCIAL_API_CLIENT_TYPE = "TAG_SOCIAL_API_CLIENT_TYPE";
    public static final String TAG_USER_INFO_ID = "TAG_USER_INFO_ID";
    public static final String TAG_USER_INFO_NAME = "TAG_USER_INFO_NAME";
    public static final String TAG_USER_INFO_TOKEN = "TAG_USER_INFO_TOKEN";
    public static final String TAG_USER_INFO_SECRET = "TAG_USER_INFO_SECRET";
    public static final String TAG_ITEM_LIST = "TAG_ITEM_LIST";

    protected ConnectCallback mConnectCallback;
    protected FetchItemsCallback mFetchItemsCallback;

    public abstract void connect(Activity activity, ConnectCallback callback);
    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);
    public abstract void fetchItems(UserInfo userInfo, FetchItemsCallback callback);

    public interface ConnectCallback {
        void success(UserInfo userInfo);
        void failure(Exception exception);
    }

    public interface FetchItemsCallback {
        void success(ArrayList<Item> itemList);
        void failure(Exception exception);
    }

    /**
     * UserInfo must be saved to call apis without authentication
     *
     * @param userInfo to be saved
     */
    protected void saveUserInfo(UserInfo userInfo) {
        // TODO: values must be encrypted for security
        Context context = BaseApplication.getInstance().getApplicationContext();

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int clientTypeAsInt = userInfo.getSocialApiClientType() == null ? -1 : userInfo.getSocialApiClientType().ordinal();
        editor.putInt(TAG_SOCIAL_API_CLIENT_TYPE, clientTypeAsInt);
        editor.putLong(TAG_USER_INFO_ID, userInfo.getUserId());
        editor.putString(TAG_USER_INFO_NAME, userInfo.getUserName());
        editor.putString(TAG_USER_INFO_TOKEN, userInfo.getUserToken());
        editor.putString(TAG_USER_INFO_SECRET, userInfo.getUserSecret());

        editor.apply();
    }

    /**
     * It is better to save fetched item list,
     * so we can show previously saved item list to user while fetching new list.
     *
     * @param itemList to be saved
     */
    protected void saveItemList(ArrayList<Item> itemList) {
        // TODO: values must be encrypted for security
        String itemListAsString = null;
        if(itemList != null && itemList.size() > 0) {
            Gson gson = new Gson();
            Type listOfItemObject = new TypeToken<ArrayList<Item>>() {}.getType();
            itemListAsString = gson.toJson(itemList, listOfItemObject);
        }

        Context context = BaseApplication.getInstance().getApplicationContext();

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(TAG_ITEM_LIST, itemListAsString);
        editor.apply();
    }

    /**
     * Returns previously logged in user info.
     * <br>
     *
     * This method can return null;
     * <ol>
     *     <li>
     *         User never logged in
     *     </li>
     *     <li>
     *         User logged out
     *     </li>
     *      <li>
     *         App's data cleared
     *     </li>
     * </ol>
     *
     * @return userInfo
     */
    public static UserInfo getSavedUserInfo() {
        Context context = BaseApplication.getInstance().getApplicationContext();

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        long savedUserId = sharedPref.getLong(TAG_USER_INFO_ID, 0L);
        if(savedUserId == 0L) {
            // User is not logged in
            return null;
        } else {
            UserInfo userInfo = new UserInfo();
            int tmpSocialClientType = sharedPref.getInt(TAG_SOCIAL_API_CLIENT_TYPE, -1);
            if(tmpSocialClientType != -1) {
                userInfo.setSocialApiClientType(SocialApiClientType.values()[tmpSocialClientType]);
            }
            userInfo.setUserId(savedUserId);
            userInfo.setUserName(sharedPref.getString(TAG_USER_INFO_NAME, null));
            userInfo.setUserToken(sharedPref.getString(TAG_USER_INFO_TOKEN, null));
            userInfo.setUserSecret(sharedPref.getString(TAG_USER_INFO_SECRET, null));

            return userInfo;
        }
    }

    /**
     * Same flow with {#getSavedUserInfo}
     *
     * @return savedItemList
     */
    public static ArrayList<Item> getSavedItemList() {
        Context context = BaseApplication.getInstance().getApplicationContext();

        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        String itemListAsString = sharedPref.getString(TAG_ITEM_LIST, null);

        ArrayList<Item> itemList = null;
        if(!TextUtils.isEmpty(itemListAsString)) {
            Gson gson = new Gson();
            Type listOfItemObject = new TypeToken<ArrayList<Item>>(){}.getType();
            itemList = gson.fromJson(itemListAsString, listOfItemObject);
        }

        return itemList;
    }


    /**
     * Clear user info with its item list.
     *
     * This method must be called after logout.
     */
    public static void clearUserInfo() {
        Context context = BaseApplication.getInstance().getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong(TAG_USER_INFO_ID, 0L);
        editor.putInt(TAG_SOCIAL_API_CLIENT_TYPE, -1);
        editor.putString(TAG_USER_INFO_NAME, null);
        editor.putString(TAG_USER_INFO_TOKEN, null);
        editor.putString(TAG_USER_INFO_SECRET, null);

        // Item list must be cleared
        editor.putString(TAG_ITEM_LIST, null);

        editor.apply();
    }
}
