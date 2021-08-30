package com.majinnaibu.monstercards.ui.editmonster;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.databinding.FragmentEditStringsListItemBinding;
import com.majinnaibu.monstercards.utils.ItemCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EditStringsRecyclerViewAdapter extends RecyclerView.Adapter<EditStringsRecyclerViewAdapter.ViewHolder> {
    private final List<String> mValues;
    private final ItemCallback<String> mOnClick;

    public EditStringsRecyclerViewAdapter(List<String> items, ItemCallback<String> onClick) {
        mValues = items;
        mOnClick = onClick;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentEditStringsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (mOnClick != null) {
                mOnClick.onItem(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public String mItem;

        public ViewHolder(FragmentEditStringsListItemBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
        }

        @NotNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
