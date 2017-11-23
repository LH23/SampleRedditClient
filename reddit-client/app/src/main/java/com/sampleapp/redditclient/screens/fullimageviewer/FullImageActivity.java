package com.sampleapp.redditclient.screens.fullimageviewer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sampleapp.redditclient.BaseActivity;
import com.sampleapp.redditclient.R;
import com.sampleapp.redditclient.screens.fullimageviewer.callbacks.ImageSaveCallback;
import com.sampleapp.redditclient.screens.fullimageviewer.callbacks.LoadImageCallback;
import com.sampleapp.redditclient.utils.GlideHelper;
import com.sampleapp.redditclient.utils.FileModule;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FullImageActivity extends BaseActivity implements FullImageContract.FullImageView, LoadImageCallback {

    private static final String TAG = "FullImageActivity";

    private static final String SAVED_STATE = "saved_state";
    private static final String DIRNAME = "Reddit-Client";
    private static final int REQUEST_WRITE_PERMISSION = 1;
    public static final String SOURCE_IMAGE_INTENT_EXTRA = "source_image";

    @BindView(R.id.full_image_progress)
    ProgressBar progress;
    @BindView(R.id.full_image_view)
    ImageView fullImageView;
    @BindView(R.id.full_image_save_fab)
    FloatingActionButton saveFAB;
    @BindView(R.id.full_image_coordinator)
    View coordinatorView;

    @Inject
    FileModule fileModule;
    @Inject
    GlideHelper glideHelper;
    @Inject
    FullImageContract.FullImagePresenter fullImagePresenter;

    private ProgressDialog progressDialog;

    // activity lifecycle methods
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        ButterKnife.bind(this);
        fullImagePresenter.setView(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || !bundle.containsKey(SOURCE_IMAGE_INTENT_EXTRA)) {
            throw new IllegalArgumentException("Source image not bundled");
        }

        String sourceImage = bundle.getString(SOURCE_IMAGE_INTENT_EXTRA);

        fullImagePresenter.setSourceImage(sourceImage);
        if (savedInstanceState != null) {
            fullImagePresenter.setSavedStatus(savedInstanceState.getBoolean(SAVED_STATE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SAVED_STATE, fullImagePresenter.getSavedStatus());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fullImagePresenter.destroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fullImagePresenter.saveSelectedImage();
                } else {
                    showErrorNoPermission();
                }
            }
        }
    }

    // binded actions
    @OnClick(R.id.full_image_save_fab)
    public void onSave() {
        fullImagePresenter.saveSelectedImage();
    }

    // FullImageContract.FullImageView methods
    @Override
    public void showFullImage(String imageUrl) {
        glideHelper.loadFullImage(fullImageView, imageUrl, this);
    }

    @Override
    public File getSaveDirectory() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), DIRNAME);
        if (dir.isDirectory() || dir.mkdirs()) {
            return dir;
        } else {
            throw new IllegalStateException("Error obtaining dir");
        }
    }

    @Override
    public void saveFile(String sourceImage, ImageSaveCallback callback) {
        fileModule.saveImage(sourceImage, getSaveDirectory(), callback);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.saving));
        progressDialog.show();
    }

    @Override
    public void showErrorOnImage() {
        Snackbar.make(coordinatorView, R.string.error_loading_image, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorSavingFile() {
        Snackbar.make(coordinatorView, R.string.error_saving_image, Snackbar.LENGTH_LONG).show();
        progressDialog.dismiss();

    }
    @Override
    public void showSavedCorrectly() {
        Snackbar.make(coordinatorView, R.string.info_saved_correctly, Snackbar.LENGTH_LONG).show();
        progressDialog.dismiss();
    }

    @Override
    public void showAlreadySaved() {
        Snackbar.make(coordinatorView, R.string.info_already_saved, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorNoPermission() {
        Snackbar.make(coordinatorView, R.string.error_dont_have_permission, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean hasWritePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestWritePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_PERMISSION);
    }

    @Override
    public void scanFile(File savedFile) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(savedFile));
        sendBroadcast(intent);
    }

    // LoadImageCallback
    @Override
    public void onLoadImageError(String imageUrl) {
        progress.setVisibility(View.GONE);
        showErrorOnImage();
    }

    @Override
    public void onLoadImageFinished() {
        progress.setVisibility(View.GONE);
        saveFAB.show();
    }

}
