package com.szx.myapplication.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.squareup.picasso.Picasso;
import com.szx.myapplication.R;
import com.szx.myapplication.model.UserDetail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private String mTitle;
    private String url = "http://rs.xidian.edu.cn/home.php?mod=space&uid=299032&do=profile&mobile=2";
    private CircleImageView mImg;
    private TextView mScore;
    private TextView mGold;
    private TextView mUpload;
    private TextView mDownload;
    private TextView mPublishSeedNum;
    private TextView mChipNum;
    private TextView mProtectSeddNum;
    private TextView mPeopleQuality;


    String imgUrl;
    String score;
    String gold;
    String upload;
    String download;
    String publishSeedNum;
    String chipNum;
    String protectSeddNum;
    String peopleQuality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsiing_toolbar_layout);
        mCollapsingToolbarLayout.setExpandedTitleGravity(Gravity.CENTER_HORIZONTAL);
        mCollapsingToolbarLayout.setExpandedTitleMarginBottom(-0);

        url = "http://bbs.rs.xidian.me/" + getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("name");
        mCollapsingToolbarLayout.setTitle(mTitle);

        initData();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "你点击了注销", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        (new GetUserDetailTask()).execute();
//        AsyncHttpClient client = new AsyncHttpClient();
//        PersistentCookieStore cookieStore = new PersistentCookieStore(UserDetailActivity.this);
//        client.setCookieStore(cookieStore);
//        client.get(UserDetailActivity.this, url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Document doc = Jsoup.parse(new String(responseBody));
//                imgUrl = doc.select("div.avatar_m").select("img").attr("src");
//                Elements elements = doc.select("div.user_box").select("li");
//
//                Log.w("数量", new String(responseBody) );
//                Log.w("数量", "onSuccess: " + elements.size()+ "");
//                score = elements.get(0).select("span").text();
//                gold = elements.get(1).select("span").text();
//                upload = elements.get(2).select("span").text();
//                download = elements.get(3).select("span").text();
//                publishSeedNum = elements.get(4).select("span").text();
//                chipNum = elements.get(5).select("span").text();
//                protectSeddNum = elements.get(6).select("span").text();
//                peopleQuality = elements.get(7).select("span").text();
//
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Picasso.with(UserDetailActivity.this).load(imgUrl).into(mImg);
//                        Log.w("数量", "run: " + imgUrl);
//                        mScore.setText(score);
//                        mGold.setText(gold);
//                        mUpload.setText(upload);
//                        mDownload.setText(download);
//                        mPublishSeedNum.setText(publishSeedNum);
//                        mChipNum.setText(chipNum);
//                        mPublishSeedNum.setText(publishSeedNum);
//                        mProtectSeddNum.setText(protectSeddNum);
//                        mPeopleQuality.setText(peopleQuality);
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Toast.makeText(UserDetailActivity.this, "获取个人信息失败！", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void initData() {
        // url = getIntent().getStringExtra("url");
        mImg = (CircleImageView) findViewById(R.id.userdetail_user_img);
        mScore = (TextView) findViewById(R.id.userdetail_score);
        mGold = (TextView) findViewById(R.id.userdetail_gold);
        mUpload = (TextView) findViewById(R.id.userdetail_upload);
        mDownload = (TextView) findViewById(R.id.userdetail_download);
        mPublishSeedNum = (TextView) findViewById(R.id.userdetail_public_seed);
        mChipNum = (TextView) findViewById(R.id.userdetail_chip);
        mProtectSeddNum = (TextView) findViewById(R.id.userdetail_protectedseed);
        mPeopleQuality = (TextView) findViewById(R.id.userdetail_people_quality);
    }


    class GetUserDetailTask extends AsyncTask<Void, Void, Void> {
        String imgUrl;
        String score;
        String gold;
        String upload;
        String download;
        String publishSeedNum;
        String chipNum;
        String protectSeddNum;
        String peopleQuality;

        @Override
        protected Void doInBackground(Void... params) {

            Log.w("doInBack", "进入");
            AsyncHttpClient client = new AsyncHttpClient();
            PersistentCookieStore cookieStore = new PersistentCookieStore(UserDetailActivity.this);
            client.setCookieStore(cookieStore);
            client.get(UserDetailActivity.this, url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Document doc = Jsoup.parse(new String(responseBody));
                    imgUrl = doc.select("div.avatar").select("img").attr("src");
                    Elements elements = doc.select("div.user_box").select("li");
                    score = elements.get(0).select("span").text();
                    gold = elements.get(1).select("span").text();
                    upload = elements.get(3).select("span").text();
                    download = elements.get(4).select("span").text();
                    publishSeedNum = elements.get(5).select("span").text();
                    chipNum = elements.get(6).select("span").text();
                    protectSeddNum = elements.get(7).select("span").text();
                    peopleQuality = elements.get(8).select("span").text();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(UserDetailActivity.this, "获取个人信息失败！", Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.w("doInBack", "onPostExecute: ");
            Picasso.with(UserDetailActivity.this).load(imgUrl).into(mImg);
            mScore.setText(score);
            mGold.setText(gold);
            mUpload.setText(upload);
            mDownload.setText(download);
            mPublishSeedNum.setText(publishSeedNum);
            mChipNum.setText(chipNum);
            mPublishSeedNum.setText(publishSeedNum);
            mProtectSeddNum.setText(protectSeddNum);
            mPeopleQuality.setText(peopleQuality);
        }
    }
}
