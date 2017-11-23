package com.sampleapp.redditclient.screens.toplist.adapter;

import com.sampleapp.redditclient.model.RedditPost;

public class PostHolder {

    private final String id;
    private final String title;
    private final String author;
    private final long createdTime;
    private final int numComments;
    private final String thumbnailUrl;
    private final String sourceUrl;

    public PostHolder(RedditPost post) {
        this.id = post.getFullname();
        this.title = post.getTitle();
        this.author = post.getAuthor();
        this.createdTime = post.getCreatedUtc();
        this.numComments = post.getNumComments();
        this.thumbnailUrl = post.getThumbnail();
        this.sourceUrl = post.getSourceUrl();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public int getNumComments() {
        return numComments;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public long getElapsedHours() {
        return (System.currentTimeMillis()/1000 - createdTime)/3600;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }
}
