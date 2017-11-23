package com.sampleapp.redditclient.screens.toplist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sampleapp.redditclient.BaseActivity;
import com.sampleapp.redditclient.R;
import com.sampleapp.redditclient.screens.fullimageviewer.FullImageActivity;
import com.sampleapp.redditclient.screens.toplist.adapter.PostHolder;
import com.sampleapp.redditclient.screens.toplist.adapter.TopPostAdapter;
import com.sampleapp.redditclient.screens.toplist.callbacks.PostActionCallback;
import com.sampleapp.redditclient.utils.GlideHelper;
import com.sampleapp.redditclient.utils.LogDebug;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopListActivity extends BaseActivity
        implements TopListContract.TopListView, PostActionCallback,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TopListActivity";

    private static final String FIRST_VISIBLE_POS_STATE = "first_visible_position";
    private static final java.lang.String TOP_VIEW_POS_STATE = "top_view_position";

    @BindView(R.id.top_posts_list)
    RecyclerView topPostsList;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout topPostsRefreshLayout;

    @Inject
    TopListContract.TopListPresenter topListPresenter;

    @Inject
    GlideHelper glideHelper;

    private TopPostAdapter stylesAdapter;

    // activity lifecycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        ButterKnife.bind(this);

        topListPresenter.setView(this);

        topListPresenter.bringMorePosts(false);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        topPostsList.setLayoutManager(layoutManager);
        stylesAdapter = new TopPostAdapter(new ArrayList<PostHolder>(), glideHelper, this);
        topPostsList.setAdapter(stylesAdapter);
        topPostsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                View startView = topPostsList.getChildAt(0);
                int topView = (startView == null) ? 0 : (startView.getTop() - topPostsList.getPaddingTop());
                topListPresenter.updateScroll(visibleItemCount, totalItemCount, firstVisibleItemPosition, topView);

            }
        });

        topPostsRefreshLayout.setOnRefreshListener(this);

        if (savedInstanceState != null) {
            int firstVisibleItemPosition = savedInstanceState.getInt(FIRST_VISIBLE_POS_STATE);
            int topViewPosition = savedInstanceState.getInt(TOP_VIEW_POS_STATE);
            topListPresenter.restoreScroll(firstVisibleItemPosition, topViewPosition);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(FIRST_VISIBLE_POS_STATE, topListPresenter.getFirstVisiblePosition());
        outState.putInt(TOP_VIEW_POS_STATE, topListPresenter.getTopViewPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        topListPresenter.destroyView();
    }

    // TopListContract.TopListView methods
    @Override
    public void showListOfPosts(List<PostHolder> posts) {
        LogDebug.d(TAG, "showListOfPosts: "+posts);
        stylesAdapter.setPosts(posts);
    }

    @Override
    public void startImageViewer(String sourceUrl) {
        Intent imageViewerIntent = new Intent(this, FullImageActivity.class);
        imageViewerIntent.putExtra(FullImageActivity.SOURCE_IMAGE_INTENT_EXTRA, sourceUrl);
        startActivity(imageViewerIntent);
    }

    @Override
    public void showRefreshingPosts(boolean show) {
        topPostsRefreshLayout.setRefreshing(show);
    }

    @Override
    public void setScroll(int firstVisibleItemPosition, int topViewPosition) {
        ((LinearLayoutManager)topPostsList.getLayoutManager()).scrollToPositionWithOffset(firstVisibleItemPosition, topViewPosition);
    }

    @Override
    public void showErrorObtainingPosts() {
        Snackbar.make(topPostsList, R.string.error_loading_posts, Snackbar.LENGTH_LONG).show();
    }

    // PostActionCallback method
    @Override
    public void onThumbClick(String sourceUrl) {
        topListPresenter.openSelectedImage(sourceUrl);
    }

    // helper methods
    private void refreshPosts() {
        topListPresenter.refreshTopPosts();
    }

    @Override
    public void onRefresh() {
        refreshPosts();
    }
}
