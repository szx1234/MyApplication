package com.szx.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.szx.myapplication.R;
import com.szx.myapplication.activity.App;
import com.szx.myapplication.activity.ArticleActivity;
import com.szx.myapplication.model.CollectionOrArticle;
import com.szx.myapplication.util.Util;
import java.util.List;

/**
 * Created by songzhixin on 2017/6/20.
 */

public class CollectionOrArticleAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<CollectionOrArticle> mData;

    static final int TYPE_NORMAL = 0;
    static final int TYPE_FOOTER = 1;

    class NormalHolder extends RecyclerView.ViewHolder {
        TextView title;

        public NormalHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.collection_article_title);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public FooterHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }


    public CollectionOrArticleAdapter(List<CollectionOrArticle> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        if (viewType == TYPE_NORMAL) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_collectionorarticle, parent, false);
            viewHolder = new NormalHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.footer, parent, false);
            viewHolder = new FooterHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FooterHolder) {
            ((FooterHolder) holder).progressBar.setIndeterminate(true);
        } else {
            ((NormalHolder) holder).title.setText(mData.get(position).getTitle());
            ((NormalHolder) holder).title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.setmCurrentTid(Util.analysisTid(mData.get(position).getUrl()));
                    Intent intent =  new Intent(mContext, ArticleActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position) == null ? TYPE_FOOTER : TYPE_NORMAL;
    }
}
