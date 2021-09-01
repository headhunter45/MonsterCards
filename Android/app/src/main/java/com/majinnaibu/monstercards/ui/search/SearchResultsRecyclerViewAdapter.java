package com.majinnaibu.monstercards.ui.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.majinnaibu.monstercards.databinding.SimpleListItemBinding;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.shared.SimpleListItemViewHolder;
import com.majinnaibu.monstercards.utils.ItemCallback;

public class SearchResultsRecyclerViewAdapter extends ListAdapter<Monster, SimpleListItemViewHolder<Monster>> {
    private static final DiffUtil.ItemCallback<Monster> DIFF_CALLBACK = new DiffUtil.ItemCallback<Monster>() {
        @Override
        public boolean areItemsTheSame(@NonNull Monster oldItem, @NonNull Monster newItem) {
            return Monster.areItemsTheSame(oldItem, newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Monster oldItem, @NonNull Monster newItem) {
            return Monster.areContentsTheSame(oldItem, newItem);
        }
    };
    private final ItemCallback<Monster> mOnClick;

    public SearchResultsRecyclerViewAdapter(ItemCallback<Monster> onClick) {
        super(DIFF_CALLBACK);
        mOnClick = onClick;
    }

    @NonNull
    @Override
    public SimpleListItemViewHolder<Monster> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SimpleListItemBinding binding = SimpleListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SimpleListItemViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleListItemViewHolder<Monster> holder, int position) {
        Monster monster = getItem(position);
        holder.item = monster;
        holder.contentView.setText(monster.name);
        holder.itemView.setOnClickListener(view -> {
            if (mOnClick != null) {
                mOnClick.onItem(holder.item);
            }
        });
    }
}
