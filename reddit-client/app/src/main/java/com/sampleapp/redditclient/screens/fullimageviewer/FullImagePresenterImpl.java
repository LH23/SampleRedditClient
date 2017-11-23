package com.sampleapp.redditclient.screens.fullimageviewer;

import com.sampleapp.redditclient.screens.fullimageviewer.callbacks.ImageSaveCallback;

import java.io.File;

public class FullImagePresenterImpl implements FullImageContract.FullImagePresenter, ImageSaveCallback {

    private FullImageContract.FullImageView fullImageView;
    private String sourceImage;
    private boolean saved;

    @Override
    public void setView(FullImageContract.FullImageView view) {
        fullImageView = view;
    }

    @Override
    public void destroyView() {
        fullImageView = null;
    }

    @Override
    public boolean isViewAttached() {
        return fullImageView != null;
    }

    @Override
    public void setSourceImage(String sourceImage) {
        saved = false;
        this.sourceImage = sourceImage;
        if (isViewAttached()) {
            fullImageView.showFullImage(sourceImage);
        }
    }

    @Override
    public void saveSelectedImage() {
        if (isViewAttached()) {
            if (!fullImageView.hasWritePermission()) {
                fullImageView.requestWritePermission();
                return;
            }
            if (saved) {
                fullImageView.showAlreadySaved();
            } else {
                fullImageView.saveFile(sourceImage, this);
            }
        }
    }

    @Override
    public boolean getSavedStatus() {
        return saved;
    }

    @Override
    public void setSavedStatus(boolean saved) {
        this.saved = saved;
    }

    // save callback
    @Override
    public void onSaveSuccess(File savedFile) {
        saved = true;
        if (isViewAttached()) {
            fullImageView.scanFile(savedFile);
            fullImageView.showSavedCorrectly();
        }
    }

    @Override
    public void onSaveError() {
        if (isViewAttached()) {
            fullImageView.showErrorSavingFile();
        }
    }
}
