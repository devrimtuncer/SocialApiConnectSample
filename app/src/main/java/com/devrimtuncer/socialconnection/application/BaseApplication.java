package com.devrimtuncer.socialconnection.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.devrimtuncer.socialconnection.BuildConfig;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;

/**
 *
 * Created by devrimtuncer on 12/03/16.
 */
public class BaseApplication extends Application {



    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // TWITTER_KEY and TWITTER_SECRET are defined at build.gradle file
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.TWITTER_KEY, BuildConfig.TWITTER_SECRET);

        // Crash report is not necessary for debug builds. So it is disabled for debug builds at build.gradle file.
        Kit[] kits;
        int kitIndex = 0;
        if (BuildConfig.CRASH_REPORT_ENABLED) {
            kits = new Kit[2];
            kits[kitIndex] = new Crashlytics();
            kitIndex++;
        } else {
            kits = new Kit[1];
        }
        kits[kitIndex] = new Twitter(authConfig);

        Fabric.with(this, kits);
    }

    public static Application getInstance() {
        return instance;
    }
}
