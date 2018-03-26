package com.chaicopaillag.app.mageli;

import android.app.Application;

/**
 * Created by Programador on 26/03/2018.
 */
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MageliApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());

        AppEventsLogger.activateApp(this);
    }
}
