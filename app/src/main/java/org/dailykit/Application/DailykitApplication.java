package org.dailykit.application;

import android.app.Application;

import com.bugfender.sdk.Bugfender;

import org.dailykit.BuildConfig;
import org.dailykit.logs.DevelopmentTree;
import org.dailykit.logs.ReleaseTree;

import timber.log.Timber;

public class DailykitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bugfender.init(this, "cMKmb6cWcp1FhXc2N3FPDNZMmzUAiEOv", BuildConfig.DEBUG);
        Bugfender.enableCrashReporting();
        Bugfender.enableUIEventLogging(this);
        Bugfender.enableLogcatLogging(); // optional, if you want logs automatically collected from logcat
        if(BuildConfig.DEBUG){
            Timber.plant(new DevelopmentTree());
        }
        else{
            Timber.plant(new ReleaseTree());
        }
    }
}
