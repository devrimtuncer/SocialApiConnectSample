package com.devrimtuncer.socialconnection.socialapiclients;

import com.devrimtuncer.socialconnection.socialapiclients.twitter.TwitterClient;

/**
 * A simple factory to create a {@link SocialApiClient}
 *
 * Created by devrimtuncer on 12/03/16.
 */
public abstract class SocialApiClientFactory {

    public static SocialApiClient getClient(SocialApiClientType socialApiClientType) {
        if(SocialApiClientType.TWITTER.equals(socialApiClientType)) {
            return TwitterClient.getInstance();
        } else {
            throw new RuntimeException("ClientType: " + socialApiClientType + " is not supported!");
        }
    }
}
