package com.szx.myapplication.model;

import java.io.Serializable;

/**
 * Created by songzhixin on 2017/5/24.
 */

public class Article implements Serializable{
    //图片url
    private String imgUrl;
    //用户名称
    private String userName;
    //回复时间
    private String replyTime;
    //按钮提交url
    private String btnUrl;
    //回复内容
    private String content;
    //用户个人信息
    private String userDetailUrl;

    public String getUserDetailUrl() {
        return userDetailUrl;
    }

    public void setUserDetailUrl(String userDetailUrl) {
        this.userDetailUrl = userDetailUrl;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getBtnUrl() {
        return btnUrl;
    }

    public void setBtnUrl(String btnUrl) {
        this.btnUrl = btnUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
