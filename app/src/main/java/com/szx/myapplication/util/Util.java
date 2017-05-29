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

    public static String analysisUid(String href){
        String uid = null;
        Pattern pattern = Pattern.compile("uid=\\d+");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
            uid = matcher.group().replaceAll("uid=", "");
        }
        return uid;
    }
}
