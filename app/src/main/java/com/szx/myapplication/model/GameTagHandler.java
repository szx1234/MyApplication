package com.szx.myapplication.model;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.xml.sax.XMLReader;

/**
 * Created by songzhixin on 2017/5/20.
 */

public class GameTagHandler implements Html.TagHandler {
    private int startIndex;
    private int endIndex;
    private Context context;
    public GameTagHandler(Context context){
        this.context = context;
    }
    String TAG = "out";
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        Log.w("out", "handleTag: " + "   tag=" + tag );
        Log.w(TAG, "handleTag: " + "   output=" + output);
        Log.w(TAG, "handleTag: " + "   xmlReader=" + xmlReader.getContentHandler() );
        Log.w(TAG, "            ");
        if(tag.toLowerCase().equals("game")) {
            if(opening)
                handleGameStart(tag, output, xmlReader);
            else
                handleGameEnd(tag, output, xmlReader);
        } else if(tag.toLowerCase().equals("strong_plus")){
            if(opening)
                handleStrongStart(tag, output, xmlReader);
            else
                handleStrongEnd(tag, output, xmlReader);
        }
    }

    private void handleStrongStart(String tag, Editable output, XMLReader xmlReader) {
        startIndex = output.length();
        Log.w("www", "handleStrongStart: " + startIndex+ "   " + output );
    }

    private void handleStrongEnd(String tag, Editable output, XMLReader xmlReader) {
        endIndex = output.length();
        Log.w("www", "handleStrongEnd: " + endIndex + "  " + output );
        output.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        output.setSpan(new StrongClickSpan(tag), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void handleGameStart(String tag, Editable output, XMLReader xmlReader) {
        Log.w(TAG, "handleGameStart: " + "   tag=" + tag );
        Log.w(TAG, "handleGameStart: " + "   output=" + output );
        startIndex = output.length();
    }
    private void handleGameEnd(String tag, Editable output, XMLReader xmlReader) {
        endIndex = output.length();
        Log.w(TAG, "handleGameEnd: " + "   tag=" + tag );
        Log.w(TAG, "handleGameEnd: " + "   output=" + output );
        output.setSpan(new GameSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        output.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        output.setSpan(new RelativeSizeSpan(2f), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        output.setSpan(new ForegroundColorSpan(Color.BLACK), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    class StrongClickSpan extends ClickableSpan{
        String content;
        public StrongClickSpan(String content){
            this.content = content;
        }
        @Override
        public void onClick(View widget) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
    }

    class GameSpan extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            Log.w("www", "onClick: " + "离开家了；asked；饭卡上；的六块腹肌啊老师；控件；；离开家； ");
            Toast.makeText(context, "heihei", Toast.LENGTH_SHORT).show();
        }
    }

}
