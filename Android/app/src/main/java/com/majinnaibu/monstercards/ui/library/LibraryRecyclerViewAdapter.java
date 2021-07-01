package com.majinnaibu.monstercards.ui.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.models.Monster;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LibraryRecyclerViewAdapter extends RecyclerView.Adapter<LibraryRecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private final ItemCallback mOnDelete;
    private final ItemCallback mOnClick;
    private final Flowable<List<Monster>> mItemsObservable;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Monster monster = (Monster) view.getTag();
            if (mOnClick != null) {
                mOnClick.onItemCallback(monster);
            }
        }
    };
    private List<Monster> mValues;
    private Disposable mDisposable;

    public LibraryRecyclerViewAdapter(Context context,
                                      Flowable<List<Monster>> itemsObservable,
                                      ItemCallback onClick,
                                      ItemCallback onDelete) {
        mItemsObservable = itemsObservable;
        mValues = new ArrayList<>();
        mContext = context;
        mOnDelete = onDelete;
        mOnClick = onClick;
        mDisposable = null;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.monster_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        Monster monster = mValues.get(position);
        holder.mContentView.setText(monster.name);
        holder.itemView.setTag(monster);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // TODO: consider moving this subscription out of the adapter and make the subscriber call setItems on the adapter
        mDisposable = mItemsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monsters -> {
                    mValues = monsters;
                    notifyDataSetChanged();
                });
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mDisposable.dispose();
    }

    public void deleteItem(int position) {
        if (mOnDelete != null) {
            Monster monster = mValues.get(position);
            mOnDelete.onItemCallback(monster);
        }
    }

    public interface ItemCallback {
        void onItemCallback(Monster monster);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mContentView = view.findViewById(R.id.content);
        }
    }
}
