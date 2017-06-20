package com.szx.myapplication;


import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by songzhixin on 2017/5/27.
 */

public class Test extends View{

    public Test(Context context) {
        super(context);
    }

    public static void  shit(){

    }
    public void f(){
        A a = new A();
    }


    class A {
        public void f(){
            System.out.println("我是shit");
            Test.this.f();
        }
    }
}