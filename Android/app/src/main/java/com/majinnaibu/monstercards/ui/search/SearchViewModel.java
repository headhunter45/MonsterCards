package com.majinnaibu.monstercards.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mSearchQuery;

    public SearchViewModel() {
        mSearchQuery = new MutableLiveData<>();
        mSearchQuery.setValue("");
    }

    public LiveData<String> getSearchQuery() {
        return mSearchQuery;
    }
}