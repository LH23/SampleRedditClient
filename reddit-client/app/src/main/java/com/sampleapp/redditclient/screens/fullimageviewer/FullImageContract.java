package com.sampleapp.redditclient.screens.fullimageviewer;

import com.sampleapp.redditclient.BasePresenter;
import com.sampleapp.redditclient.screens.fullimageviewer.callbacks.ImageSaveCallback;

import java.io.File;

public interface FullImageContract {

    interface FullImageView {

        void showFullImage(String imageUrl);

        File getSaveDirectory();

        void saveFile(String sourceImage, ImageSaveCallback callback);

        void showErrorSavingFile();

        void showErrorOnImage();

        void showSavedCorrectly();

        void showAlreadySaved();

        void showErrorNoPermission();

        boolean hasWritePermission();

        void requestWritePermission();

        void scanFile(File savedFile);
    }

    interface FullImagePresenter extends BasePresenter<FullImageView> {

        void setSourceImage(String sourceImage);

        void saveSelectedImage();

        boolean getSavedStatus();

        void setSavedStatus(boolean saved);
    }
}
