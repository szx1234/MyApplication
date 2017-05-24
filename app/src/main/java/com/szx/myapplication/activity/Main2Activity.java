package com.szx.myapplication.activity;

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
import com.szx.myapplication.R;
import com.szx.myapplication.model.GameTagHandler;
import com.szx.myapplication.model.Post;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

public class Main2Activity extends AppCompatActivity {

    Button mBtn_noCookie;
    Button mBtn_useCookie;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView = (TextView) findViewById(R.id.text_grade);
        Button button = (Button) findViewById(R.id.button2);
        mBtn_noCookie = (Button) findViewById(R.id.btn_notUseCookie);
        mBtn_useCookie = (Button) findViewById(R.id.btn_cookie);

        mBtn_noCookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(Main2Activity.this, "http://bbs.rs.xidian.me/forum.php?mod=forumdisplay&fid=110&mobile=2", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                        Log.w("2222", new String(responseBody));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(Html.fromHtml(new String(responseBody)));
                            }
                        });
                        Document doc = Jsoup.parse(new String(responseBody));
                        Elements elements = doc.select("li");
                        for(Element element : elements){
                            Element a = element.select("a").get(0);
                            String url = a.attr("href").replaceAll("amp;", "");
                            String replyCount = element.select("span.num").text();
                            boolean hasImg = element.select("span.icon_tu").select("img").attr("src").equals("") ? false : true;
                            String author = element.select("a").select("span.by").text();
                            String title = element.select("a").text();
                            title = title.substring(0, title.length()-author.length());
                            Log.w("element", "  ");
                            Log.w("element", author);
                            Log.w("element",title);
                            Log.w("element", url);
                            Log.w("element", replyCount);
                            Log.w("element", hasImg + "");
                            Post post = new Post(url, title, author, replyCount, hasImg);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.w("2222", "onFailure: " + new String(responseBody) );
                    }
                });
            }
        });

        mBtn_useCookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client = new AsyncHttpClient();
                PersistentCookieStore cookieStore = new PersistentCookieStore(Main2Activity.this);
                client.setCookieStore(cookieStore);
                client.get(Main2Activity.this, "http://bbs.rs.xidian.me/forum.php?mod=viewthread&tid=867616&fromguid=hot&extra=&mobile=2", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                        Log.w("2222", new String(responseBody));
                        Document doc = Jsoup.parse(new String(responseBody));
                        Elements elements = doc.select("div.message");
                        for(Element element : elements){
                            Log.w("日你妈", element.html());
                        }

                        textView.setText(Html.fromHtml(new String(responseBody), null, new GameTagHandler(Main2Activity.this)));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCookie();
            }
        });
    }


    private void showCookie(){
        PersistentCookieStore mCookieStore = new PersistentCookieStore(Main2Activity.this);
        List<Cookie> cookies = mCookieStore.getCookies();


        mCookieStore.clear();
        Log.e("cookies**", "     " );
        Log.e("cookies**", "     " );
        Log.e("cookies**", "cookies.size() = " + cookies.size());

        for (Cookie cookie : cookies) {
            Log.e("cookies", cookie.getName() + " = " + cookie.getValue());
        }
    }
}
