package com.majinnaibu.monstercards.ui.collections;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.models.Collection;
import com.majinnaibu.monstercards.models.CollectionWithCount;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CollectionsRecyclerViewAdapter extends RecyclerView.Adapter<CollectionsRecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private final CollectionCallback mOnClick;
    private final CollectionCallback mOnDelete;
    private final Flowable<List<CollectionWithCount>> mItemsObservable;
    private List<CollectionWithCount> mValues;
    private Disposable mDisposable;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(@NonNull View view) {
            Collection collection = (Collection) view.getTag();
            if (mOnClick != null) {
                mOnClick.onCallback(collection);
            }
        }
    };

    public CollectionsRecyclerViewAdapter(Context context,
                                          Flowable<List<CollectionWithCount>> itemsObservable,
                                          CollectionCallback onClick,
                                          CollectionCallback onDelete) {
        mContext = context;
        mItemsObservable = itemsObservable;
        mOnClick = onClick;
        mOnDelete = onDelete;
        mValues = new ArrayList<>();
        mDisposable = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollectionWithCount item = mValues.get(position);
        Collection collection = item.collection;
        holder.mNameView.setText(collection.name);

        String countText;
        if (item.monsterCount == 1) {
            countText = mContext.getString(R.string.format_monster_count_one);
        } else {
            countText = mContext.getString(R.string.format_monster_count_other, item.monsterCount);
        }
        holder.mCountView.setText(countText);
        holder.mCountView.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(collection.description)) {
            holder.mDescriptionView.setVisibility(View.VISIBLE);
            holder.mDescriptionView.setText(collection.description);
        } else {
            holder.mDescriptionView.setVisibility(View.GONE);
        }

        holder.itemView.setTag(collection);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mDisposable = mItemsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(collections -> {
                    mValues = collections;
                    notifyDataSetChanged();
                });
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    public void deleteItem(int position) {
        if (mOnDelete != null && position >= 0 && position < mValues.size()) {
            Collection collection = mValues.get(position).collection;
            mOnDelete.onCallback(collection);
        }
    }

    public interface CollectionCallback {
        void onCallback(Collection collection);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mNameView;
        final TextView mDescriptionView;
        final TextView mCountView;

        ViewHolder(View view) {
            super(view);
            mNameView = view.findViewById(R.id.collection_name);
            mDescriptionView = view.findViewById(R.id.collection_description);
            mCountView = view.findViewById(R.id.collection_count);
        }
    }
}
