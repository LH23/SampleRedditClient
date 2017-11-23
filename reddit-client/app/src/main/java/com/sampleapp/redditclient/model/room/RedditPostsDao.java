package com.sampleapp.redditclient.model.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RedditPostsDao {

    @Query("SELECT * FROM redditpostentity")
    List<RedditPostEntity> getAll();

    @Query("DELETE FROM redditpostentity")
    void removeAll();

    @Insert
    void addAll(List<RedditPostEntity> entities);
}

