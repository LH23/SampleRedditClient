package com.sampleapp.redditclient.screens.fullimageviewer.callbacks;

import java.io.File;

public interface ImageSaveCallback {

    void onSaveSuccess(File savedFile);

    void onSaveError();

}
