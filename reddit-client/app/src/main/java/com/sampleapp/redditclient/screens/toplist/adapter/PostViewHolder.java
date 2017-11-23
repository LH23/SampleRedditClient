package com.sampleapp.redditclient.screens.toplist.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sampleapp.redditclient.R;
import com.sampleapp.redditclient.screens.toplist.callbacks.PostActionCallback;
import com.sampleapp.redditclient.utils.GlideHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.constraint.ConstraintSet.END;
import static android.support.constraint.ConstraintSet.START;

public class PostViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.post_item_parent)
    ConstraintLayout parent;
    @BindView(R.id.post_item_author)
    TextView itemAuthorView;
    @BindView(R.id.post_item_title)
    TextView itemTitleView;
    @BindView(R.id.post_item_comments)
    TextView itemCommentsView;
    @BindView(R.id.post_item_time)
    TextView itemTimeView;
    @BindView(R.id.post_item_thumb)
    ImageView itemThumbView;

    private final String createdText;
    private final String numCommentsText;

    public PostViewHolder(View itemView, String createdText, String numCommentsText) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.createdText = createdText;
        this.numCommentsText = numCommentsText;
    }

    public void set(final PostHolder styleHolder, final GlideHelper glideHelper, final PostActionCallback callback) {
        itemAuthorView.setText(styleHolder.getAuthor());
        itemTitleView.setText(styleHolder.getTitle());
        itemTimeView.setText(String.format(createdText, styleHolder.getElapsedHours()));
        itemCommentsView.setText(String.format(numCommentsText, styleHolder.getNumComments()));

        ConstraintSet set = new ConstraintSet();
        set.clone(parent);
        if (styleHolder.getThumbnailUrl() != null) {
            glideHelper.loadThumb(itemThumbView, styleHolder.getThumbnailUrl());
            set.connect(R.id.post_item_thumb, START, R.id.barrier, END, 8);
        } else {
            set.clear(R.id.post_item_thumb, START);
        }
        set.applyTo(parent);

        if (styleHolder.getSourceUrl() != null) {
            itemThumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onThumbClick(styleHolder.getSourceUrl());
                }
            });
        }
    }

}
