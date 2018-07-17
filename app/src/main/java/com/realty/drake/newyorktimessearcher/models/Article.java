package com.realty.drake.newyorktimessearcher.models;

/**
 * Created by drake on 7/16/18
 */
public class Article {
    private String mHeadline;
    private String mSnippet;
    private String mThumbnailUrl;
    private String mNewsDesk;

    public Article(String mHeadline, String mSnippet,
                   String mThumbnailUrl, String mNewsDesk) {
        this.mHeadline = mHeadline;
        this.mSnippet = mSnippet;
        this.mThumbnailUrl = mThumbnailUrl;
        this.mNewsDesk = mNewsDesk;
    }

    public String getmHeadline() {
        return mHeadline;
    }

    public void setmHeadline(String mHeadline) {
        this.mHeadline = mHeadline;
    }

    public String getmSnippet() {
        return mSnippet;
    }

    public void setmSnippet(String mSnippet) {
        this.mSnippet = mSnippet;
    }

    public String getmThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setmThumbnailUrl(String mThumbnailUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
    }

    public String getmNewsDesk() {
        return mNewsDesk;
    }

    public void setmNewsDesk(String mNewsDesk) {
        this.mNewsDesk = mNewsDesk;
    }
}
