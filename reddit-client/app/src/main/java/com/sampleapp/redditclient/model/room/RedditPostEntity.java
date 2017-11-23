package com.sampleapp.redditclient.model.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.sampleapp.redditclient.model.RedditPost;

@Entity
public class RedditPostEntity {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "fullname")
    private String fullname;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "author")
    private String author;
    @ColumnInfo(name = "created_utc")
    private long createdUtc;
    @ColumnInfo(name = "thumbnail")
    private String thumbnail;
    @ColumnInfo(name = "numComments")
    private int numComments;
    @ColumnInfo(name = "sourceUrl")
    private String sourceUrl;

    public RedditPostEntity() {
        super();
    }

    public RedditPostEntity(RedditPost redditPost) {
        this.fullname = redditPost.getFullname();
        this.title = redditPost.getTitle();
        this.author = redditPost.getAuthor();
        this.createdUtc = redditPost.getCreatedUtc();
        this.thumbnail = redditPost.getThumbnail();
        this.numComments = redditPost.getNumComments();
        this.sourceUrl = redditPost.getSourceUrl();
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getCreatedUtc() {
        return createdUtc;
    }

    public void setCreatedUtc(long createdUtc) {
        this.createdUtc = createdUtc;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
