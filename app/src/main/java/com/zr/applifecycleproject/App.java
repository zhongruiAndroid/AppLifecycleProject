package com.zr.applifecycleproject;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.github.applifecycle.AppLifecycleHelper;

import androidx.annotation.NonNull;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppLifecycleHelper.get().onCreate(this);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        AppLifecycleHelper.get().onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AppLifecycleHelper.get().onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppLifecycleHelper.get().onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        AppLifecycleHelper.get().onTrimMemory(level);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppLifecycleHelper.get().attachBaseContext(base);
    }
}
