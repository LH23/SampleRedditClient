package com.sampleapp.redditclient.model.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RedditApiService {

    @GET("/top.json")
    Call<PostsJSONResponse> getTopPosts(@Query("limit") int limit,
                                        @Query("after") String after);

}
