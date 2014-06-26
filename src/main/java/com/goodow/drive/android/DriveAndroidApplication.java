package com.goodow.drive.android;

import android.app.Application;
import roboguice.RoboGuice;

/**
 * Created by dpw on 5/28/14.
 */
public class DriveAndroidApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(this), new DriveAndroidModule());
    }
}
