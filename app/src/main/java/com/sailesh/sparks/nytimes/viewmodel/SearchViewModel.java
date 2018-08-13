package com.sailesh.sparks.nytimes.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sailesh.sparks.nytimes.model.ArticleSearch;
import com.sailesh.sparks.nytimes.model.Meta;
import com.sailesh.sparks.nytimes.util.Constants;
import com.sailesh.sparks.nytimes.util.UriBuilderUtil;
import com.sailesh.sparks.nytimes.util.VolleyTon;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends BaseObservable {

    private String noResult;
    private boolean emptyList;
    private boolean progressBarVisibility = false;
    private boolean emptyViewVisibility = false;

    private int pageIndex;
    private boolean loading;
    private Context mContext;
    //    private ArrayList<ArticleSearch> articleSearchList = new ArrayList<>();
    private ViewModelListener viewModelListener;

    public SearchViewModel(Context context, ViewModelListener viewModelListener){
        mContext = context;
        this.viewModelListener = viewModelListener;
    }

    @Bindable
    public String getNoResult() {
        return noResult;
    }

    public void setNoResult(String noResult) {
        this.noResult = noResult;
        notifyPropertyChanged(BR.noResult);
    }

    @Bindable
    public boolean isEmptyViewVisibility() {
        return emptyViewVisibility;
    }

    public void setEmptyViewVisibility(boolean emptyViewVisibility) {
        this.emptyViewVisibility = emptyViewVisibility;
        notifyPropertyChanged(BR.emptyViewVisibility);
    }

    @Bindable
    public boolean isEmptyList() {
        return emptyList;
    }

    public void setEmptyList(boolean emptyList) {
        this.emptyList = emptyList;
        notifyPropertyChanged(BR.emptyList);
    }

    @Bindable
    public boolean isProgressBarVisibilty() {
        return progressBarVisibility;
    }

    public void setProgressBarVisibility(boolean progressBarVisibility) {
        this.progressBarVisibility = progressBarVisibility;
        notifyPropertyChanged(BR.progressBarVisibilty);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void fetchNyTimesData(String searchText, int page) {
        if(isNetworkAvailable()) {
            if (!loading) {
                setProgressBarVisibility(true);
                String url = UriBuilderUtil.getURLForArticleSearch(searchText, page);
                if (page == 0) {
                    pageIndex = 0;
                }
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                        if (jsonObject.has(Constants.RESPONSE)) {
                            JsonObject jsonResponseObject = jsonObject.getAsJsonObject(Constants.RESPONSE);
                            if (jsonResponseObject != null) {
                                JsonArray jsonDocsArray = jsonResponseObject.getAsJsonArray(Constants.DOCS);
                                Type collectionType = new TypeToken<List<ArticleSearch>>() {
                                }.getType();

                                JsonObject jsonMeta = jsonResponseObject.getAsJsonObject(Constants.META);
                                Type collectionMetaType = new TypeToken<Meta>() {
                                }.getType();

                                Meta fetchedMeta = gson.fromJson(jsonMeta, collectionMetaType);

                                updateIndex(fetchedMeta.getOffset());

                                List<ArticleSearch> fetchedDocs = gson.fromJson(jsonDocsArray,
                                        collectionType);
//                        articleSearchList.addAll(fetchedDocs);
                                List<NyAdapterViewModel> nyAdapterViewModelList = new ArrayList<>();
                                for (ArticleSearch articleSearch : fetchedDocs) {
                                    nyAdapterViewModelList.add(new NyAdapterViewModel(articleSearch));
                                }
                                viewModelListener.updateListView(nyAdapterViewModelList);
                            }
                        }
                        loading = false;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.getNetworkTimeMs();
                        loading = false;
                        setProgressBarVisibility(false);
                        Toast.makeText(mContext.getApplicationContext(), "oops something went wrong, please try again later",
                                Toast.LENGTH_LONG).show();
                    }
                });

                loading = true;
                VolleyTon.getInstance().addToRequestQueue(stringRequest);
            }
        }else {
            Toast.makeText(mContext.getApplicationContext(), "please check your network connection",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void updateIndex(int offset){
        pageIndex = offset/10 + 1;
    }

    public interface ViewModelListener {
        void updateListView(List<NyAdapterViewModel> nyAdapterViewModelList);
    }
}
