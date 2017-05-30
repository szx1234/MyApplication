package com.szx.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.szx.myapplication.R;

/**
 * Created by songzhixin on 2017/5/30.
 */

public class MActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.textView)).setText(getIntent().getStringExtra("str"));
    }

    public static void open(Context context, String str){
        Intent intent = new Intent(context, MActivity.class);
        intent.putExtra("str", str);
        context.startActivity(intent);

    }
}
