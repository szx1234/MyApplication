package com.szx.myapplication.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.szx.myapplication.R;
import com.szx.myapplication.adapter.PostAdapter;
import com.szx.myapplication.model.Post;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by songzhixin on 2017/6/20.
 */

public class ScanHistoryActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Post> mData;
    private PostAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanhistory);

        initToolbar();
        initRecyclerViewAndData();
    }

    private void initRecyclerViewAndData() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mData = DataSupport.findAll(Post.class);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new PostAdapter(mData, mRecyclerView, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.back);
        setTitle("浏览历史");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.delete:
                final Dialog dialog = new Dialog(this);
                dialog.show();
                dialog.setContentView(R.layout.dialog_remove_history);
                dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mData.clear();
                        mAdapter.notifyDataSetChanged();
                        DataSupport.deleteAll(Post.class);
                        dialog.dismiss();
                    }
                });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scanhistory, menu);
        return true;
    }
}
