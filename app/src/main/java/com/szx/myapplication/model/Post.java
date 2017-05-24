package com.szx.myapplication.model;

/**
 * Created by songzhixin on 2017/5/17.
 */

public class Post {
    private String url;
    private String title;
    private String author;
    private String replyCount;
    private boolean hasImg;
    private int titleColor = 0;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public boolean isHasImg() {
        return hasImg;
    }

    public void setHasImg(boolean hasImg) {
        this.hasImg = hasImg;
    }

    public int isTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }


    public Post(String url, String title, String author, String replyCount, boolean hasImg){
        this.url = url;
        this.title = title;
        this.author = author;
        this.replyCount = replyCount;
        this.hasImg = hasImg;
    }
}
