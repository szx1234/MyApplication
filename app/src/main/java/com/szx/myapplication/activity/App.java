package com.szx.myapplication.activity;

import android.app.Application;
import android.content.Context;

import com.szx.myapplication.util.Util;

/**
 * Created by songzhixin on 2017/5/27.
 */

public class App extends Application {
    static Context mContext;
    private static String mCurrentFid;
    private static String mCurrentTid;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mCurrentFid = Util.getLastForumFid();
    }

    public static String getmCurrentFid() {
        return mCurrentFid;
    }

    public static void setmCurrentFid(String mCurrentFid) {
        App.mCurrentFid = mCurrentFid;
        Util.setLastForumFid(mCurrentFid);
    }

    public static String getmCurrentTid() {
        return mCurrentTid;
    }

    public static void setmCurrentTid(String mCurrentTid) {
        App.mCurrentTid = mCurrentTid;
    }


    public static Context getAppContext() {
        return mContext;
    }
}