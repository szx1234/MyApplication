package com.szx.myapplication.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.szx.myapplication.activity.App;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by songzhixin on 2017/5/29.
 */

public class Util {
    public static void setUid(String uid) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit();
        editor.putString(Const.KEY_UID, uid);
        editor.commit();
    }

    public static String getUid() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String uid = sharedPreferences.getString(Const.KEY_UID, "");
        return uid;
    }

    public static void setUserName(String userName) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit();
        editor.putString(Const.KEY_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String userName = sharedPreferences.getString(Const.KEY_USER_NAME, "");
        return userName;
    }

    public static void setLastForumFid(String fid) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit();
        editor.putString(Const.KEY_LAST_FORUM_FID, fid);
        editor.commit();
    }

    public static String getLastForumFid() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        String fid = sharedPreferences.getString(Const.KEY_LAST_FORUM_FID, "110");
        return fid;
    }

    public static String analysisUid(String href) {
        String uid = null;
        Pattern pattern = Pattern.compile("uid=\\d+");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
            uid = matcher.group().replaceAll("uid=", "");
        }
        return uid;
    }

    public static String analysisTid(String href) {
        String tid = null;
        Pattern pattern = Pattern.compile("tid=\\d+");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
            tid = matcher.group().replaceAll("tid=", "");
        }
        return tid;
    }

    public static String analysisPageNum(String str) {
        String page = null;
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            page = matcher.group();
        }
        return page;
    }


}
