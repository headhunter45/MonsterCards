package com.majinnaibu.monstercards.ui.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.utils.ItemCallback;

public class LibraryRecyclerViewAdapter extends ListAdapter<Monster, LibraryRecyclerViewAdapter.ViewHolder> {
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

    public LibraryRecyclerViewAdapter(ItemCallback<Monster> onClick) {
        super(DIFF_CALLBACK);
        mOnClick = onClick;
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
        Monster monster = getItem(position);
        holder.item = monster;
        holder.contentView.setText(monster.name);
        holder.itemView.setTag(monster);
        holder.itemView.setOnClickListener(v -> {
            if (mOnClick != null) {
                mOnClick.onItem(holder.item);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView contentView;
        Monster item;

        ViewHolder(View view) {
            super(view);
            contentView = view.findViewById(R.id.content);
        }
    }
}
