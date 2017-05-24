package com.szx.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.szx.myapplication.R;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class MainActivity extends AppCompatActivity {
    String TAG = "hehe";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
        final AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore newCookie = new PersistentCookieStore(MainActivity.this);
        client.setCookieStore(newCookie);

//        client.get("http://bbs.rs.xidian.me/home.php?mod=space&uid=297362&do=profile&mycenter=1&mobile=2", new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
//                Log.w("ooo", "responseBody" + new String(responseBody));
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ((TextView)findViewById(R.id.textView)).setText(new String(responseBody));
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//            }
//        });

        client.get("http://bbs.rs.xidian.me/member.php?mod=logging&action=login&mobile=2", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {

                Log.w(TAG, "statusCode :" + statusCode);
                for(int i = 0; i < headers.length; i++)
                    Log.w(TAG, "header : " + headers[i]);

                Log.w(TAG, "responseBody" + new String(responseBody));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.textView)).setText(Html.fromHtml(new String(responseBody)));
                    }
                });

               // Map<String, String> params = new HashMap<String, String>();
                showCookie();
                RequestParams params = new RequestParams();
                params.put("username", "szx584820");
                params.put("password", "szx584820");
                client.post(MainActivity.this, "http://bbs.rs.xidian.me/member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LRaJa&mobile=2", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.w("hehe", "statusCode :" + statusCode);

                        showCookie();
                        for(int i = 0; i < headers.length; i++)
                            Log.w(TAG, "header : " + headers[i]);
                        Log.w("hehe", "responseBody" + new String(responseBody));



                        client.get("http://bbs.rs.xidian.me/forum.php?mod=guide&view=hot&mobile=2", new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Log.w("hehe", "statusCode :" + statusCode);
                                for(int i = 0; i < headers.length; i++)
                                    Log.w(TAG, "header : " + headers[i]);
                                Log.w("hehe", "responseBody" + new String(responseBody));

                                client.get("http://bbs.rs.xidian.me/home.php?mod=space&uid=297362&do=profile&mycenter=1&mobile=2", new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        Log.w("ooo", "responseBody" + new String(responseBody));
                                        showCookie();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.w("hehe", "onFailure: " + new String(responseBody
                        ));
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.w(TAG, "onFailure: " );
            }
        });
    }

    private void showCookie(){
        PersistentCookieStore mCookieStore = new PersistentCookieStore(getApplicationContext());
        List<Cookie> cookies = mCookieStore.getCookies();

        Log.e("cookies", "     " );
        Log.e("cookies", "     " );
        Log.e("cookies", "cookies.size() = " + cookies.size());

        for (Cookie cookie : cookies) {
            Log.e("cookies", cookie.getName() + " = " + cookie.getValue());
        }
    }
}
