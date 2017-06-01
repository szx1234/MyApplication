package com.szx.myapplication.util;

/**
 * Created by songzhixin on 2017/5/27.
 */

public class UrlUtil {
    public static final String LOGIN_URL = "member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LRaJa&mobile=2";
    public static final String BASE_URL = "http://rs.xidian.edu.cn/";

    //http://rs.xidian.edu.cn/
    public static String getLoginUrl(){
        return BASE_URL+LOGIN_URL;
    }

    public static String getAbsUrl(String url){
        if(url.contains("http://"))
            return url;
        else
            return BASE_URL + url;
    }

    public static String getUserDetailUrl(String uid){
        return BASE_URL + "home.php?mod=space&uid=" + uid + "&do=profile&mobile=2";
    }
}
