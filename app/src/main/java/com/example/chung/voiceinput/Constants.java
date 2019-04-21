package com.example.chung.voiceinput;

import android.sax.RootElement;

public class Constants {
    public static final String ROOT_URL = "http://awch.myqnapcloud.com/fyp/api/";
    public static final String URL_REGISTER = ROOT_URL + "user/create.php";
    public static final String URL_LOGIN = ROOT_URL + "user/login.php";
    public static final String URL_READ = ROOT_URL + "tutorial/read.php";
    public static final String URL_KEYWORD = ROOT_URL + "keyword/search_by_keyword.php";

    public static final String successKeyword = "User was created";
    public static final String failKeyword = "Unable to create user";
}
