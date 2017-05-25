package com.szx.myapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.szx.myapplication.Listener.OnLoadMoreListener;
import com.szx.myapplication.R;
import com.szx.myapplication.adapter.PostAdapter;
import com.szx.myapplication.model.Post;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by songzhixin on 2017/5/16.
 */

public class RefreshActivity extends AppCompatActivity {
    private List<Post> list;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private PostAdapter adapter;
    Handler handler = new Handler();
    int count = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text);
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }, 2000);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        addDate();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PostAdapter(list, recyclerView, this);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //处理加载更多逻辑
                list.add(null);
                adapter.notifyItemInserted(list.size() - 1);
                addDate();
            }
        });

    }

    private void addDate() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(RefreshActivity.this, "http://bbs.rs.xidian.me/forum.php?mod=forumdisplay&fid=110&mobile=2", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                Log.w("2222", new String(responseBody));

                if (list.size() > 0) {
                    list.remove(list.size() - 1);
                    adapter.notifyDataSetChanged();
                }
                Document doc = Jsoup.parse(new String(responseBody));
                Elements elements = doc.select("li");
                for (Element element : elements) {
                    Element a = element.select("a").get(0);
                    String url = a.attr("href").replaceAll("amp;", "");
                    String replyCount = element.select("span.num").text();
                    boolean hasImg = element.select("span.icon_tu").select("img").attr("src").equals("") ? false : true;
                    String author = element.select("a").select("span.by").text();
                    String title = element.select("a").text();
                    title = title.substring(0, title.length() - author.length());
                    Log.w("element", "  ");
                    Log.w("element", author);
                    Log.w("element", title);
                    Log.w("element", url);
                    Log.w("element", replyCount);
                    Log.w("element", hasImg + "");
                    Post post = new Post(url, title, author, replyCount, hasImg);
                    list.add(post);
                }
                adapter.notifyDataSetChanged();
                PostAdapter.loadding = false;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                PostAdapter.loadding = false;
                if (list.size() > 0) {
                    list.remove(list.size() - 1);
                    adapter.notifyDataSetChanged();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RefreshActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}

