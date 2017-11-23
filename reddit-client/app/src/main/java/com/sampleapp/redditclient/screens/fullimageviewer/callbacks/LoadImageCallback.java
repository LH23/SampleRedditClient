package com.sampleapp.redditclient.screens.fullimageviewer.callbacks;

public interface LoadImageCallback {

    void onLoadImageError(String imageUrl);

    void onLoadImageFinished();
}
