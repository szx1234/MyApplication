package com.szx.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szx.myapplication.R;
import com.szx.myapplication.activity.App;
import com.szx.myapplication.activity.MainActivity;
import com.szx.myapplication.model.BaseForum;
import com.szx.myapplication.model.ForumHeader;
import com.szx.myapplication.model.ForumNormal;
import com.szx.myapplication.util.Util;
import com.szx.myapplication.view.MyDialog;

import java.util.List;

/**
 * Created by songzhixin on 2017/6/5.
 */

public class ForumAdapter extends RecyclerView.Adapter {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HEADER = 1;

    private List<BaseForum> mData;
    private MyDialog mMyDialog;
    private Context mContext;

    public ForumAdapter(Context context, MyDialog myDialog, List<BaseForum> data) {
        this.mMyDialog = myDialog;
        this.mData = data;
        this.mContext = context;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView title;

        public HeaderHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.forum_title);
        }
    }

    class NormalHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;

        public NormalHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.forum_name);
            icon = (ImageView) itemView.findViewById(R.id.forum_img);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(mMyDialog.getContext()).inflate(R.layout.item_forum_header, null);
            viewHolder = new HeaderHolder(view);
        } else {
            View view = LayoutInflater.from(mMyDialog.getContext()).inflate(R.layout.item_forum_normal, null);
            viewHolder = new NormalHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BaseForum forum = mData.get(position);
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).title.setText(((ForumHeader) forum).getTitle());
        } else if (holder instanceof NormalHolder) {
            ((NormalHolder) holder).name.setText(((ForumNormal) forum).getName());

            Drawable drawable = Util.getForumIcon(((ForumNormal) forum).getFid());
            if (drawable != null) {
                ((NormalHolder) holder).icon.setImageDrawable(drawable);
            } else {
                ((NormalHolder) holder).icon.setImageResource(R.drawable.image_place);
            }
            ((NormalHolder) holder).icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMyDialog.dismiss();
                    App.setmCurrentFid(((ForumNormal) forum).getFid());
                    MainActivity.setFid(((ForumNormal) forum).getFid());
                    ((MainActivity)mContext).refreshCode();

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
        if (mData.get(position) instanceof ForumHeader)
            return TYPE_HEADER;
        else
            return TYPE_NORMAL;
    }
}
