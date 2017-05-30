package com.szx.myapplication.activity;

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
import com.szx.myapplication.util.AsyncHttpUtil;
import com.szx.myapplication.util.UrlUtil;
import com.szx.myapplication.util.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

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
    NavigationView navigationView;
    Handler handler = new Handler();
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activty);

        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
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

        
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        userName = (TextView) view.findViewById(R.id.nav_user_name);
        userName.setText(Util.getUserName());
        circleImageView = (CircleImageView) view.findViewById(R.id.nav_user_icon);
        Picasso.with(this).load(UrlUtil.getAbsUrl("ucenter/avatar.php?uid=" + Util.getUid() + "&size=small")).into(circleImageView);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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


    private void addDate() {
        AsyncHttpUtil.get(MainActivity.this, "forum.php?mod=forumdisplay&fid=110&mobile=2", new AsyncHttpResponseHandler() {
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
                        Toast.makeText(MainActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
