package com.szx.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.szx.myapplication.Listener.OnLoadMoreListener;
import com.szx.myapplication.R;
import com.szx.myapplication.model.Article;
import com.szx.myapplication.webview.MyWebView;

import java.util.List;

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

        private ImageView img_user;
        private TextView text_user_name;
        private TextView text_reply_time;
        private ImageButton btn_reply;
        private MyWebView webView_content;
        public CommentHolder(View view) {
            super(view);
            img_user = (ImageView) view.findViewById(R.id.articalcomment_img_user);
            text_user_name = (TextView) view.findViewById(R.id.articalcomment_text_name);
            text_reply_time = (TextView) view.findViewById(R.id.articalcomment_text_replytime);
            webView_content = (MyWebView) view.findViewById(R.id.articalcomment_webview_content);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_comment, parent, false);
        CommentHolder commentHolder = new CommentHolder(view);
        return commentHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
