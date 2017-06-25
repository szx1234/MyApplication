package com.szx.myapplication.activity;

import android.animation.Animator;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.szx.myapplication.R;
import com.szx.myapplication.adapter.CollectionOrArticleAdapter;
import com.szx.myapplication.model.CollectionOrArticle;
import com.szx.myapplication.util.AsyncHttpUtil;
import com.szx.myapplication.util.KeyboardUtil;
import com.szx.myapplication.util.UrlUtil;
import com.szx.myapplication.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by songzhixin on 2017/6/21.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView mCardView;
    private EditText mInput;
    private int mCurrentPage = 1;
    private int mMaxPage = 1;
    private TextView title;
    private RecyclerView recyclerView;
    private List<CollectionOrArticle> mData;
    private LinearLayoutManager mLinearLayoutManager;
    private CollectionOrArticleAdapter mAdapter;
    private Animator animator;
    private String searchId;
    private String url;

    private ImageView start_search;
    private ImageView nav_search;


    private View view;
    private boolean mIsLoadding = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init() {

        view = findViewById(R.id.main_window);
        start_search = (ImageView) findViewById(R.id.start_search);
        nav_search = (ImageView) findViewById(R.id.nav_search);
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.nav_back).setOnClickListener(this);
        start_search.setOnClickListener(this);
        nav_search.setOnClickListener(this);

        mCardView = (CardView) findViewById(R.id.search_card);
        mInput = (EditText) findViewById(R.id.search_input);
        title = (TextView) findViewById(R.id.nav_title);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mData = new ArrayList<>();
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new CollectionOrArticleAdapter(mData, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int itemCount = mLinearLayoutManager.getItemCount();
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();

                if (itemCount == lastVisibleItemPosition + 1) {
                    loadMore();
                }
            }
        });
    }

    private void loadMore() {
        if (mCurrentPage < mMaxPage && !mIsLoadding) {
            mIsLoadding = true;
            mData.add(null);
            mAdapter.notifyItemInserted(mData.size());

            AsyncHttpUtil.get(SearchActivity.this, UrlUtil.getSearchDataUrl(searchId, mCurrentPage++), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    new ParseDataTask().execute(new String(responseBody));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    mIsLoadding = false;
                    if (mData.size() > 0 && mData.get(mData.size() - 1) == null) {
                        mData.remove(mData.size() - 1);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
            case R.id.nav_back:
                finish();
                break;
            case R.id.start_search:
                startSearch();
                break;
            case R.id.nav_search:
                clearDataAndShowSearch();
                break;

        }
    }

    private void clearDataAndShowSearch() {
        clearData();
        show_search_view();
    }

    private void clearData() {
        mData.clear();
        mCurrentPage = 1;
        mMaxPage = 1;
        mAdapter.notifyDataSetChanged();
    }

    private void startSearch() {
        if (!TextUtils.isEmpty(mInput.getText())) {
            hide_search_view();
            getFirstData();
        } else {
            Toast.makeText(this, "输入为空", Toast.LENGTH_SHORT).show();
        }

    }

    private void getFirstData() {
        String str = mInput.getText().toString();
        RequestParams params = new RequestParams();
        params.put("searchsubmit", "yes");
        params.put("srchtxt", str);
        AsyncHttpUtil.post(this, UrlUtil.getSearchUrl(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                if (str.contains("只能进行一次搜索")) {
                    Snackbar.make(view, "抱歉，15秒内只能进行一次搜索", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "网络错误", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                for (int i = headers.length - 1; i >= 0; i--) {
                    if ("location".equals(headers[i].getName())) {
                        url = headers[i].getValue();
                        searchId = Util.analysisSearchId(headers[i].getValue());
                        AsyncHttpUtil.get(SearchActivity.this, url, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String str = new String(responseBody);
                                if (str.contains("没有找到匹配结果")) {
                                    Snackbar.make(view, "抱歉，没有找到匹配结果", Snackbar.LENGTH_LONG).show();
                                    title.setText("没有找到匹配结果");
                                    return;
                                }
                                /**
                                 * 获得最大页数
                                 */
                                Document doc = Jsoup.parse(str);
                                Elements elements = doc.select("div.pg");
                                if (elements.size() > 0) {
                                    mMaxPage = Integer.valueOf(Util.analysisPageNum(elements.get(0).select("span").attr("title")));
                                }
                                new ParseDataTask().execute(str);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Snackbar.make(view, "网络错误", Snackbar.LENGTH_LONG).show();
                            }
                        });
                        break;
                    }
                }
            }
        });

    }

    public class ParseDataTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            if (mData.size() > 0 && mData.get(mData.size() - 1) == null) {
                mData.remove(mData.size() - 1);
            }
            String str = params[0];
            Document doc = Jsoup.parse(str);
            Elements elements = doc.select("div.threadlist").select("li");
            for (Element element : elements) {
                CollectionOrArticle collectionOrArticle = new CollectionOrArticle();
                collectionOrArticle.setTitle(element.text());
                collectionOrArticle.setUrl(element.select("a").attr("href"));
                mData.add(collectionOrArticle);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            mIsLoadding = false;
            mAdapter.notifyDataSetChanged();
        }
    }



    private void hide_search_view() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(
                    mCardView,
                    mCardView.getWidth(),
                    0,
                    (float) Math.hypot(mCardView.getWidth(), mCardView.getHeight()),
                    0);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.setDuration(300);
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mCardView.setVisibility(View.INVISIBLE);
                    KeyboardUtil.hideKeyboard(mInput);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            mCardView.setVisibility(View.INVISIBLE);
            KeyboardUtil.hideKeyboard(mInput);
        }
    }

    private void show_search_view() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCardView.setVisibility(View.VISIBLE);
            animator = ViewAnimationUtils.createCircularReveal(
                    mCardView,
                    mCardView.getWidth(),
                    0,
                    0,
                    (float) Math.hypot(mCardView.getWidth(), mCardView.getHeight()));

            animator.setInterpolator(new AccelerateInterpolator());
            animator.setDuration(300);
            animator.start();
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    KeyboardUtil.showKeyboard(mInput);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        } else {
            mCardView.setVisibility(View.VISIBLE);
            KeyboardUtil.showKeyboard(mInput);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCardView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    show_search_view();
                }
            });
        }
    }
}
