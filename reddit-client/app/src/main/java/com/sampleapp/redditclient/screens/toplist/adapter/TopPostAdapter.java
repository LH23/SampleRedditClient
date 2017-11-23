package com.sampleapp.redditclient.screens.toplist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sampleapp.redditclient.R;
import com.sampleapp.redditclient.screens.toplist.callbacks.PostActionCallback;
import com.sampleapp.redditclient.utils.GlideHelper;

import java.util.List;

public class TopPostAdapter extends RecyclerView.Adapter {

    private final GlideHelper glideHelper;
    private List<PostHolder> postHolders;
    private PostActionCallback callback;

    public TopPostAdapter(List<PostHolder> postHolders, GlideHelper glideHelper, PostActionCallback callback) {
        this.postHolders = postHolders;
        this.glideHelper = glideHelper;
        this.callback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view, parent, false);
        return new PostViewHolder(mView, parent.getContext().getString(R.string.created_at),
                                    parent.getContext().getString(R.string.num_comments));
    }

    @Override
    public int getItemCount() {
        return postHolders.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        PostHolder postHolder = postHolders.get(position);
        PostViewHolder postViewHolder = (PostViewHolder) holder;
        postViewHolder.set(postHolder, glideHelper, callback);
    }

    public void setPosts(List<PostHolder> posts) {
        this.postHolders = posts;
        notifyDataSetChanged();
    }
}
