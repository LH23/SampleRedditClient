package com.sampleapp.redditclient.utils;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sampleapp.redditclient.RedditClientApp;
import com.sampleapp.redditclient.screens.fullimageviewer.callbacks.LoadImageCallback;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GlideHelper {

    private static final String TAG = "GlideHelper";

    private static final int BIG_SIZE = 2000;

    private final RequestManager glide;

    @Inject
    public GlideHelper(RedditClientApp app) {
        glide = Glide.with(app.getApplicationContext());
    }

    public void loadThumb(final ImageView imageView, final String thumbnailUrl) {
        LogDebug.d(TAG, "loadThumb: "+imageView+" "+thumbnailUrl);
        glide.load(thumbnailUrl).into(imageView);
    }

    public void loadFullImage(final ImageView fullImageView, final String imageUrl, final LoadImageCallback loadCallback) {
        LogDebug.d(TAG, "loadFullImage: "+fullImageView+" "+imageUrl);
        glide.load(imageUrl).into(new ImageViewTarget<Drawable>(fullImageView) {
            @Override
            protected void setResource(@Nullable Drawable resource) {
            }

            @Override
            public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                super.onResourceReady(resource, transition);
                loadCallback.onLoadImageFinished();
                if (resource instanceof BitmapDrawable && isVeryBig(resource)) {
                    Bitmap b = ((BitmapDrawable)resource).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    glide.asBitmap().load(stream.toByteArray()).into(fullImageView);
                } else {
                    fullImageView.setImageDrawable(resource);
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                loadCallback.onLoadImageError(imageUrl);
            }
        });


    }

    private boolean isVeryBig(Drawable resource) {
        return resource.getMinimumHeight() > BIG_SIZE || resource.getMinimumWidth() > BIG_SIZE;
    }
}
