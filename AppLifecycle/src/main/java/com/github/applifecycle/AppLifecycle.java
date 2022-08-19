package com.github.applifecycle;

import android.content.Context;
import android.content.res.Configuration;


public interface AppLifecycle {
    int PRIORITY_0 = 0;
    int PRIORITY_1 = 1;
    int PRIORITY_2 = 2;
    int PRIORITY_3 = 3;
    int PRIORITY_4 = 4;
    int PRIORITY_5 = 5;
    int PRIORITY_6 = 6;
    int PRIORITY_7 = 7;
    int PRIORITY_8 = 8;
    int PRIORITY_9 = 9;
    int PRIORITY_10 = 10;

    int getPriority();

    void onCreate(Context context);

    void attachBaseContext(Context base);

    void onConfigurationChanged(Configuration newConfig);

    void onLowMemory();

    void onTerminate();

    void onTrimMemory(int level);

}
