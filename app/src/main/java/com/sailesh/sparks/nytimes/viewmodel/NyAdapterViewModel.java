package com.sailesh.sparks.nytimes.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;
import com.sailesh.sparks.nytimes.R;
import com.sailesh.sparks.nytimes.model.ArticleSearch;
import com.sailesh.sparks.nytimes.model.HeadLine;
import com.sailesh.sparks.nytimes.model.MultiMedia;
import com.sailesh.sparks.nytimes.util.Constants;
import com.sailesh.sparks.nytimes.view.DetailScreenActivity;

import java.util.ArrayList;

public class NyAdapterViewModel extends BaseObservable {
    private String headline;
    private String imageUrl;
    private String webUrl;
    private Context mContext;

    public NyAdapterViewModel(ArticleSearch articleSearch) {
        this.headline = getHeadLine(articleSearch.getHeadline());
        this.imageUrl = getThumbnailUrl(articleSearch.getMultimedia());
        this.webUrl = articleSearch.getWeb_url();
    }

    private String getHeadLine(HeadLine headLine) {
        String headString = "";
        if (headLine != null) {
            headString = headLine.getMain();
        }
        return headString;
    }

    private String getThumbnailUrl(ArrayList<MultiMedia> multiMediaList) {
        String thumbnailUrl = "";
        if (multiMediaList != null && !multiMediaList.isEmpty()) {
            for (MultiMedia m : multiMediaList) {
                if (!TextUtils.isEmpty(m.getUrl())) {
                    thumbnailUrl = Constants.BASE_URI + m.getUrl();
                    break;
                }
            }
        }
        return thumbnailUrl;
    }

    @Bindable
    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
        notifyPropertyChanged(BR.headline);
    }

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.default_placeholder)
                .into(view);
    }

    public void onClick(View view, NyAdapterViewModel nyAdapterViewModel) {
        String webUrl = nyAdapterViewModel.getWebUrl();

        Intent intent = new Intent(mContext, DetailScreenActivity.class);
        intent.putExtra("webUrl", webUrl);
        mContext.startActivity(intent);
    }
}
