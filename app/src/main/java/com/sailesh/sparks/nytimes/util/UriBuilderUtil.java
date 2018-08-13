package com.sailesh.sparks.nytimes.util;

import android.net.Uri;

public class UriBuilderUtil {

    public static String getURLForArticleSearch(String searchText, int page){
        Uri.Builder builder= new Uri.Builder();

        builder.scheme("https")
                .authority(Constants.HOST_URL)
                .appendPath("svc")
                .appendPath("search")
                .appendPath("v2")
                .appendPath("articlesearch.json")
                .appendQueryParameter("api_key",Constants.API_KEY)
                .appendQueryParameter(Constants.QUERY, searchText)
                .appendQueryParameter(Constants.PAGE,page+"");

        return builder.build().toString();
    }

}
