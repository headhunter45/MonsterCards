package com.majinnaibu.monstercards.init;

import android.app.Application;

import com.majinnaibu.monstercards.BuildConfig;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class AppCenterInitializer {

    public static void init(Application app) {
        if (BuildConfig.APPCENTER_SECRET != null && !"".equals(BuildConfig.APPCENTER_SECRET)) {
            AppCenter.start(
                    app,
                    BuildConfig.APPCENTER_SECRET,
                    Analytics.class,
                    Crashes.class
            );
        }
    }
}