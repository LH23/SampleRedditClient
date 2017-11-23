package com.sampleapp.redditclient.screens.toplist;

import android.os.AsyncTask;

import com.sampleapp.redditclient.model.RedditPost;
import com.sampleapp.redditclient.model.RedditPostRepository;
import com.sampleapp.redditclient.screens.toplist.adapter.PostHolder;
import com.sampleapp.redditclient.utils.InternetUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TopListPresenterImpl implements TopListContract.TopListPresenter {

    private static final int MAX_NUM_POSTS = 50;
    private static final int PAGE_SIZE = 10;

    private RedditPostRepository postRepo;
    private InternetUtils internetUtils;

    private TopListContract.TopListView topListView;
    private List<PostHolder> postsHolders;
    private int firstVisibleItemPosition;
    private boolean loadingInProgress;
    private boolean noMoreItems;
    private int topViewPosition;
    private boolean restoreScroll;



    public TopListPresenterImpl(RedditPostRepository postRepo, InternetUtils internetUtils) {
        this.postRepo = postRepo;
        this.internetUtils = internetUtils;
        this.postsHolders = new ArrayList<>();
    }

    // TopListContract.TopListPresenter methods
    @Override
    public void setView(TopListContract.TopListView view) {
        topListView = view;
    }

    @Override
    public void destroyView() {
        topListView = null;
    }

    @Override
    public boolean isViewAttached() {
        return topListView != null;
    }

    @Override
    public void bringMorePosts(boolean forceRefresh) {
        if (!internetUtils.isInternetConnected()) {
            if (isViewAttached()) {
                topListView.showRefreshingPosts(false);
                topListView.showErrorObtainingPosts();
            }
            return;
        }
        if (loadingInProgress || noMoreItems) {
            return;
        }
        this.loadingInProgress = true;

        if (isViewAttached()) {
            topListView.showRefreshingPosts(true);
        }
        new UpdateTopPostTask(this, postRepo, PAGE_SIZE, forceRefresh).execute();
    }

    @Override
    public void openSelectedImage(String sourceUrl) {
        if (isViewAttached()) {
            topListView.startImageViewer(sourceUrl);
        }
    }

    @Override
    public void refreshTopPosts() {
        postRepo.clearPostsList();
        noMoreItems = false;
        bringMorePosts(true);
    }

    @Override
    public void updateScroll(int visibleItemCount, int totalItemCount, int firstVisibleItemPosition, int topView) {
        this.firstVisibleItemPosition = firstVisibleItemPosition;
        this.topViewPosition = topView;
        if (loadingInProgress) {
            return;
        }
        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE) {
            bringMorePosts(false);
        }
    }

    @Override
    public int getFirstVisiblePosition() {
        return firstVisibleItemPosition;
    }

    @Override
    public int getTopViewPosition() {
        return topViewPosition;
    }

    @Override
    public void restoreScroll(int firstVisibleItemPosition, int topViewPosition) {
        this.firstVisibleItemPosition = firstVisibleItemPosition;
        this.topViewPosition = topViewPosition;
        this.restoreScroll = true;
    }

    // helper methods
    private void setNewPostList(List<RedditPost> posts) {
        this.postsHolders = convertToHolders(posts);
        this.loadingInProgress = false;
        if (isViewAttached()) {
            topListView.showRefreshingPosts(false);
            topListView.showListOfPosts(this.postsHolders);
        }
    }

    private List<PostHolder> convertToHolders(List<RedditPost> posts) {
        List<PostHolder> holders = new ArrayList<>(posts.size());
        for (RedditPost post :posts) {
            holders.add(new PostHolder(post));
        }
        return holders;
    }

    // helper class
    private static class UpdateTopPostTask extends AsyncTask<Void,Void,List<RedditPost>> {

        private static final String TAG = "UpdateTopPostTask";
        private final int pageSize;
        WeakReference<TopListPresenterImpl> weak_presenter;
        private RedditPostRepository postRepo;
        private boolean forceRefresh;

        UpdateTopPostTask(TopListPresenterImpl presenter, RedditPostRepository postRepo, int pageSize, boolean forceRefresh) {
            this.weak_presenter = new WeakReference<>(presenter);
            this.postRepo = postRepo;
            this.pageSize = pageSize;
            this.forceRefresh = forceRefresh;
        }

        @Override
        protected List<RedditPost> doInBackground(Void... voids) {
            return postRepo.getMorePostsSync(pageSize, forceRefresh);
        }

        @Override
        protected void onPostExecute(List<RedditPost> redditPosts) {
            super.onPostExecute(redditPosts);
            TopListPresenterImpl presenter = weak_presenter.get();
            if (presenter != null) {
                if (redditPosts == null) {
                    presenter.topListView.showRefreshingPosts(false);
                    presenter.topListView.showErrorObtainingPosts();
                    return;
                }
                if (redditPosts.size() > 50) {
                    throw new IllegalStateException("more than 50 postsHolders: "+redditPosts.size());
                }
                if (redditPosts.size() == MAX_NUM_POSTS) {
                    presenter.noMoreItems = true;
                }
                presenter.setNewPostList(redditPosts);
                if (presenter.restoreScroll) {
                    presenter.restoreScroll = false;
                    if (presenter.isViewAttached()) {
                        presenter.topListView.setScroll(presenter.firstVisibleItemPosition, presenter.topViewPosition);
                    }
                }
            }
        }
    }
}
