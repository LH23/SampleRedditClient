package com.sampleapp.redditclient;

import android.app.Activity;
import android.app.Application;

import com.sampleapp.redditclient.di.ApiModule;
import com.sampleapp.redditclient.di.DaggerRedditClientComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class RedditClientApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerRedditClientComponent.builder().setApiModule(new ApiModule(Constants.API_ENDPOINT)).create(this).inject(this);

    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

}
