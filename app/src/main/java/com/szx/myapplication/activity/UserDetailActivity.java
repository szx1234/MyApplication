package com.szx.myapplication.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.szx.myapplication.R;
import com.szx.myapplication.util.AsyncHttpUtil;
import com.szx.myapplication.util.UrlUtil;
import com.szx.myapplication.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private String mTitle;
    private CircleImageView mImg;
    private TextView mScore;
    private TextView mGold;
    private TextView mUpload;
    private TextView mDownload;
    private TextView mPublishSeedNum;
    private TextView mChipNum;
    private TextView mProtectSeddNum;
    private TextView mPeopleQuality;
    private FloatingActionButton mFab;

    String imgUrl;
    String score;
    String gold;
    String upload;
    String download;
    String publishSeedNum;
    String chipNum;
    String protectSeedNum;
    String peopleQuality;


    private boolean isMyUid = false;
    private String uid;
    private final float GB = 1024*1024*1024;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.menu_return);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsiing_toolbar_layout);
        mCollapsingToolbarLayout.setExpandedTitleGravity(Gravity.CENTER_HORIZONTAL);
        mCollapsingToolbarLayout.setExpandedTitleMarginBottom(-200);

        uid = getIntent().getStringExtra("uid");
        mTitle = getIntent().getStringExtra("name");

        mCollapsingToolbarLayout.setTitle(mTitle);

        initData();
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * 聊天与注销
                 */

                String str = "";
                if (isMyUid)
                    str = "你点击了注销";
                else
                    str = "你点击了聊天";
                Snackbar.make(view, str, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        isMyUid = Util.getUid().equals(uid);

        AsyncHttpUtil.get(UserDetailActivity.this, UrlUtil.getUserDetailUrl(uid), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Document doc = Jsoup.parse(new String(responseBody));
                imgUrl = doc.select("div.avatar_m").select("img").attr("src");
                Elements elements = doc.select("div.user_box").select("li");

                Log.w("数量", new String(responseBody));
                Log.w("数量", "onSuccess: " + elements.size() + "");
                score = elements.get(0).select("span").text();
                gold = elements.get(1).select("span").text();

                upload = new DecimalFormat("###,###,###.##").format(Float.valueOf(elements.get(2).select("span").text())/GB) + "GB";
                download = new DecimalFormat("###,###,###.##").format(Float.valueOf(elements.get(3).select("span").text())/GB) + "GB";

                publishSeedNum = elements.get(4).select("span").text();
                chipNum = elements.get(5).select("span").text();
                protectSeedNum = elements.get(6).select("span").text();
                peopleQuality = elements.get(7).select("span").text();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(UserDetailActivity.this).load(imgUrl).into(mImg);
                        Log.w("数量", "run: " + imgUrl);
                        mScore.setText(score);
                        mGold.setText(gold);
                        mUpload.setText(upload);
                        mDownload.setText(download);
                        mPublishSeedNum.setText(publishSeedNum);
                        mChipNum.setText(chipNum);
                        mPublishSeedNum.setText(publishSeedNum);
                        mProtectSeddNum.setText(protectSeedNum);
                        mPeopleQuality.setText(peopleQuality);
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(UserDetailActivity.this, "获取个人信息失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /**
         * 区别是自己或者是别人的情况
         */
        getMenuInflater().inflate(R.menu.menu_userdetail_activity, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_addfriend);
        if(isMyUid) {
            menuItem.setVisible(false);
            mFab.setImageResource(R.mipmap.fab_delete );
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_addfriend:
                /**
                 * TODO 添加好友
                 */
                Toast.makeText(this, "添加好友", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    //初始化控件
    private void initData() {
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

}
