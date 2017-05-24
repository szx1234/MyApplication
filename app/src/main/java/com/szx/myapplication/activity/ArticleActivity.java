package com.szx.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.szx.myapplication.Listener.OnLoadMoreListener;
import com.szx.myapplication.R;
import com.szx.myapplication.adapter.ArticleAdapter;
import com.szx.myapplication.model.Article;

import java.util.List;

/**
 * Created by songzhixin on 2017/5/24.
 */

public class ArticleActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Article> data;
    private ArticleAdapter adapter;
    private String url;
    private int crrentPage = 0;
    private int totalPage;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        url = getIntent().getStringExtra("url");
        mRecyclerView = (RecyclerView) findViewById(R.id.artical_recycler_view);
        adapter = new ArticleAdapter(this, data);
        adapter.setLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                /**
                 *TODO 实现加载更多逻辑
                 */
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        initHeader();
        addData();
    }

    private void addData() {
        /**
         * TODO 加载更多
         */
    }

    private void initHeader() {
        /**
         * TODO 初始化头部
         */
    }
}
