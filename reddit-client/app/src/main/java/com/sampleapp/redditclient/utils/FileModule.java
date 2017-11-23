package com.sampleapp.redditclient.utils;


import android.os.AsyncTask;
import android.util.Log;

import com.sampleapp.redditclient.screens.fullimageviewer.callbacks.ImageSaveCallback;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;

public class FileModule {
    private static final String TAG = "FileModule";

    private OkHttpClient okhttpClient;

    public FileModule(OkHttpClient okhttp) {
        this.okhttpClient = okhttp;
    }

    public void saveImage(String url, File dir, ImageSaveCallback callback) {
        new SaveTask(url, dir, callback, okhttpClient).execute();
    }


    private static class SaveTask extends AsyncTask<Void, Void, File> {
        private String url;
        private File dir;
        private ImageSaveCallback callback;
        private OkHttpClient okhttpClient;

        SaveTask(String url, File dir, ImageSaveCallback callback, OkHttpClient okhttp) {
            this.url = url;
            this.dir = dir;
            this.callback = callback;
            this.okhttpClient = okhttp;
        }

        @Override
        protected File doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = okhttpClient.newCall(request).execute();

                String ext = url.substring(url.lastIndexOf(".")+1);
                if (!(ext.equals("jpg") || ext.equals("png") || ext.equals("gif"))) {
                    ext = "jpg"; // defaults to jpg
                }
                File outputFile = new File(dir, getFilename(ext));
                BufferedSink sink = Okio.buffer(Okio.sink(outputFile));
                ResponseBody body = response.body();
                if (body != null) {
                    sink.writeAll(body.source());
                    sink.close();
                    body.close();
                    return outputFile;
                }

            } catch (IOException e) {
                Log.e(TAG, "saveImage: ERROR");
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {
                callback.onSaveSuccess(file);
            } else {
                callback.onSaveError();
            }
        }

        private String getFilename(String extension) {
            Date cDate = new Date();
            String fDate = new SimpleDateFormat("yyyyMMdd-hhmmss", Locale.US).format(cDate);
            return "Reddit-"+fDate+"."+extension;
        }
    }
}
