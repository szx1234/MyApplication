package com.szx.myapplication.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by songzhixin on 2017/5/24.
 */

public class MyWebView extends WebView{
    private static final String FILE_CSS = "file:///android_asset/style.css";

    private static final String HTML_HEADER = "" +
            "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<link type=\"text/css\" rel=\"stylesheet\" href=\""+ FILE_CSS + "\">\n"+
            "<style>img{display: inline; height: auto; max-width: 100%;}</style>\n"+
            "<meta charset=\"UTF-8\">\n" +
            "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1\">\n"+
            "</head>\n"+
            "<body>\n";

    private static final String HTML_REAR = "" +
            "</body>\n" +
            "</html>";

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        String newData = HTML_HEADER + data + HTML_REAR;
        super.loadData(newData, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        String newData = HTML_HEADER + data + HTML_REAR;
        super.loadDataWithBaseURL(baseUrl, newData, mimeType, encoding, historyUrl);
    }
}
