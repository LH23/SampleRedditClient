package com.sampleapp.redditclient.di;

import android.arch.persistence.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sampleapp.redditclient.Constants;
import com.sampleapp.redditclient.RedditClientApp;
import com.sampleapp.redditclient.model.api.RedditApiService;
import com.sampleapp.redditclient.model.room.RedditDatabase;
import com.sampleapp.redditclient.utils.FileModule;
import com.sampleapp.redditclient.utils.InternetUtils;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.support.AndroidSupportInjectionModule;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = AndroidSupportInjectionModule.class)
public class ApiModule {

    private String baseUrl;

    public ApiModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    protected FileModule provideFileModule(OkHttpClient okHttpClient) {
        return new FileModule(okHttpClient);
    }

    @Provides
    @Singleton
    protected InternetUtils provideInternetUtils(RedditClientApp app) {
        return new InternetUtils(app);
    }

    @Provides
    @Singleton
    protected RequestManager provideGlide(RedditClientApp app) {
        return Glide.with(app);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        return builder.create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json; charset=utf-8").build();
                return chain.proceed(request);
            }
        });
        if (Constants.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging).build();
        }
        return builder.build();
    }

    @Provides
    @Singleton
    public RedditDatabase provideRedditDatabase(RedditClientApp app) {
        return Room.databaseBuilder(app.getApplicationContext(),
                RedditDatabase.class, "reddit-database").build();
    }

    @Provides
    @Singleton
    public RedditApiService provideApiService(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient).build()
            .create(RedditApiService.class);
    }

    @Provides
    @Named("base_url")
    public String provideBaseUrl() {
        return baseUrl;
    }
}

