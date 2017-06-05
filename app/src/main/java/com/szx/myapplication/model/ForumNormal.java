package com.szx.myapplication.model;

/**
 * Created by songzhixin on 2017/6/5.
 * 板块
 */

public class ForumNormal extends BaseForum{
    private String name;
    private String fid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }
}
