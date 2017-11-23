package com.sampleapp.redditclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sampleapp.redditclient.R;
import com.sampleapp.redditclient.RedditClientApp;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesModule {

    private static final String LAST_UPDATE_TIME_PREF = "lastUpdateTimePref";
    private SharedPreferences preferences;

    @Inject
    public PreferencesModule(RedditClientApp app) {
        preferences = app.getSharedPreferences(app.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void saveLastUpdateTime(long lastUpdateTime) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(LAST_UPDATE_TIME_PREF, lastUpdateTime);
        editor.apply();
    }

    public long getLastUpdateTime() {
        return preferences.getLong(LAST_UPDATE_TIME_PREF, 0L);
    }
}
