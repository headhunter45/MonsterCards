package com.majinnaibu.monstercards.ui.shared;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.databinding.SimpleListItemBinding;

public class SimpleListItemViewHolder<T> extends RecyclerView.ViewHolder {
    public final TextView contentView;
    public T item;

    public SimpleListItemViewHolder(SimpleListItemBinding binding) {
        super(binding.getRoot());
        contentView = binding.content;
    }
}
