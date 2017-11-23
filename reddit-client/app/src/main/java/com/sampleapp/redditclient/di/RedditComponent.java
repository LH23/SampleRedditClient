package com.sampleapp.redditclient.di;


import com.sampleapp.redditclient.RedditClientApp;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = { AppModule.class, ApiModule.class})
interface RedditClientComponent extends AndroidInjector<RedditClientApp> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<RedditClientApp> {

        public abstract Builder setApiModule(ApiModule apiModule);
    }
}
