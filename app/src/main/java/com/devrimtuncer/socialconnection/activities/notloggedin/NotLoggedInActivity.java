package com.devrimtuncer.socialconnection.activities.notloggedin;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.devrimtuncer.socialconnection.R;
import com.devrimtuncer.socialconnection.activities.CommonActivity;
import com.devrimtuncer.socialconnection.activities.loggedin.ItemListActivity;
import com.devrimtuncer.socialconnection.pojo.Item;
import com.devrimtuncer.socialconnection.pojo.UserInfo;
import com.devrimtuncer.socialconnection.socialapiclients.SocialApiClient;
import com.devrimtuncer.socialconnection.socialapiclients.SocialApiClientType;

import java.util.ArrayList;

/**
 * Base class for not logged in activities
 *
 * Created by devrimtuncer on 12/03/16.
 */
public abstract class NotLoggedInActivity extends CommonActivity {
    private static final String TAG = NotLoggedInActivity.class.getSimpleName();

    public void showLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(final SocialApiClientType socialApiClientType) {
        checkClientType(socialApiClientType);
        SocialApiClient socialApiClient = createSocialApiClient(socialApiClientType);

        if(socialApiClient != null) {
            socialApiClient.connect(this, new SocialApiClient.ConnectCallback() {
                @Override
                public void success(UserInfo userInfo) {
                    onLoggedIn(userInfo);
                }

                @Override
                public void failure(Exception exception) {
                    Log.e(TAG, "Login with " + socialApiClientType + " failure", exception);

                    View snackBarContainer = findViewById(R.id.coordinator_layout);
                    assert snackBarContainer != null;
                    Snackbar.make(snackBarContainer, getResources().getString(R.string.error_message_connect), Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    protected void onLoggedIn(final UserInfo userInfo) {
        ArrayList<Item> previouslySessionsSavedItemList = SocialApiClient.getSavedItemList();

        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtra(ItemListActivity.ARG_USER_INFO, userInfo);
        intent.putParcelableArrayListExtra(ItemListActivity.ARG_ITEM_LIST, previouslySessionsSavedItemList);
        startActivity(intent);
        finish();
    }

    private void checkClientType(SocialApiClientType socialApiClientType) {
        if(socialApiClientType == null) {
            throw new RuntimeException("ClientType must not be null!");
        }
    }
}
