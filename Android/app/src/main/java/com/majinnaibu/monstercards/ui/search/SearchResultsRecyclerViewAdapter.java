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
import com.majinnaibu.monstercards.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchResultsRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultsRecyclerViewAdapter.ViewHolder> {
    public interface ItemCallback {
        void onItem(Monster monster);
    }

    private final MonsterRepository mRepository;
    private String mSearchText;
    private List<Monster> mValues;
    private Disposable mSubscriptionHandler;
    private final ItemCallback mOnClickHandler;

    public SearchResultsRecyclerViewAdapter(MonsterRepository repository,
                                            ItemCallback onClick) {
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

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.monster_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Monster monster = mValues.get(position);
        holder.mIdView.setText(monster.id.toString().substring(0, 6));
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

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.id_text);
            mContentView = view.findViewById(R.id.content);
        }
    }
}
