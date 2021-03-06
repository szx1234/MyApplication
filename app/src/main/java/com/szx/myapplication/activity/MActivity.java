package com.szx.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.szx.myapplication.R;

/**
 * Created by songzhixin on 2017/6/20.
 */

public class MActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mactivity);
        ((TextView)findViewById(R.id.textView)).setText(getIntent().getStringExtra("content"));
    }

    public static void open(Activity activity, String str) {
        Intent intent = new Intent(activity, MActivity.class);
        intent.putExtra("content", str);
        activity.startActivity(intent);
    }

}
