package com.szx.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.ResponseHandlerInterface;
import com.szx.myapplication.Listener.OnLoadMoreListener;
import com.szx.myapplication.R;
import com.szx.myapplication.adapter.ArticleAdapter;
import com.szx.myapplication.model.Article;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private int crrentPage = 0;
    private int totalPage;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        text_title = (TextView) findViewById(R.id.article_text_title);
        url = getIntent().getStringExtra("url");
        Toast.makeText(this, "我收到的URL是：" + url, Toast.LENGTH_SHORT).show();
        data = new ArrayList<Article>();
        mRecyclerView = (RecyclerView) findViewById(R.id.article_recycler_view);
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
        mRecyclerView.setAdapter(adapter);
        initHeader();
//        addData();
    }

    private void addData() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ArticleActivity.this, "http://bbs.rs.xidian.me/" + url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void initHeader() {
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore cookie = new PersistentCookieStore(this);
        client.setCookieStore(cookie);
        client.get(ArticleActivity.this, "http://bbs.rs.xidian.me/" + url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final byte[] responseBody) {
                Log.w("想尿尿", "" + new String(responseBody));

                /**
                 * 把带有static的图片换成本地的贴吧文件
                 */
                Document doc = Jsoup.parse(new String(responseBody));
                for (Element temp : doc.select("img[src^=static/image/smiley/tieba/]")) {
                    //System.out.print("replace before------>>>>>>>>>>>"+temp+"\n");
                    String imgUrl = temp.attr("src");
                    String newimgurl =  imgUrl.replace("static/image/smiley/tieba/","file:///android_asset/tieba/");
                    //System.out.print("replace------>>>>>>>>>>>"+imgUrl+newimgurl+"\n");
                    Log.w("tieba666", newimgurl );
                    temp.attr("src", newimgurl);
                }

                /**
                 * 给图片src之前加上基地址
                 */
                for (Element temp : doc.select("img[src^=forum.php]")){
                    temp.attr("src", "http://bbs.rs.xidian.me/"+ temp.attr("src"));
                    Log.w("wodema", "onSuccess: " + temp.attr("src") );
                }
                for(Element temp : doc.select("a.orange")){
                    Log.w("showme", "onSuccess: " + temp.html());

                      temp.removeAttr("href");
//                    temp.attr("href", "");
                }
                Elements elements = doc.select("div.postlist");
                final Elements titles = elements.select("h2");
                if (titles.size() > 0)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text_title.setText(titles.get(0).text().replaceAll("只看楼主", ""));
//                            text_title.setText(new String(responseBody));
                        }
                    });
                elements = doc.select("div.plc.cl");
                for(Element element : elements){
                    Log.w("想死", "onSuccess: " + element.html() );
                    Article article = new Article();
                    article.setImgUrl(element.select("span.avatar").select("img").attr("src"));
                    article.setUserDetailUrl(element.select("ul.authi").select("li.grey").select("b").select("a").attr("href"));
                    article.setUserName(element.select("ul.authi").select("li.grey").select("b").text());
                    article.setReplyTime(element.select("ul.authi").select("li.grey.rela").text().replaceAll("收藏", ""));
                    article.setBtnUrl(element.select("div.replybtn").select("input").attr("href"));
                    article.setContent(element.select("div.message").html());
                   // if(!element.select("div.message").text().equals(""))
                    data.add(article);
                    adapter.notifyDataSetChanged();
                }
                data.remove(data.size()-1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
