package com.devrimtuncer.socialconnection.activities.notloggedin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.devrimtuncer.socialconnection.R;
import com.devrimtuncer.socialconnection.socialapiclients.SocialApiClientType;

/**
 * Login activity for this app
 *
 * Created by devrimtuncer on 12/03/16.
 */
public class LoginActivity extends NotLoggedInActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;

        setSupportActionBar(toolbar);

        toolbar.setTitle(getTitle());

        Button twitterLoginButton = (Button) findViewById(R.id.twitter_login_button_);
        assert twitterLoginButton != null;

        twitterLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(SocialApiClientType.TWITTER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getSocialApiClient().onActivityResult(requestCode, resultCode, data);
    }
}
