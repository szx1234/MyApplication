package com.szx.myapplication.activity;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.szx.myapplication.fragment.MyPreferenceFragment;
import com.szx.myapplication.util.Const;
import com.szx.myapplication.util.Util;

import org.litepal.LitePalApplication;

/**
 * Created by songzhixin on 2017/5/27.
 */

public class App extends LitePalApplication {
    static Context mContext;
    /**
     * 设置中的以一些常量
     */
    public static boolean openNight = false;
    public static boolean openNotation = false;
    public static int refreshTime = 5 * Const.SECOND;
    public static boolean openTail = false;
    public static String tail = "";

    /**
     * 当前fid和tid
     */
    private static String mCurrentFid;
    private static String mCurrentTid;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mCurrentFid = Util.getLastForumFid();
        initEnviroment();
    }


    private void initEnviroment() {
        try { //第一次启动程序没有此sharedPreference
            SharedPreferences settingPreference = getSharedPreferences("com.szx.myapplication_preferences", 0);
            openNight = settingPreference.getBoolean("setting_mode", false);
            openNotation = settingPreference.getBoolean("setting_notation", false);
            String time = settingPreference.getString("setting_refresh_time", "5000");
            if (time.length() < 3)
                time = time + "000";
            refreshTime = Integer.valueOf(time);
            openTail = settingPreference.getBoolean("setting_tail_state", false);
            tail = settingPreference.getString("setting_tail_content", "---来自睿思手机客户端");

            Log.w("初始化", "openNight=" + openNight);
            Log.w("初始化", "openNotation=" + openNotation);
            Log.w("初始化", "openTail=" + openTail);
            Log.w("初始化", "refreshTime=" + refreshTime);
            Log.w("初始化", "tail=" + tail);
        } catch (NullPointerException e) {
            Log.w("NullPoint", " hahahhahahahahahahhahahhahaha");
        }
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