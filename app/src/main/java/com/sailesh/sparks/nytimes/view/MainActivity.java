package com.sailesh.sparks.nytimes.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sailesh.sparks.nytimes.R;
import com.sailesh.sparks.nytimes.adapters.NyAdapter;
import com.sailesh.sparks.nytimes.adapters.PaginationScrollListener;
import com.sailesh.sparks.nytimes.databinding.SearchBinding;
import com.sailesh.sparks.nytimes.viewmodel.NyAdapterViewModel;
import com.sailesh.sparks.nytimes.viewmodel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener, SearchViewModel.ViewModelListener {

    private SearchBinding searchBinding;
    private SearchViewModel searchViewModel;
    private RecyclerView recyclerView;
    private List<NyAdapterViewModel> nyAdapterViewModelList;
    private String queryText = "News";
    private NyAdapter nyAdapter;
    private SearchView searchView;
    private LinearLayoutManager mLinearLayoutManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        searchViewModel = new SearchViewModel(this, this);
        searchBinding.setSearchView(searchViewModel);
        setSupportActionBar(searchBinding.toolbar);
        recyclerView = searchBinding.nyRecycleView;
        mLinearLayoutManger = new LinearLayoutManager(this);
        searchViewModel.setProgressBarVisibility(true);
        recyclerView.addOnScrollListener(new PaginationScrollListener(mLinearLayoutManger) {
            @Override
            public void loadMore() {
                searchViewModel.fetchNyTimesData(queryText, searchViewModel.getPageIndex());
                Toast.makeText(getApplicationContext(), "Loading More...", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(mLinearLayoutManger);

        nyAdapterViewModelList = new ArrayList<>();

        nyAdapter = new NyAdapter(this, nyAdapterViewModelList);
        recyclerView.setAdapter(nyAdapter);
        searchViewModel.fetchNyTimesData(queryText, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        nyAdapterViewModelList.clear();
        nyAdapter.notifyDataSetChanged();
        queryText = query;
        searchViewModel.fetchNyTimesData(query, 0);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void updateListView(List<NyAdapterViewModel> nyAdapterViewModelList) {
        this.nyAdapterViewModelList.addAll(nyAdapterViewModelList);
        nyAdapter.updateListElements(this.nyAdapterViewModelList);
    }
}
