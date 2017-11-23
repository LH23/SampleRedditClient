package com.sampleapp.redditclient.screens.toplist;

import com.sampleapp.redditclient.BasePresenter;
import com.sampleapp.redditclient.screens.toplist.adapter.PostHolder;

import java.util.List;

public interface TopListContract {

    interface TopListView {

        void showListOfPosts(List<PostHolder> posts);

        void startImageViewer(String sourceUrl);

        void showRefreshingPosts(boolean show);

        void setScroll(int firstVisibleItemPosition, int topViewPosition);

        void showErrorObtainingPosts();
    }

    interface TopListPresenter extends BasePresenter<TopListView> {

        void bringMorePosts(boolean forceRefresh);

        void openSelectedImage(String sourceUrl);

        void refreshTopPosts();

        void updateScroll(int visibleItemCount, int totalItemCount, int firstVisibleItemPosition, int topView);

        int getFirstVisiblePosition();

        int getTopViewPosition();

        void restoreScroll(int firstVisibleItemPosition, int topViewPosition);
    }
}
