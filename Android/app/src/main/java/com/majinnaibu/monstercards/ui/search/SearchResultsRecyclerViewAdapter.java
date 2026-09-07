package com.majinnaibu.monstercards.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.SearchResultItem;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchResultsRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultsRecyclerViewAdapter.ViewHolder> {
    private final MonsterRepository mRepository;
    private final ItemCallback mOnClickHandler;
    private String mSearchText;
    private List<SearchResultItem> mValues;
    private Disposable mSubscriptionHandler;

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
        Flowable<List<SearchResultItem>> resultsFlowable = mRepository.searchAll(mSearchText);
        mSubscriptionHandler = resultsFlowable.subscribe(results -> {
                    mValues = results;
                    notifyDataSetChanged();
                },
                throwable -> Logger.logError("Error performing search", throwable));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        SearchResultItem item = mValues.get(position);
        if (item.type == SearchResultItem.Type.MONSTER && item.monster != null) {
            Monster monster = item.monster;
            holder.mTitleView.setText(monster.name);

            StringBuilder sb = new StringBuilder();
            if (!StringHelper.isNullOrEmpty(monster.alignment)) {
                sb.append(monster.alignment);
            }
            String cr = monster.getChallengeRatingDescription();
            if (!StringHelper.isNullOrEmpty(cr)) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append("CR ").append(cr);
            }
            if (sb.length() > 0) {
                holder.mSubtitleView.setText(sb.toString());
                holder.mSubtitleView.setVisibility(View.VISIBLE);
            } else {
                holder.mSubtitleView.setVisibility(View.GONE);
            }
            holder.mIconView.setVisibility(View.GONE);
        } else if (item.type == SearchResultItem.Type.COLLECTION && item.collection != null) {
            Collection collection = item.collection;
            holder.mTitleView.setText(collection.name);

            if (!StringHelper.isNullOrEmpty(collection.description)) {
                holder.mSubtitleView.setText(collection.description);
                holder.mSubtitleView.setVisibility(View.VISIBLE);
            } else {
                holder.mSubtitleView.setVisibility(View.GONE);
            }
            holder.mIconView.setVisibility(View.VISIBLE);
        }

        holder.itemView.setTag(item);
        holder.itemView.setOnClickListener(view -> {
            if (mOnClickHandler != null) {
                mOnClickHandler.onItem(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemCallback {
        void onItem(SearchResultItem item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitleView;
        final TextView mSubtitleView;
        final ImageView mIconView;

        ViewHolder(View view) {
            super(view);
            mTitleView = view.findViewById(R.id.title);
            mSubtitleView = view.findViewById(R.id.subtitle);
            mIconView = view.findViewById(R.id.icon);
        }
    }
}
