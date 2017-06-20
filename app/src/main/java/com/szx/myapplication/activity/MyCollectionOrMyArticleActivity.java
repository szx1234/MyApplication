package com.szx.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.szx.myapplication.R;
import com.szx.myapplication.adapter.CollectionOrArticleAdapter;
import com.szx.myapplication.model.CollectionOrArticle;
import com.szx.myapplication.util.AsyncHttpUtil;
import com.szx.myapplication.util.UrlUtil;
import com.szx.myapplication.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by songzhixin on 2017/6/20.
 */

public class MyCollectionOrMyArticleActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<CollectionOrArticle> mDate;
    private CollectionOrArticleAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private Handler handler;
    private int mCurrentPage = 1;
    private int mMaxPage = 1;
    private boolean mFirstInThisActivity = true;
    private boolean isRequestting = false;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main_activty);

        initToolBar();
        initRecyclerViewAndAdapter();
        loadMore();
    }

    private void loadMore() {
        AsyncHttpUtil.get(this, type.equals("MyCollection") ? UrlUtil.getAbsUrl("home.php?mod=space&uid=" + Util.getUid() + "&do=favorite&type=thread&page=" + mCurrentPage++ + "&mobile=2") : UrlUtil.getAbsUrl("home.php?mod=space&uid=" + Util.getUid() + "&do=thread&page=" + mCurrentPage++ + "&view=me&mobile=2"), new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (mDate.size() > 0 && mDate.get(mDate.size() - 1) == null) {
                    mDate.remove(mDate.size() - 1);
                    mAdapter.notifyDataSetChanged();
                }

                String html = new String(responseBody);
                Document doc = Jsoup.parse(new String(responseBody));
                Elements elements = doc.select("div.threadlist").select("li");
                if (mFirstInThisActivity) {
                    mFirstInThisActivity = false;
                    if (html.contains("输入页码")) {
                        String size = doc.select("input.px").attr("size");
                        mMaxPage = Integer.valueOf(size);
                    }
                }

                for (Element element : elements) {
                    CollectionOrArticle item = new CollectionOrArticle();
                    item.setTitle(element.text());
                    item.setUrl(element.select("a").attr("href"));
                    Log.w("wari", "onSuccess: " + element.text());
                    Log.w("wari", "onSuccess: " + element.select("a").attr("href"));
                    mDate.add(item);
                    mAdapter.notifyDataSetChanged();
                }
                isRequestting = false;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (mDate.size() > 0 && mDate.get(mDate.size() - 1) == null) {
                    mDate.remove(mDate.size() - 1);
                    mAdapter.notifyDataSetChanged();
                }
                isRequestting = false;
            }
        });
    }

    private void initRecyclerViewAndAdapter() {
        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mDate = new ArrayList<>();
        mAdapter = new CollectionOrArticleAdapter(mDate, this);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPositon = mLinearLayoutManager.findLastVisibleItemPosition();
                int totalItem = mLinearLayoutManager.getItemCount();
                if (!isRequestting && totalItem == lastVisibleItemPositon + 1) {
                    if (mCurrentPage <= mMaxPage) {
                        mDate.add(null);
                        isRequestting = true;
                        mAdapter.notifyDataSetChanged();
                        loadMore();
                    }
                }
            }
        });
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.back);
        type = getIntent().getStringExtra("type");
        handler = new Handler();
        if ("MyArticle".equals(type))
            setTitle("我的帖子");
        else
            setTitle("我的收藏");
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
                mCurrentPage = 1;
                mMaxPage = 1;
                mFirstInThisActivity = true;
                mDate.clear();
                mAdapter.notifyDataSetChanged();
                loadMore();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default :
                break;
        }
        return true;
    }

}
