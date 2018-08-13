package com.sailesh.sparks.nytimes.model;

import java.util.ArrayList;

public class ArticleSearch {
    private String web_url;
    private String snippet;
    private HeadLine headline;
    private ArrayList<MultiMedia> multimedia;

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public HeadLine getHeadline() {
        return headline;
    }

    public void setHeadline(HeadLine headline) {
        this.headline = headline;
    }

    public ArrayList<MultiMedia> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(ArrayList<MultiMedia> multimedia) {
        this.multimedia = multimedia;
    }
}
