package com.majinnaibu.monstercards.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.utils.ItemCallback;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchResultsRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultsRecyclerViewAdapter.ViewHolder> {
    private final MonsterRepository mRepository;
    private final ItemCallback<Monster> mOnClickHandler;
    private String mSearchText;
    private List<Monster> mValues;
    private Disposable mSubscriptionHandler;

    public SearchResultsRecyclerViewAdapter(MonsterRepository repository,
                                            ItemCallback<Monster> onClick) {
        mRepository = repository;
        mSearchText = "";
        mValues = new ArrayList<>();
        mOnClickHandler = onClick;
        mSubscriptionHandler = null;

        doSearch(mSearchText);
    }

    public void doSearch(String searchText) {
        if (mSubscriptionHandler != null && !mSubscriptionHandler.isDisposed()) {
            mSubscriptionHandler.dispose();
        }
        mSearchText = searchText;
        Flowable<List<Monster>> foundMonsters = mRepository.searchMonsters(mSearchText);
        mSubscriptionHandler = foundMonsters.subscribe(monsters -> {
                    mValues = monsters;
                    notifyDataSetChanged();
                },
                throwable -> Logger.logError("Error performing search", throwable));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.monster_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Monster monster = mValues.get(position);
        holder.mContentView.setText(monster.name);
        holder.itemView.setTag(monster);
        holder.itemView.setOnClickListener(view -> {
            if (mOnClickHandler != null) {
                mOnClickHandler.onItem(monster);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mContentView = view.findViewById(R.id.content);
        }
    }
}
