package com.sampleapp.redditclient.model.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostsJSONResponse {

    @Expose
    @SerializedName("data")
    private final PostsJsonData data;

    public PostsJSONResponse(PostsJsonData data) {
        this.data = data;
    }

    public PostsJsonData getData() {
        return data;
    }

    public static class PostsJsonData {

        @Expose
        @SerializedName("children")
        private final List<JsonPostData> postsData;

        public PostsJsonData(List<JsonPostData> posts) {
            this.postsData = posts;
        }

        public List<JsonPostData> getPostsData() {
            return postsData;
        }

        public static class JsonPostData {
            @Expose
            @SerializedName("kind")
            private final String kind;
            @Expose
            @SerializedName("data")
            private final JsonPost post;

            public JsonPostData(String kind, JsonPost post) {
                this.kind = kind;
                this.post = post;
            }

            public JsonPost getPost() {
                return post;
            }

            public String getKind() {
                return kind;
            }

            public static class JsonPost {
                @Expose
                @SerializedName("id")
                private final String id;
                @Expose
                @SerializedName("title")
                private final String title;
                @Expose
                @SerializedName("author")
                private final String author;
                @Expose
                @SerializedName("created_utc")
                private final long createdUtc;
                @Expose
                @SerializedName("thumbnail")
                private final String thumbnail;
                @Expose
                @SerializedName("num_comments")
                private final int numComments;
                @Expose
                @SerializedName("url")
                private final String url;
                @Expose
                @SerializedName("preview")
                private final Preview preview;

                public JsonPost(String id, String title, String author, long createdUtc, String thumbnail, int numComments, String url, Preview preview) {
                    this.id = id;
                    this.title = title;
                    this.author = author;
                    this.createdUtc = createdUtc;
                    this.thumbnail = thumbnail;
                    this.numComments = numComments;
                    this.url = url;
                    this.preview = preview;
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

                public Preview getPreview() {
                    return preview;
                }

                public String getId() {
                    return id;
                }

                public String getUrl() {
                    return url;
                }

                public static class Preview {
                    @Expose
                    @SerializedName("images")
                    private List<Image> images;

                    public Preview(List<Image> images) {
                        this.images = images;
                    }

                    public List<Image> getImages() {
                        return images;
                    }

                    public static class Image {
                        @Expose
                        @SerializedName("source")
                        private Source source;

                        public Image(Source source) {
                            this.source = source;
                        }

                        public Source getSource() {
                            return source;
                        }

                        public static class Source {
                            @Expose
                            @SerializedName("url")
                            private String url;

                            public Source(String url) {
                                this.url = url;
                            }

                            public String getUrl() {
                                return url;
                            }
                        }

                    }
                }
            }
        }
    }
}
