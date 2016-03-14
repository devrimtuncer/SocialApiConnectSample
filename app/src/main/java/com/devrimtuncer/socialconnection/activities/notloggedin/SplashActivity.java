package com.devrimtuncer.socialconnection.activities.notloggedin;

import android.os.Bundle;

import com.devrimtuncer.socialconnection.R;
import com.devrimtuncer.socialconnection.pojo.UserInfo;
import com.devrimtuncer.socialconnection.socialapiclients.SocialApiClient;

/**
 *
 * Initial activity for this app
 *
 * Created by devrimtuncer on 12/03/16.
 */
public class SplashActivity extends NotLoggedInActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        // Get previously logged in user info.
        // If; User info exists do not show login view. Get item list and redirect to list activity.
        // Else; Show login activity.
        UserInfo userInfo = SocialApiClient.getSavedUserInfo();
        if(userInfo != null) {
            onLoggedIn(userInfo);
        } else {
            showLogin();
        }
    }
}