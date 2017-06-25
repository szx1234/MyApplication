package com.szx.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.szx.myapplication.activity.App;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by songzhixin on 2017/5/29.
 */

public class Util {

    static Context context = App.getAppContext();

    public static void setUid(String uid) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.KEY_UID, uid);
        editor.commit();
    }

    public static String getUid() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String uid = sharedPreferences.getString(Const.KEY_UID, "");
        return uid;
    }

    public static void setUserName(String userName) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.KEY_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userName = sharedPreferences.getString(Const.KEY_USER_NAME, "");
        return userName;
    }

    public static void setLastForumFid(String fid) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.KEY_LAST_FORUM_FID, fid);
        editor.commit();
    }

    public static String getLastForumFid() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String fid = sharedPreferences.getString(Const.KEY_LAST_FORUM_FID, "110");
        return fid;
    }

    public static void setLastForumName(String name) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Const.KEY_LAST_FORUM_NAME, name);
        editor.commit();
    }

    public static String getLastForumName () {
        SharedPreferences sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(context);
        String name = sharedPreferences.getString(Const.KEY_LAST_FORUM_NAME, "校园交易");
        return name;

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

    public static String analysisSearchId(String href) {
        String searchid = null;
        Pattern pattern = Pattern.compile("searchid=\\d+");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
            searchid = matcher.group().replaceAll("searchid=", "");
            Log.w("headers", searchid );
        }
        return searchid;
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

    public static Drawable getForumIcon(String fid) {
        try {
            InputStream is = context.getAssets().open("forumlogo/common_" + fid + "_icon.gif");
            return Drawable.createFromStream(is, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
