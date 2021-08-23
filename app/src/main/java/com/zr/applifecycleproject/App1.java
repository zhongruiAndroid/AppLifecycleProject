package com.zr.applifecycleproject;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.github.applifecycle.AppLifecycle;
import com.github.applifecycle.ApplicationLifecycle;

import androidx.annotation.NonNull;

@ApplicationLifecycle
public class App1 implements AppLifecycle {
    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void onCreate(Context context) {
        Log.i("=====", getPriority() + "=====onCreate");
    }

    @Override
    public void attachBaseContext(Context base) {
        Log.i("=====", getPriority() + "=====attachBaseContext");

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        Log.i("=====", getPriority() + "=====onConfigurationChanged");

    }

    @Override
    public void onLowMemory() {
        Log.i("=====", getPriority() + "=====onLowMemory");

    }

    @Override
    public void onTerminate() {
        Log.i("=====", getPriority() + "=====onTerminate");

    }

    @Override
    public void onTrimMemory(int level) {
        Log.i("=====", getPriority() + "=====onTrimMemory");

    }
}
