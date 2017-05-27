package com.szx.myapplication.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by songzhixin on 2017/5/27.
 */

public class App extends Application {
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext(){
        return context;
    }
}
