package com.sampleapp.redditclient.model;

import com.sampleapp.redditclient.model.api.PostsJSONResponse;
import com.sampleapp.redditclient.model.api.PostsJSONResponse.PostsJsonData.JsonPostData.JsonPost.Preview.Image;
import com.sampleapp.redditclient.model.room.RedditPostEntity;
import com.sampleapp.redditclient.utils.LogDebug;

import java.util.List;

public class RedditPost {

    private String fullname;
    private String title;
    private String author;
    private long createdUtc;
    private String thumbnail;
    private int numComments;
    private String sourceUrl;

    public RedditPost(PostsJSONResponse.PostsJsonData.JsonPostData jsonPostData) {
        PostsJSONResponse.PostsJsonData.JsonPostData.JsonPost jsonPost = jsonPostData.getPost();
        this.fullname = jsonPostData.getKind()+"_"+jsonPost.getId();
        this.title = jsonPost.getTitle();
        this.author = jsonPost.getAuthor();
        this.createdUtc = jsonPost.getCreatedUtc();
        this.numComments = jsonPost.getNumComments();
        if (jsonPost.getThumbnail().startsWith("http")) {
            this.thumbnail = jsonPost.getThumbnail();
        } else {
            this.thumbnail = "";
        }
        if (hasImageExtension(jsonPost.getUrl())) {
            this.sourceUrl = jsonPost.getUrl();
            if (sourceUrl.endsWith(".gifv")) {
                sourceUrl = sourceUrl.substring(0, sourceUrl.length()-1);
            }
        } else if (jsonPost.getPreview() != null) {
            List<Image> images = jsonPost.getPreview().getImages();
            if (!images.isEmpty()) {
                this.sourceUrl = images.get(0).getSource().getUrl();
                LogDebug.d("RP", "RedditPost: "+this.sourceUrl);
            }
        }
    }

    public RedditPost(RedditPostEntity entity) {
        this.fullname = entity.getFullname();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.createdUtc = entity.getCreatedUtc();
        this.numComments = entity.getNumComments();
        this.thumbnail = entity.getThumbnail();
        this.sourceUrl = entity.getSourceUrl();
    }

    private boolean hasImageExtension(String url) {
        return url != null && (url.endsWith("jpg") || url.endsWith("png") || url.endsWith("gif") || url.endsWith("gifv"));
    }

    public String getFullname() {
        return fullname;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public long getCreatedUtc() {
        return createdUtc;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getNumComments() {
            return numComments;
        }

    public String getSourceUrl() {
        return sourceUrl;
    }
}
