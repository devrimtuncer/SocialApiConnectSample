package com.devrimtuncer.socialconnection.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.devrimtuncer.socialconnection.socialapiclients.SocialApiClient;
import com.devrimtuncer.socialconnection.socialapiclients.SocialApiClientFactory;
import com.devrimtuncer.socialconnection.socialapiclients.SocialApiClientType;

/**
 * Base activity for this app
 *
 * Created by devrimtuncer on 12/03/16.
 */
public abstract class CommonActivity extends AppCompatActivity {
    private static final String TAG_SOCIAL_CLIENT_TYPE = "TAG_SOCIAL_CLIENT_TYPE";

    private SocialApiClientType mSocialApiClientType;
    private SocialApiClient mSocialApiClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            int tmpSocialClientType = savedInstanceState.getInt(TAG_SOCIAL_CLIENT_TYPE, -1);
            mSocialApiClientType = tmpSocialClientType == -1 ? null : SocialApiClientType.values()[tmpSocialClientType];

            if(mSocialApiClientType != null) {
                createSocialApiClient(mSocialApiClientType);
            }
        }
    }

    public SocialApiClient getSocialApiClient() {
        return mSocialApiClient;
    }

    public SocialApiClient createSocialApiClient(SocialApiClientType socialApiClientType) {
        mSocialApiClientType = socialApiClientType;
        mSocialApiClient = SocialApiClientFactory.getClient(socialApiClientType);
        return mSocialApiClient;
    }

    // TODO: app must be tested when Developer Options --> Don't keep activities enabled
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(TAG_SOCIAL_CLIENT_TYPE, mSocialApiClientType == null ? -1 : mSocialApiClientType.ordinal());
    }
}
