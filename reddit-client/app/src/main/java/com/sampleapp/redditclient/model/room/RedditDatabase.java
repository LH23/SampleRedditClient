package com.sampleapp.redditclient.model.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {RedditPostEntity.class}, version = 1)
public abstract class RedditDatabase extends RoomDatabase {
    public abstract RedditPostsDao redditPostDao();
}

