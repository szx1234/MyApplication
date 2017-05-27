package com.szx.myapplication;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by songzhixin on 2017/5/27.
 */

public class Test {
    private static String str = "home.php?mod=space&uid=297362&do=profile&mycenter=1&mobile=2";
    public static void main(String args[]) {
        Pattern pattern = Pattern.compile("^(uid)\\d+");
        Matcher matcher = pattern.matcher(str);
//        for(int i = 0; i < matcher.groupCount(); i++){
            System.out.println(matcher.toMatchResult());
//        }
        System.out.print("heiehi");
    }
}
