package com.sampleapp.redditclient;

public interface BasePresenter<T> {

    public void setView(T view);

    public void destroyView();

    public boolean isViewAttached();

}