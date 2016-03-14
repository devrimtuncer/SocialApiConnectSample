package com.devrimtuncer.socialconnection.activities.loggedin;

import android.content.Intent;

import com.devrimtuncer.socialconnection.activities.CommonActivity;
import com.devrimtuncer.socialconnection.activities.notloggedin.LoginActivity;
import com.devrimtuncer.socialconnection.socialapiclients.SocialApiClient;

/**
 *
 * Base class for logged in activities
 *
 * Created by devrimtuncer on 13/03/16.
 */
public class LoggedInActivity extends CommonActivity {
    protected void logout() {
        SocialApiClient.clearUserInfo();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
