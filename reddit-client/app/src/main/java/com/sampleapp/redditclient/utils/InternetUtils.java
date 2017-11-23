package com.sampleapp.redditclient.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.sampleapp.redditclient.RedditClientApp;

public class InternetUtils {

    private Context appContext;

    public InternetUtils(RedditClientApp app) {
        this.appContext = app.getApplicationContext();
    }

    public boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
