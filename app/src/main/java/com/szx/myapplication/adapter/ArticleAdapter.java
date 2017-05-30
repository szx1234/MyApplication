package com.szx.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.szx.myapplication.Listener.OnLoadMoreListener;
import com.szx.myapplication.R;
import com.szx.myapplication.activity.ArticleActivity;
import com.szx.myapplication.activity.UserDetailActivity;
import com.szx.myapplication.model.Article;
import com.szx.myapplication.webview.MyWebView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

/**
 * Created by songzhixin on 2017/5/24.
 */

public class ArticleAdapter extends RecyclerView.Adapter{
    private List<Article> data;
    private Context context;
    private OnLoadMoreListener loadMoreListener;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    public ArticleAdapter(Context context, List<Article> data){
        this.context = context;
        this.data = data;
    }


    static class CommentHolder extends  RecyclerView.ViewHolder{

        private CircleImageView img_user;
        private TextView text_user_name;
        private TextView text_reply_time;
        private ImageButton btn_reply;
        private MyWebView webView_content;
        public CommentHolder(View view) {
            super(view);
            img_user = (CircleImageView) view.findViewById(R.id.articlecomment_img_user);
            text_user_name = (TextView) view.findViewById(R.id.articlecomment_text_name);
            text_reply_time = (TextView) view.findViewById(R.id.articlecomment_text_replytime);
            btn_reply = (ImageButton) view.findViewById(R.id.articlecomment_btn_reply);
            webView_content = (MyWebView) view.findViewById(R.id.articlecomment_webview_content);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        try {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_comment, parent, false);

        }catch (Exception e){
            e.printStackTrace();
        }
        CommentHolder commentHolder = new CommentHolder(view);
        return commentHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommentHolder commentHolder = (CommentHolder) holder;
        final Article article = data.get(position);
        Log.w("picasso", "onBindViewHolder: " + article.getImgUrl());

        Picasso.with(context).load(article.getImgUrl()).into(commentHolder.img_user);

        commentHolder.text_user_name.setText(article.getUserName());
        commentHolder.text_reply_time.setText(article.getReplyTime());
        Log.w("mama", "onBindViewHolder: " + article.getImgUrl() );
        commentHolder.webView_content.loadDataWithBaseURL(null, article.getContent(), "text/html", "UTF-8", null);
        commentHolder.img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "你点了" + article.getUserDetailUrl(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra("url", article.getUserDetailUrl());
                intent.putExtra("name", article.getUserName());
                context.startActivity(intent);
            }
        });
        commentHolder.btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "你点击了" + article.getBtnUrl() + "heihei", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener){
        this.loadMoreListener = loadMoreListener;
    }
}
