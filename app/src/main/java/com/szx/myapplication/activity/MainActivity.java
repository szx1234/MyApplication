package com.szx.myapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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
import com.szx.myapplication.util.AsyncHttpUtil;
import com.szx.myapplication.util.Const;
import com.szx.myapplication.util.UrlUtil;
import com.szx.myapplication.util.Util;
import com.szx.myapplication.view.MyDialog;

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
    public Toolbar toolbar;
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
    private static String fid = "108";
    private boolean isFirstInThisForum = true;

    private long mLastPressedTime = 0;
    private long mCurrentPressedTime = 0;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activty);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        }
        /**
         * 刚开始的时候加载当前fid，如果是第一次加载那么就是上一次的fid
         */
        fid = App.getmCurrentFid();
        title = Util.getLastForumName();
        setTitle(title);

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

    public static void setFid(String fid){
        MainActivity.fid = fid;
    }


    private void initToolBarAndDrawerLayout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
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
                refreshCode();
            }
        });

        //给recycler设置一些东西，比如Linearlayoutmanager和adapter
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PostAdapter(list, recyclerView, this);
        recyclerView.setAdapter(adapter);

        //加载第一次数据
        refreshCode();

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
                    /**
                     * 修复了bug，解决了刷新时触发了loadMore而导致两次请求网络
                     */
                    if (!refreshLayout.isRefreshing()) {
                        loadMore();
                    }
                }
            }
        });
    }

    public void refreshCode() {
        if (!refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(true);
        currentPage = 1;
        isFirstInThisForum = true;
        list.clear();
        adapter.notifyDataSetChanged();
        loadMore();
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

        Intent intent;
        String str = "NULL";
        /**
         * TODO 添加nav各项逻辑
         */
        switch (id) {
            case R.id.nav_myArticle:
                intent = new Intent(MainActivity.this, MyCollectionOrMyArticleActivity.class);
                intent.putExtra("type", "MyArticle");
                startActivity(intent);
                break;
            case R.id.nav_myFriend:
                str = "朋友";
                break;
            case R.id.nav_myCollection:
                intent = new Intent(MainActivity.this, MyCollectionOrMyArticleActivity.class);
                intent.putExtra("type", "MyCollection");
                startActivity(intent);
                break;
            case R.id.nav_scanHistory:
                intent = new Intent(MainActivity.this, ScanHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_message:
                str = "消息";
                break;
            case R.id.nav_settings:
                intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                str = "关于";
                break;
            case R.id.nav_search:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_choose_forum:
                new MyDialog(this).show();
                break;
            default:
                str = "未知";
                break;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 50);

        return true;
    }


    private void loadMore() {
        AsyncHttpUtil.get(MainActivity.this, "forum.php?mod=forumdisplay&fid=" + fid + "&page=" + currentPage++ + "&mobile=2", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                if (list.size() > 0 && list.get(list.size() - 1) == null) {
                    list.remove(list.size() - 1);
                    adapter.notifyDataSetChanged();
                }
                Document doc = Jsoup.parse(new String(responseBody));
                Log.w("shit", "onSuccess: " + new String(responseBody));
                if (isFirstInThisForum) {
                    isFirstInThisForum = false;
                    if (TextUtils.isEmpty(Util.analysisPageNum(doc.select("span[title]").text()))){
                        maxPage = 100;
                    } else {
                        maxPage = Integer.valueOf(Util.analysisPageNum(doc.select("span[title]").text()));
                    }
                }
                Elements elements = doc.select("div.threadlist").select("li");
                for (Element element : elements) {
                    Element a = element.select("a").get(0);
                    String url = a.attr("href").replaceAll("amp;", "");
                    String replyCount = element.select("span.num").text();
                    boolean hasImg = element.select("span.icon_tu").select("img").attr("src").equals("") ? false : true;
                    String author = element.select("a").select("span.by").text();
                    String title = element.select("a").text();
                    title = title.substring(0, title.length() - author.length());
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

                if (list.size() > 0 && list.get(list.size() - 1) == null) {
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mCurrentPressedTime = System.currentTimeMillis();
            if(mCurrentPressedTime - mLastPressedTime > 2* Const.SECOND) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mLastPressedTime = mCurrentPressedTime;
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
