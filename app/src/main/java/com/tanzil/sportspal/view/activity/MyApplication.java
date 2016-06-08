package com.tanzil.sportspal.view.activity;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by arun.sharma on 6/8/2016.
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}