package com.sampleapp.redditclient.screens.toplist.di;

import com.sampleapp.redditclient.model.RedditPostRepository;
import com.sampleapp.redditclient.screens.toplist.TopListContract;
import com.sampleapp.redditclient.screens.toplist.TopListPresenterImpl;
import com.sampleapp.redditclient.utils.InternetUtils;

import dagger.Module;
import dagger.Provides;

@Module
public class TopListModule {

    @Provides
    TopListContract.TopListPresenter provideTopListPresenter(RedditPostRepository repository, InternetUtils internetUtils) {
        return new TopListPresenterImpl(repository, internetUtils);
    }

}

