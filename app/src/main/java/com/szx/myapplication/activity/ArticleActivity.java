package com.szx.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.szx.myapplication.Listener.OnLoadMoreListener;
import com.szx.myapplication.R;
import com.szx.myapplication.adapter.ArticleAdapter;
import com.szx.myapplication.model.Article;
import com.szx.myapplication.util.AsyncHttpUtil;
import com.szx.myapplication.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by songzhixin on 2017/5/24.
 */

public class ArticleActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Article> data;
    private ArticleAdapter adapter;
    private String url;
    private TextView text_title;
    private int currentPage = 1;
    private int maxPage = 1;
    private String tid;

    private boolean isFirstInThisArticle = true;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        text_title = (TextView) findViewById(R.id.article_text_title);
        url = getIntent().getStringExtra("url");

        tid = Util.analysisTid(url);
        data = new ArrayList<Article>();
        mRecyclerView = (RecyclerView) findViewById(R.id.article_recycler_view);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        adapter = new ArticleAdapter(data, mRecyclerView, this);
        adapter.setLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (currentPage <= maxPage) {
                    ArticleAdapter.loadding = true;
                    data.add(null);
                    adapter.notifyItemInserted(data.size() - 1);
                    onLoadMore();
                }
            }
        });
        mRecyclerView.setAdapter(adapter);
        onLoadMore();
    }

    private void onLoadMore() {
        AsyncHttpUtil.get(ArticleActivity.this, "forum.php?mod=viewthread&tid=" + tid + "&extra=&page=" + currentPage++ + "&mobile=2", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                /**
                 * 去掉Footer
                 */
                if(data.size() > 0 && data.get(data.size()-1) == null){
                    data.remove(data.size() - 1);
                    adapter.notifyDataSetChanged();
                }

                /**
                 * 获取maxPage
                 */
                Document doc = Jsoup.parse(new String(responseBody));
                if (isFirstInThisArticle) {
                    isFirstInThisArticle = false;
                    if(!TextUtils.isEmpty(Util.analysisPageNum(doc.select("span[title]").text())))
                        maxPage = Integer.valueOf(Util.analysisPageNum(doc.select("span[title]").text()));
                    else {
                        maxPage = 1;
                    }
                }

                /**
                 * 把带有static的图片换成本地的贴吧文件
                 */
                for (Element temp : doc.select("img[src^=static/image/smiley/tieba/]")) {
                    String imgUrl = temp.attr("src");
                    String newimgurl = imgUrl.replace("static/image/smiley/tieba/", "file:///android_asset/tieba/");
                    temp.attr("src", newimgurl);
                }

                /**
                 * 给图片src之前加上基地址
                 */
                for (Element temp : doc.select("img[src^=forum.php]")) {
                    temp.attr("src", "http://bbs.rs.xidian.me/" + temp.attr("src"));
                }

                /**
                 * 删除图片上的链接，防止出bug
                 */
                for (Element temp : doc.select("a.orange")) {
                    temp.removeAttr("href");
                }

                Elements elements = doc.select("div.postlist");

                /**
                 * 添加标题
                 */
                if (currentPage == 2) { //之前currentPage++过所以是2
                    final Elements titles = elements.select("h2");
                    if (titles.size() > 0)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text_title.setText(titles.get(0).text().replaceAll("只看楼主", ""));
                            }
                        });
                }
                elements = doc.select("div.plc.cl");
                for (Element element : elements) {
                    Article article = new Article();
                    article.setImgUrl(element.select("span.avatar").select("img").attr("src"));
                    article.setUserDetailUrl(element.select("ul.authi").select("li.grey").select("b").select("a").attr("href"));
                    article.setUserName(element.select("ul.authi").select("li.grey").select("b").text());
                    article.setReplyTime(element.select("ul.authi").select("li.grey.rela").text().replaceAll("收藏", ""));
                    article.setBtnUrl(element.select("div.replybtn").select("input").attr("href"));
                    article.setContent(element.select("div.message").html());
                    data.add(article);
                }

                adapter.notifyDataSetChanged();

                /**
                 * 解决莫名的多一个问题
                 */
                if(data.size() > 0){
                    data.remove(data.size()-1);
                    adapter.notifyDataSetChanged();
                }
                ArticleAdapter.loadding = false;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ArticleAdapter.loadding = false;
                if(data.size() > 0 && data.get(data.size()-1) == null){
                    data.remove(data.size() - 1);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
