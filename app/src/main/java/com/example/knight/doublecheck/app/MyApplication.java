package com.example.knight.doublecheck.app;

import android.app.Application;
import android.content.Context;

import com.knight.modularization.library.AppSpec;

@AppSpec
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
