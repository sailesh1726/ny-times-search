package com.sailesh.sparks.nytimes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sailesh.sparks.nytimes.databinding.NyItemBinding;
import com.sailesh.sparks.nytimes.viewmodel.NyAdapterViewModel;

import java.util.List;

public class NyAdapter extends RecyclerView.Adapter<NyAdapter.NyViewHolder> {

    private Context context;
    private List<NyAdapterViewModel> mNyAdapterViewModelList;

    public NyAdapter(Context context, List<NyAdapterViewModel> nyAdapterViewModelList) {
        this.context = context;
        this.mNyAdapterViewModelList = nyAdapterViewModelList;
    }

    @NonNull
    @Override
    public NyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        NyItemBinding nyItemBinding =
                NyItemBinding.inflate(layoutInflater, parent, false);
        return new NyViewHolder(nyItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NyViewHolder holder, int position) {
        NyAdapterViewModel item = mNyAdapterViewModelList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mNyAdapterViewModelList.size();
    }

    public void updateListElements(List<NyAdapterViewModel> nyAdapterViewModelList) {
        int positionStart = this.mNyAdapterViewModelList.size();
        int itemCount = nyAdapterViewModelList.size();
        this.mNyAdapterViewModelList = nyAdapterViewModelList;
        notifyItemRangeInserted(positionStart, itemCount);
    }

    class NyViewHolder extends RecyclerView.ViewHolder {

        private NyItemBinding nyItemBinding;

        public NyViewHolder(NyItemBinding nyItemBinding) {
            super(nyItemBinding.getRoot());
            this.nyItemBinding = nyItemBinding;
        }

        public void bind(NyAdapterViewModel nyAdapterViewModel) {
            nyAdapterViewModel.setContext(context);
            nyItemBinding.setNyItemView(nyAdapterViewModel);
            nyItemBinding.executePendingBindings();
        }
    }
}
