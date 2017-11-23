package com.sampleapp.redditclient.di;


import android.app.Activity;
import android.app.Application;

import com.sampleapp.redditclient.RedditClientApp;
import com.sampleapp.redditclient.screens.fullimageviewer.FullImageActivity;
import com.sampleapp.redditclient.screens.fullimageviewer.di.FullImageSubcomponent;
import com.sampleapp.redditclient.screens.toplist.TopListActivity;
import com.sampleapp.redditclient.screens.toplist.di.TopListSubcomponent;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.IntoMap;

@Module(includes = AndroidSupportInjectionModule.class,
    subcomponents = {
        TopListSubcomponent.class,
        FullImageSubcomponent.class,
    })
public abstract class AppModule {

    @Binds
    @Singleton // it's always a Singleton, just a convention
    abstract Application application(RedditClientApp app);

    @Binds
    @IntoMap
    @ActivityKey(TopListActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    topListInjectorFactory(TopListSubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(FullImageActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    fullImageInjectorFactory(FullImageSubcomponent.Builder builder);


}