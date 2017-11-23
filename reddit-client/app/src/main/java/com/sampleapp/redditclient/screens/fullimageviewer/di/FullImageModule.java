package com.sampleapp.redditclient.screens.fullimageviewer.di;

import com.sampleapp.redditclient.screens.fullimageviewer.FullImageContract;
import com.sampleapp.redditclient.screens.fullimageviewer.FullImagePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class FullImageModule {

    @Provides
    FullImageContract.FullImagePresenter provideFullImagePresenter() {
        return new FullImagePresenterImpl();
    }

}

