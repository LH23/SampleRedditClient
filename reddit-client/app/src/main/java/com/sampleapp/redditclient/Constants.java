package com.sampleapp.redditclient;

public class Constants {
    public static final String API_ENDPOINT = "http://www.reddit.com/";

    private static final boolean DEBUG_FLAG = true; // set as false to hide logs and debug prints
    public static final boolean DEBUG = DEBUG_FLAG && !BuildConfig.BUILD_TYPE.equals("release");

}
