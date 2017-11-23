package com.sampleapp.redditclient.screens.fullimageviewer.di;

import com.sampleapp.redditclient.di.PerActivity;
import com.sampleapp.redditclient.screens.fullimageviewer.FullImageActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@PerActivity
@Subcomponent(modules = FullImageModule.class)
public interface FullImageSubcomponent extends AndroidInjector<FullImageActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<FullImageActivity> {
    }
}
