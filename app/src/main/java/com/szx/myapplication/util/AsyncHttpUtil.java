package com.szx.myapplication.util;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.szx.myapplication.activity.App;

/**
 * Created by songzhixin on 2017/5/27.
 */

public class AsyncHttpUtil {
    private static AsyncHttpClient client;
    private static PersistentCookieStore cookieStore;

    static{
        client = new AsyncHttpClient();
        cookieStore = new PersistentCookieStore(App.getAppContext());
        client.setCookieStore(cookieStore);
    }

    public static void get(Context context, String url, AsyncHttpResponseHandler handler){
        client.get(context, url, handler);
    }

    public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler handler){
        client.post(context, url, params, handler);
    }

    public static void clearCookieStore(){
        cookieStore.clear();
    }

}
