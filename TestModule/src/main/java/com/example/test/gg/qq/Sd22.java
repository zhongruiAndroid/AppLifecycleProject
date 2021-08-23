package com.example.test.gg.qq;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.github.applifecycle.AppLifecycle;
import com.github.applifecycle.ApplicationLifecycle;

import androidx.annotation.NonNull;

@ApplicationLifecycle
public class Sd22 implements AppLifecycle {
    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void onCreate(Context context) {
        Log.i("=====",getPriority()+"===onCreate:S"+getClass().getName());
    }

    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void onTrimMemory(int level) {

    }
}
