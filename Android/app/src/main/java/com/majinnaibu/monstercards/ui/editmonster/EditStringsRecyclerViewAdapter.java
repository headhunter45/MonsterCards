package com.majinnaibu.monstercards.ui.editmonster;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.databinding.FragmentEditStringsListItemBinding;

import java.util.List;

public class EditStringsRecyclerViewAdapter extends RecyclerView.Adapter<EditStringsRecyclerViewAdapter.ViewHolder> {
    private final List<String> mValues;
    private final ItemCallback mOnClick;

    public EditStringsRecyclerViewAdapter(List<String> items, ItemCallback onClick) {
        mValues = items;
        mOnClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentEditStringsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (mOnClick != null) {
                mOnClick.onItemCallback(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemCallback {
        void onItemCallback(String value);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public String mItem;

        public ViewHolder(@NonNull FragmentEditStringsListItemBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
