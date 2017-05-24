package com.szx.myapplication.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.szx.myapplication.Listener.OnLoadMoreListener;
import com.szx.myapplication.R;
import com.szx.myapplication.model.Post;

import java.util.List;

/**
 * Created by songzhixin on 2017/5/16.
 */

public class PostAdapter extends RecyclerView.Adapter {
    private static final int TYPE__FOOTER = 0;
    private static final int TYPE_NORMOL = 1;
    private List<Post> mPostList;
    private OnLoadMoreListener onLoadMoreListener;
    public static boolean loadding = false;
    //当还剩5个帖子时进行加载
    //private int visibleThreshold = 5;

    private static class NormalHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView authorName;
        TextView replyCount;
        TextView hasImg;
        View view;
        public NormalHolder(View view) {
            super(view);
            this.view = view;
            title = (TextView) view.findViewById(R.id.post_text_title);
            authorName = (TextView) view.findViewById(R.id.post_text_author_name);
            replyCount = (TextView) view.findViewById(R.id.post_text_reply_count);
            hasImg = (TextView) view.findViewById(R.id.post_text_hasImg);
        }
    }

    static class ProgressHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public ProgressHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        }
    }

    public PostAdapter(List<Post> postList, RecyclerView recyclerView) {

        mPostList = postList;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPositon = linearLayoutManager.findLastVisibleItemPosition();
                int totalItem = linearLayoutManager.getItemCount();
                if (!loadding && totalItem == lastVisibleItemPositon+1) {
                    if (onLoadMoreListener != null) {
                        loadding = true;
                        onLoadMoreListener.onLoadMore();
                    }
                }

            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_NORMOL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_me, parent, false);
            holder = new NormalHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, parent, false);
            holder = new ProgressHolder(view);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalHolder) {
            final Post post = mPostList.get(position);
            ((NormalHolder) holder).title.setText(post.getTitle());
            ((NormalHolder) holder).authorName.setText(post.getAuthor());
            ((NormalHolder) holder).replyCount.setText(post.getReplyCount());
            ((NormalHolder) holder).hasImg.setVisibility(post.isHasImg() == true ? View.VISIBLE : View.INVISIBLE);
            ((NormalHolder) holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(activity, "你点击了：" + post.getUrl(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(v.getContext(), "你点击了" + post.getUrl(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ((ProgressHolder) holder).progressBar.setIndeterminate(true);
        }
    }


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mPostList.get(position) != null ? TYPE_NORMOL : TYPE__FOOTER;
    }
}
