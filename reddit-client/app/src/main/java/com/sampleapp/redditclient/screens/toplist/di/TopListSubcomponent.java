package com.sampleapp.redditclient.screens.toplist.di;

import com.sampleapp.redditclient.di.PerActivity;
import com.sampleapp.redditclient.screens.toplist.TopListActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerActivity
@Subcomponent(modules = TopListModule.class)
public interface TopListSubcomponent extends AndroidInjector<TopListActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<TopListActivity> {
    }
}
