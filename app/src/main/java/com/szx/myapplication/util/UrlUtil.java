package com.szx.myapplication.util;

import com.szx.myapplication.activity.App;

/**
 * Created by songzhixin on 2017/5/27.
 */

public class UrlUtil {
    public static final String LOGIN_URL = "member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LRaJa&mobile=2";
    public static final String BASE_URL = "http://bbs.rs.xidian.me/";
    public static final String SEARCH_URL = "search.php?mod=forum&mobile=2";

    //http://bbs.rs.xidian.me/member.php?mod=logging&action=login&mobile=2
    //http://rs.xidian.edu.cn/
    public static String getLoginUrl() {
        return LOGIN_URL;
    }

    public static String getAbsUrl(String url) {
        if (url.contains("http://"))
            return url;
        else
            return BASE_URL + url;
    }

    public static String getUserDetailUrl(String uid) {
        return "home.php?mod=space&uid=" + uid + "&do=profile&mobile=2";
    }

    public static String getReplyUrl() {
        return "forum.php?mod=post&action=reply&fid=" + App.getmCurrentFid() + "&tid=" + App.getmCurrentTid() + "&extra=&replysubmit=yes&mobile=2";
    }

    public static String getSearchUrl() {
        return SEARCH_URL;
    }

    public static String getSearchDataUrl(String searchId, int currentPage) {
        return "search.php?mod=forum&searchid=" + searchId + "&orderby=lastpost&ascdesc=desc&searchsubmit=yes&page=" + currentPage +"&mobile=2";
    }


}
