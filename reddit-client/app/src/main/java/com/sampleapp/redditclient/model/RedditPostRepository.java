package com.sampleapp.redditclient.model;

import android.support.annotation.WorkerThread;

import com.sampleapp.redditclient.model.api.PostsJSONResponse;
import com.sampleapp.redditclient.model.api.PostsJSONResponse.PostsJsonData.JsonPostData;
import com.sampleapp.redditclient.model.api.RedditApiService;
import com.sampleapp.redditclient.model.room.RedditDatabase;
import com.sampleapp.redditclient.model.room.RedditPostEntity;
import com.sampleapp.redditclient.utils.PreferencesModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Response;

@Singleton
public class RedditPostRepository {

    private static final String TAG = "RedditPostRepo";

    private static final long CACHE_TIME = 3600000; // cache 1 hour
    private final PreferencesModule preferencesModule;

    private RedditApiService redditApiService;
    private RedditDatabase redditDatabase;
    private List<RedditPost> topPostsList;
    private long lastUpdateTime;

    @Inject
    public RedditPostRepository(RedditApiService redditApiService, RedditDatabase redditDatabase, PreferencesModule preferencesModule) {
        this.redditApiService = redditApiService;
        this.topPostsList = new ArrayList<>(10);
        this.redditDatabase = redditDatabase;
        this.preferencesModule = preferencesModule;
        this.lastUpdateTime = preferencesModule.getLastUpdateTime();
    }

    public List<RedditPost> getTopPosts() {
        return topPostsList;
    }

    private boolean needsToRefresh() {
        return (System.currentTimeMillis() > lastUpdateTime + CACHE_TIME);
    }

    @WorkerThread
    public List<RedditPost> getMorePostsSync(int pageSize, boolean forcedWebRefresh) {
        if (topPostsList == null) {
            throw new IllegalArgumentException("post list is null");
        }

        if (needsToRefresh() || forcedWebRefresh) {
            redditDatabase.redditPostDao().removeAll();
        } else if (topPostsList.isEmpty()){
            topPostsList = convertToRedditPosts(redditDatabase.redditPostDao().getAll());
        }
        if (topPostsList.size() == 50) {
            return topPostsList;
        }

        String lastItemId = "";
        if (!topPostsList.isEmpty()) {
            lastItemId = topPostsList.get(topPostsList.size()-1).getFullname();
        }
        Call<PostsJSONResponse> listOfPostsCall = redditApiService.getTopPosts(pageSize, lastItemId);
        try {
            Response<PostsJSONResponse> response = listOfPostsCall.execute();
            List<JsonPostData> jsonPosts = response.body().getData().getPostsData();

            for (JsonPostData jsonPost: jsonPosts) {
                topPostsList.add(new RedditPost(jsonPost));
            }
            redditDatabase.redditPostDao().removeAll();
            redditDatabase.redditPostDao().addAll(convertToRedditPostEntities(topPostsList));

            lastUpdateTime = System.currentTimeMillis();
            preferencesModule.saveLastUpdateTime(lastUpdateTime);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return topPostsList;
    }

    private List<RedditPostEntity> convertToRedditPostEntities(List<RedditPost> posts) {
        List<RedditPostEntity> postEntities = new ArrayList<>(posts.size());
        for (RedditPost redditPost: posts) {
            postEntities.add(new RedditPostEntity(redditPost));
        }
        return postEntities;
    }

    private List<RedditPost> convertToRedditPosts(List<RedditPostEntity> postEntities) {
        List<RedditPost> posts = new ArrayList<>(postEntities.size());
        for (RedditPostEntity entity: postEntities) {
            posts.add(new RedditPost(entity));
        }
        return posts;
    }

    public void clearPostsList() {
        topPostsList.clear();
    }
}