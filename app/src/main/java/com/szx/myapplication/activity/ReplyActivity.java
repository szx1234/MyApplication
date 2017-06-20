package com.szx.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.szx.myapplication.R;
import com.szx.myapplication.model.Article;
import com.szx.myapplication.model.UserDetail;
import com.szx.myapplication.util.AsyncHttpUtil;
import com.szx.myapplication.util.UrlUtil;
import com.szx.myapplication.util.Util;
import com.szx.myapplication.webview.MyWebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songzhixin on 2017/6/1.
 */

public class ReplyActivity extends AppCompatActivity {
    private TextInputEditText mInputEditText;
    private ImageButton mImageButton_reply;
    private Article mArticle;

    private CircleImageView mCircleImageView;
    private TextView mUserName;
    private TextView mReplyTime;
    private MyWebView myWebView;
    private ImageButton mReplyButton;

    private RequestParams mParams;
    private String url;
    private String replyUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        url = getIntent().getStringExtra("url");

        init();

        initContent();

        initToolBar();

        initParams();
        mImageButton_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * TODO 增加错误处理机制，回复时间，回复内容等等，现在只是简单的回复了内容
                 */
                mParams.put("message", mInputEditText.getText().toString());
                Toast.makeText(ReplyActivity.this, "加载中。。。", Toast.LENGTH_SHORT).show();
                AsyncHttpUtil.post(ReplyActivity.this, replyUrl, mParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        MActivity.open(ReplyActivity.this, new String(responseBody));
                        Toast.makeText(ReplyActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(ReplyActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
//                        MActivity.open(ReplyActivity.this, new String(error.getMessage()));
                        finish();
                    }
                });
            }
        });
    }

    private void initParams() {
        mParams = new RequestParams();
        AsyncHttpUtil.get(ReplyActivity.this, url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Document document = Jsoup.parse(new String(responseBody));
                Elements els = document.select("#postform");
                mParams.put("formhash", els.select("input[name=formhash]").attr("value"));
                mParams.put("posttime", els.select("input[name=posttime]").attr("value"));
                mParams.put("noticeauthor", els.select("input[name=noticeauthor]").attr("value"));
                mParams.put("noticetrimstr", els.select("input[name=noticetrimstr]").attr("value"));
                mParams.put("reppid", els.select("input[name=reppid]").attr("value"));
                mParams.put("reppost", els.select("input[name=reppost]").attr("value"));
                mParams.put("noticeauthormsg", els.select("input[name=noticeauthormsg]").attr("value"));
                mParams.put("replysubmit", "yes");
                replyUrl = els.attr("action");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.reply_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.back);
        actionBar.setTitle("回复:" + mArticle.getUserName());
    }

    private void initContent() {
        Picasso.with(this).load(mArticle.getImgUrl()).into(mCircleImageView);
        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReplyActivity.this, UserDetailActivity.class);
                intent.putExtra("uid", Util.analysisUid(mArticle.getImgUrl()));
                intent.putExtra("name", mArticle.getUserName());
                startActivity(intent);
            }
        });
        mUserName.setText(mArticle.getUserName());
        mReplyTime.setText(mArticle.getReplyTime());
        myWebView.loadDataWithBaseURL(null, mArticle.getContent(), "text/html", "UTF-8", null);
    }

    private void init() {
        mArticle = (Article) getIntent().getSerializableExtra("article");
        mCircleImageView = (CircleImageView) findViewById(R.id.articlecomment_img_user);
        mUserName = (TextView) findViewById(R.id.articlecomment_text_name);
        mReplyTime = (TextView) findViewById(R.id.articlecomment_text_replytime);
        myWebView = (MyWebView) findViewById(R.id.articlecomment_webview_content);
        mReplyButton = (ImageButton) findViewById(R.id.articlecomment_btn_reply);
        mReplyButton.setVisibility(View.INVISIBLE);

        mInputEditText = (TextInputEditText) findViewById(R.id.reply_input);
        mImageButton_reply = (ImageButton) findViewById(R.id.reply_btn_reply);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
