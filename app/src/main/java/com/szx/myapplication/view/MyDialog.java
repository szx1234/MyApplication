package com.szx.myapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.szx.myapplication.R;
import com.szx.myapplication.adapter.ForumAdapter;
import com.szx.myapplication.model.BaseForum;
import com.szx.myapplication.model.ForumHeader;
import com.szx.myapplication.model.ForumNormal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by songzhixin on 2017/6/5.
 */

public class MyDialog extends Dialog {

    private Context mContext;
    private List<BaseForum> mData;
    private RecyclerView mRecyclerView;
    private ForumAdapter mForumAdapter;
    private GridLayoutManager mGridLayoutManager;


    public MyDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_forum);

        initRecyclerView();

        initData();

    }

    private void initRecyclerView() {
        mData = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.dialog_recyclerView);
        mForumAdapter = new ForumAdapter(mContext, this, mData);
        mGridLayoutManager = new GridLayoutManager(mContext, 4);

        //SpanSize是一个view占多少列，所以如果是header就要占4列，而普通的就只占一列就好
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mForumAdapter.getItemViewType(position) == ForumAdapter.TYPE_HEADER ? 4 : 1;
            }
        });
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mForumAdapter);

    }

    private void initData() {
        InputStream is = null;
        String s = null;
        try {
            is = mContext.getAssets().open("forums.json");
            int size = is.available();
            byte[] buff = new byte[size];
            is.read(buff);
            s = new String(buff);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "读取失败", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                //获取大的板块
                JSONObject jsonBigObject = (JSONObject) jsonArray.get(i);
                //获取标题
                ForumHeader header = new ForumHeader();
                header.setTitle(jsonBigObject.getString("name"));
                mData.add(header);
                //获取大板块内的小板块
                JSONArray array = jsonBigObject.getJSONArray("forums");
                for (int j = 0; j < array.length(); j++) {
                    JSONObject jsonSmallObject = (JSONObject) array.get(j);
                    ForumNormal forumNormal = new ForumNormal();
                    forumNormal.setName(jsonSmallObject.getString("name"));
                    forumNormal.setFid(jsonSmallObject.getString("fid"));
                    mData.add(forumNormal);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "解析失败", Toast.LENGTH_SHORT).show();
        }
    }

}
