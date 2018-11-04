package com.myapp.yldzmamak.myapplication.config;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by yldzmamak on 18.07.2018.
 */

public class CustomApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
