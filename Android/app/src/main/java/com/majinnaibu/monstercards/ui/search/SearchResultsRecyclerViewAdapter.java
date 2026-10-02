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
import com.majinnaibu.monstercards.data.enums.GameSystem;
import com.majinnaibu.monstercards.helpers.StringHelper;
import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.models.ReferenceMonster;
import com.majinnaibu.monstercards.models.SearchResultItem;
import com.majinnaibu.monstercards.ui.components.SourceTagView;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;

public class SearchResultsRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultsRecyclerViewAdapter.ViewHolder> {

    public enum ScopeMode {
        MY_LIBRARY,
        COMPENDIUMS,
        ALL,
        COLLECTIONS
    }

    public enum SystemFilter {
        ALL,
        DND_5E,
        PF_2E,
        SF_2E
    }

    // Retain legacy enum for backwards compatibility with tests
    public enum FilterMode {
        ALL,
        MY_LIBRARY,
        COMPENDIUMS,
        DND_5E,
        PF_2E,
        SF_2E,
        COLLECTIONS
    }

    private final MonsterRepository mRepository;
    private final ItemCallback mOnClickHandler;
    private String mSearchText;
    private List<SearchResultItem> mAllValues;
    private List<SearchResultItem> mFilteredValues;
    private ScopeMode mScopeMode = ScopeMode.COMPENDIUMS;
    private SystemFilter mSystemFilter = SystemFilter.ALL;
    private Disposable mSubscriptionHandler;

    public SearchResultsRecyclerViewAdapter(MonsterRepository repository,
                                            ItemCallback onClick) {
        mRepository = repository;
        mSearchText = "";
        mAllValues = new ArrayList<>();
        mFilteredValues = new ArrayList<>();
        mOnClickHandler = onClick;
        mSubscriptionHandler = null;

        doSearch(mSearchText);
    }

    public void setFilterScope(@NonNull ScopeMode scopeMode) {
        mScopeMode = scopeMode;
        applyFilter();
    }

    public void setSystemFilter(@NonNull SystemFilter systemFilter) {
        mSystemFilter = systemFilter;
        applyFilter();
    }

    public void setFilterMode(@NonNull FilterMode filterMode) {
        switch (filterMode) {
            case MY_LIBRARY:
                mScopeMode = ScopeMode.MY_LIBRARY;
                mSystemFilter = SystemFilter.ALL;
                break;
            case COMPENDIUMS:
                mScopeMode = ScopeMode.COMPENDIUMS;
                mSystemFilter = SystemFilter.ALL;
                break;
            case COLLECTIONS:
                mScopeMode = ScopeMode.COLLECTIONS;
                mSystemFilter = SystemFilter.ALL;
                break;
            case DND_5E:
                mScopeMode = ScopeMode.ALL;
                mSystemFilter = SystemFilter.DND_5E;
                break;
            case PF_2E:
                mScopeMode = ScopeMode.ALL;
                mSystemFilter = SystemFilter.PF_2E;
                break;
            case SF_2E:
                mScopeMode = ScopeMode.ALL;
                mSystemFilter = SystemFilter.SF_2E;
                break;
            case ALL:
            default:
                mScopeMode = ScopeMode.ALL;
                mSystemFilter = SystemFilter.ALL;
                break;
        }
        applyFilter();
    }

    private void applyFilter() {
        mFilteredValues = new ArrayList<>();
        for (SearchResultItem item : mAllValues) {
            if (matchesFilter(item)) {
                mFilteredValues.add(item);
            }
        }
        notifyDataSetChanged();
    }

    private boolean matchesFilter(SearchResultItem item) {
        if (item == null) return false;

        // 1. Check Scope
        boolean matchesScope = false;
        switch (mScopeMode) {
            case MY_LIBRARY:
                matchesScope = (item.type == SearchResultItem.Type.MONSTER);
                break;
            case COMPENDIUMS:
                matchesScope = (item.type == SearchResultItem.Type.REFERENCE_MONSTER);
                break;
            case COLLECTIONS:
                matchesScope = (item.type == SearchResultItem.Type.COLLECTION);
                break;
            case ALL:
            default:
                matchesScope = true;
                break;
        }

        if (!matchesScope) {
            return false;
        }

        // 2. Check System Filter
        if (mSystemFilter == SystemFilter.ALL) {
            return true;
        }

        if (item.type == SearchResultItem.Type.MONSTER && item.monster != null) {
            if (mSystemFilter == SystemFilter.DND_5E) return item.monster.gameSystem == GameSystem.DND_5E;
            if (mSystemFilter == SystemFilter.PF_2E) return item.monster.gameSystem == GameSystem.PF_2E;
            if (mSystemFilter == SystemFilter.SF_2E) return item.monster.gameSystem == GameSystem.SF_2E;
        } else if (item.type == SearchResultItem.Type.REFERENCE_MONSTER && item.referenceMonster != null) {
            if (mSystemFilter == SystemFilter.DND_5E) return item.referenceMonster.gameSystem == GameSystem.DND_5E;
            if (mSystemFilter == SystemFilter.PF_2E) return item.referenceMonster.gameSystem == GameSystem.PF_2E;
            if (mSystemFilter == SystemFilter.SF_2E) return item.referenceMonster.gameSystem == GameSystem.SF_2E;
        }

        return false;
    }

    public void doSearch(String searchText) {
        if (mSubscriptionHandler != null && !mSubscriptionHandler.isDisposed()) {
            mSubscriptionHandler.dispose();
        }
        mSearchText = searchText;
        Flowable<List<SearchResultItem>> resultsFlowable = mRepository.searchAll(mSearchText);
        mSubscriptionHandler = resultsFlowable.subscribe(results -> {
                    mAllValues = results;
                    applyFilter();
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
        SearchResultItem item = mFilteredValues.get(position);
        if (item.type == SearchResultItem.Type.MONSTER && item.monster != null) {
            Monster monster = item.monster;
            holder.mTitleView.setText(monster.name != null ? monster.name : "Unnamed Monster");
            holder.mSourceTagView.setMonster(monster);
            holder.mSourceTagView.setVisibility(View.VISIBLE);

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
        } else if (item.type == SearchResultItem.Type.REFERENCE_MONSTER && item.referenceMonster != null) {
            ReferenceMonster rm = item.referenceMonster;
            holder.mTitleView.setText(rm.name != null ? rm.name : "Unnamed Reference Monster");
            holder.mSourceTagView.setReferenceMonster(rm);
            holder.mSourceTagView.setVisibility(View.VISIBLE);

            StringBuilder sb = new StringBuilder();
            if (!StringHelper.isNullOrEmpty(rm.alignment)) {
                sb.append(rm.alignment);
            }
            String cr = rm.getChallengeRatingDescription();
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
            holder.mTitleView.setText(collection.name != null ? collection.name : "Unnamed Collection");
            holder.mSourceTagView.setVisibility(View.GONE);

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
        return mFilteredValues.size();
    }

    public interface ItemCallback {
        void onItem(SearchResultItem item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitleView;
        final SourceTagView mSourceTagView;
        final TextView mSubtitleView;
        final ImageView mIconView;

        ViewHolder(View view) {
            super(view);
            mTitleView = view.findViewById(R.id.title);
            mSourceTagView = view.findViewById(R.id.sourceTag);
            mSubtitleView = view.findViewById(R.id.subtitle);
            mIconView = view.findViewById(R.id.icon);
        }
    }
}
