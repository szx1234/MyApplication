package com.szx.myapplication.activity;

import android.os.Bundle;
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

import com.squareup.picasso.Picasso;
import com.szx.myapplication.R;
import com.szx.myapplication.model.Article;
import com.szx.myapplication.webview.MyWebView;

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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        init();

        initContent();

        initToolBar();

        mImageButton_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReplyActivity.this, "你点了回复: url = " + mArticle.getBtnUrl(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.reply_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.menu_return);
        actionBar.setTitle("回复:" + mArticle.getUserName());
    }

    private void initContent() {
        Picasso.with(this).load(mArticle.getImgUrl()).into(mCircleImageView);
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
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
