package com.sampleapp.redditclient.utils;

import android.util.Log;

import com.sampleapp.redditclient.Constants;

public class LogDebug {

    public static void d(String tag, String msg) {
        if (Constants.DEBUG) {
            Log. d(tag, msg); // small hack: the space makes the replace all Log.d avoid changing this one
        }
    }
}
