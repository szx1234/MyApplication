package com.szx.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.szx.myapplication.Listener.OnLoadMoreListener;
import com.szx.myapplication.R;
import com.szx.myapplication.adapter.PostAdapter;
import com.szx.myapplication.model.Post;
import com.szx.myapplication.model.UserDetail;
import com.szx.myapplication.util.AsyncHttpUtil;
import com.szx.myapplication.util.UrlUtil;
import com.szx.myapplication.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<Post> list;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private PostAdapter adapter;
    private CircleImageView circleImageView;
    private TextView userName;
    private NavigationView navigationView;
    private Handler handler = new Handler();
    private DrawerLayout mDrawerLayout;
    private int currentPage = 1;
    private int maxPage = 1;
    private String fid = "108";
    private boolean isFirstInThisForum = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activty);

        fid = Util.getLastForumFid();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        /**
         * 初始化RecyclerView和数据
         */
        initRecyclerAndData();

        /**
         * 初始化用户信息
         */
        initUserDetail();

        /**
         * 初始化Toolbar等控件
         */
        initToolBarAndDrawerLayout();
    }



    private void initToolBarAndDrawerLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initRecyclerAndData() {
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                list.clear();
                adapter.notifyDataSetChanged();
                loadMore();
            }
        });
        //加载第一次数据
        loadMore();

        //给recycler设置一些东西，比如Linearlayoutmanager和adapter
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PostAdapter(list, recyclerView, this);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //处理加载更多逻辑
                if (currentPage <= maxPage) {
                    if (list.size() > 0) {
                        PostAdapter.loadding = true;
                        list.add(null);
                        adapter.notifyItemInserted(list.size() - 1);
                    }
                    loadMore();
                }
            }
        });
    }

    private void initUserDetail() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        userName = (TextView) view.findViewById(R.id.nav_user_name);
        userName.setText(Util.getUserName());
        circleImageView = (CircleImageView) view.findViewById(R.id.nav_user_icon);
        Picasso.with(this).load(UrlUtil.getAbsUrl("ucenter/avatar.php?uid=" + Util.getUid() + "&size=small")).into(circleImageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserDetailActivity.class);
                intent.putExtra("uid", Util.getUid());
                intent.putExtra("name", Util.getUserName());
                startActivity(intent);
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }, 100);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Toast.makeText(this, item.getItemId(), Toast.LENGTH_SHORT).show();
        String str = "NULL";
        /**
         * TODO 添加nav各项逻辑
         */
        switch (id) {
            case R.id.nav_myArticle:
                str = "文章";
                break;
            case R.id.nav_myFriend:
                str = "朋友";
                break;
            case R.id.nav_myCollection:
                str = "收藏";
                break;
            case R.id.nav_scanHistory:
                str = "历史";
                break;
            case R.id.nav_message:
                str = "消息";
                break;
            case R.id.nav_settings:
                str = "设置";
                break;
            case R.id.nav_about:
                str = "关于";
                break;
            case R.id.nav_search:
                str = "搜索";
                break;
            default:
                str = "未知";
                break;
        }
        Toast.makeText(this, "你点击了 ： " + str, Toast.LENGTH_SHORT).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadMore() {
        AsyncHttpUtil.get(MainActivity.this, "forum.php?mod=forumdisplay&fid=" + fid + "&page=" + currentPage++ + "&mobile=2", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                Log.w("2222", new String(responseBody).replaceAll("amp;", ""));

                if (list.size() > 0 && list.get(list.size()-1) == null) {
                    list.remove(list.size() - 1);
                    adapter.notifyDataSetChanged();
                }
                Document doc = Jsoup.parse(new String(responseBody));
                Log.w("shit", "onSuccess: " + new String(responseBody));
                if (isFirstInThisForum) {
                    isFirstInThisForum = false;
                    maxPage = Integer.valueOf(Util.analysisPageNum(doc.select("span[title]").text()));
                }
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
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                PostAdapter.loadding = false;
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);

                if (list.size() > 0 && list.get(list.size()-1) == null) {
                    list.remove(list.size() - 1);
                    adapter.notifyDataSetChanged();
                }
                PostAdapter.loadding = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
