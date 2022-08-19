package com.github.applifecycle;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AppLifecycleHelper {
    /**********************************************************/
    private static AppLifecycleHelper singleObj;

    private AppLifecycleHelper() {
    }

    public static AppLifecycleHelper get() {
        if (singleObj == null) {
            synchronized (AppLifecycleHelper.class) {
                if (singleObj == null) {
                    singleObj = new AppLifecycleHelper();
                }
            }
        }
        return singleObj;
    }

    private List<AppLifecycle> appLifecycleList = new ArrayList<>();

    /**********************************************************/
    public void addAppLifecycle(AppLifecycle appLifecycle) {
        if (appLifecycle == null) {
            return;
        }
        for (AppLifecycle item : appLifecycleList) {
            if (item.getClass().getName().equals(appLifecycle.getClass().getName())) {
                return;
            }
        }
        appLifecycleList.add(appLifecycle);
    }

    public void addAppLifecycle(String className) {
        if (TextUtils.isEmpty(className)) {
            return;
        }
        try {
            Object object = Class.forName(className).getConstructor().newInstance();
            if (object instanceof AppLifecycle) {
                addAppLifecycle((AppLifecycle) object);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
        }catch (SecurityException e) {
            e.printStackTrace();
        }catch (InstantiationException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
        }catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmpty() {
        if (appLifecycleList == null || appLifecycleList.isEmpty()) {
            return true;
        }
        return false;
    }

    public void onCreate(Context context) {
        if (isEmpty()) {
            return;
        }
        Collections.sort(appLifecycleList, new Comparator<AppLifecycle>() {
            @Override
            public int compare(AppLifecycle o1, AppLifecycle o2) {
                return o2.getPriority() - o1.getPriority();
            }
        });
        for (AppLifecycle appLifecycle : appLifecycleList) {
            if (appLifecycle == null) {
                continue;
            }
            appLifecycle.onCreate(context);
        }
    }

    public void attachBaseContext(Context base) {
        if (isEmpty()) {
            return;
        }
        for (AppLifecycle appLifecycle : appLifecycleList) {
            if (appLifecycle == null) {
                continue;
            }
            appLifecycle.attachBaseContext(base);
        }
    }

    public void onConfigurationChanged( Configuration newConfig) {
        if (isEmpty()) {
            return;
        }
        for (AppLifecycle appLifecycle : appLifecycleList) {
            if (appLifecycle == null) {
                continue;
            }
            appLifecycle.onConfigurationChanged(newConfig);
        }
    }

    public void onLowMemory() {
        if (isEmpty()) {
            return;
        }
        for (AppLifecycle appLifecycle : appLifecycleList) {
            if (appLifecycle == null) {
                continue;
            }
            appLifecycle.onLowMemory();
        }
    }

    public void onTerminate() {
        if (isEmpty()) {
            return;
        }
        for (AppLifecycle appLifecycle : appLifecycleList) {
            if (appLifecycle == null) {
                continue;
            }
            appLifecycle.onTerminate();
        }
    }

    public void onTrimMemory(int level) {
        if (isEmpty()) {
            return;
        }
        for (AppLifecycle appLifecycle : appLifecycleList) {
            if (appLifecycle == null) {
                continue;
            }
            appLifecycle.onTrimMemory(level);
        }
    }
}
