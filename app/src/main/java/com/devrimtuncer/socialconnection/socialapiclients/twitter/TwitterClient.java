package com.devrimtuncer.socialconnection.socialapiclients.twitter;

import android.app.Activity;
import android.content.Intent;

import com.devrimtuncer.socialconnection.R;
import com.devrimtuncer.socialconnection.application.BaseApplication;
import com.devrimtuncer.socialconnection.pojo.Item;
import com.devrimtuncer.socialconnection.pojo.UserInfo;
import com.devrimtuncer.socialconnection.socialapiclients.SocialApiClient;
import com.devrimtuncer.socialconnection.socialapiclients.SocialApiClientType;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.services.common.CommonUtils;

/**
 *
 * A simple mClient for Twitter
 * <br>
 * Most of the code is copied from {@link TwitterLoginButton}'s source code
 * <br>
 * Created by devrimtuncer on 12/03/16.
 */
public class TwitterClient extends SocialApiClient {
    private static final StatusQueryType statusQueryType = StatusQueryType.HOME_TIMELINE;
    private static final int MAX_ITEM_COUNT_TO_BE_FETCHED = 20;

    // Twitters API's authClient
    volatile TwitterAuthClient authClient;

    // Twitters API's callback
    private Callback<TwitterSession> twitterSessionCallback;

    private static TwitterClient instance;

    public static synchronized TwitterClient getInstance( ) {
        if (instance == null) {
            instance = new TwitterClient();
        }
        return instance;
    }

    enum StatusQueryType {
        HOME_TIMELINE, USER_TIMELINE
    }

    private TwitterClient() {
        this.twitterSessionCallback = new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                TwitterAuthToken authToken = session.getAuthToken();

                UserInfo userInfo = new UserInfo();
                userInfo.setSocialApiClientType(SocialApiClientType.TWITTER);
                userInfo.setUserToken(authToken.token);
                userInfo.setUserSecret(authToken.secret);
                userInfo.setUserId(session.getUserId());
                userInfo.setUserName(session.getUserName());

                saveUserInfo(userInfo);

                if(mConnectCallback != null) {
                    mConnectCallback.success(userInfo);
                }
            }

            @Override
            public void failure(TwitterException e) {
                if(mConnectCallback != null) {
                    mConnectCallback.failure(new Exception("Twitter connect failed", e));
                }
            }
        };
    }

    @Override
    public void connect(Activity activity, ConnectCallback callback) {
        // initial check
        checkCallback(this.twitterSessionCallback);
        checkActivity(activity);
        checkConnectCallback(callback);

        this.mConnectCallback = callback;

        // API call
        getTwitterAuthClient().authorize(activity, twitterSessionCallback);
    }


    @Override
    public void fetchItems(UserInfo userInfo, FetchItemsCallback callback) {
        // initial check
        checkFetchItemsCallback(callback);

        this.mFetchItemsCallback = callback;

        // API call
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();

        if(StatusQueryType.HOME_TIMELINE.equals(statusQueryType)) {
            homeTimeline(statusesService);
        } else if(StatusQueryType.USER_TIMELINE.equals(statusQueryType)) {
            userTimeLine(userInfo, statusesService);
        }
    }

    /**
     * Returns a collection of the most recent Tweets and retweets posted by the authenticating user and the users they follow.
     *
     * @param statusesService from {@link StatusesService}
     */
    private void homeTimeline(StatusesService statusesService) {
        statusesService.homeTimeline(MAX_ITEM_COUNT_TO_BE_FETCHED, null, null, true, null, null, null, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                ArrayList<Item> itemList = mapResultToItemList(result);

                saveItemList(itemList);

                if (mFetchItemsCallback != null) {
                    mFetchItemsCallback.success(itemList);
                }
            }

            @Override
            public void failure(TwitterException e) {
                if (mFetchItemsCallback != null) {
                    mFetchItemsCallback.failure(new Exception("Twitter fetch items failed", e));
                }
            }
        });
    }

    /**
     * Returns a collection of the most recent Tweets posted by the user indicated by the screen_name or user_id parameters.
     *
     * @param userInfo has userId and userName parameters
     * @param statusesService {@link StatusesService}
     */
    private void userTimeLine(UserInfo userInfo, StatusesService statusesService) {
        statusesService.userTimeline(userInfo.getUserId(), userInfo.getUserName(), MAX_ITEM_COUNT_TO_BE_FETCHED, null, null, null, null, null, null, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                ArrayList<Item> itemList = mapResultToItemList(result);

                saveItemList(itemList);

                if (mFetchItemsCallback != null) {
                    mFetchItemsCallback.success(itemList);
                }
            }

            @Override
            public void failure(TwitterException e) {
                if (mFetchItemsCallback != null) {
                    mFetchItemsCallback.failure(new Exception("Twitter fetch items failed", e));
                }
            }
        });
    }

    private ArrayList<Item> mapResultToItemList(Result<List<Tweet>> result) {
        ArrayList<Item> itemList = null;
        if(result != null
                && result.data != null
                && result.data.size() > 0) {

            itemList = new ArrayList<>(result.data.size());

            for (Tweet tweet : result.data) {
                Item item = new Item();
                item.setCreatedAt(tweet.createdAt);
                item.setText(tweet.text);
                item.setId(tweet.id);

                if(tweet.user != null) {
                    String userId = BaseApplication.getInstance().getResources().getString(R.string.user_id_text, tweet.user.idStr);
                    item.setUserId(userId);
                }
                itemList.add(item);
            }
        }

        return itemList;
    }

    private void checkConnectCallback(ConnectCallback connectCallback) {
        if(connectCallback == null) {
            throw new RuntimeException("ConnectCallback must not be null!");
        }
    }

    private void checkFetchItemsCallback(FetchItemsCallback fetchItemsCallback) {
        if(fetchItemsCallback == null) {
            throw new RuntimeException("FetchItemsCallback must not be null!");
        }
    }

    /**
     * Copied from {#TwitterLoginButton}
     *
     * @param callback is checked
     */
    private void checkCallback(Callback callback) {
        if(callback == null) {
            CommonUtils.logOrThrowIllegalStateException("Twitter", "Callback must not be null, did you call setCallback?");
        }
    }

    /**
     * Copied from {#TwitterLoginButton}
     *
     * @param activity is checked
     */
    private void checkActivity(Activity activity) {
        if(activity == null || activity.isFinishing()) {
            CommonUtils.logOrThrowIllegalStateException("Twitter", "TwitterLoginButton requires an activity. Override getActivity to provide the activity for this button.");
        }
    }

    /**
     * Copied from {#TwitterLoginButton}
     *
     * @return TwitterAuthClient
     */
    TwitterAuthClient getTwitterAuthClient() {
        if(this.authClient == null) {
            synchronized(TwitterLoginButton.class) {
                if(this.authClient == null) {
                    this.authClient = new TwitterAuthClient();
                }
            }
        }
        return this.authClient;
    }

    /**
     * Copied from {#TwitterLoginButton}
     *
     * @param requestCode for {#TwitterAuthClient}
     * @param resultCode for {#TwitterAuthClient}
     * @param data for {#TwitterAuthClient}
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == this.getTwitterAuthClient().getRequestCode()) {
            this.getTwitterAuthClient().onActivityResult(requestCode, resultCode, data);
        }
    }
}
