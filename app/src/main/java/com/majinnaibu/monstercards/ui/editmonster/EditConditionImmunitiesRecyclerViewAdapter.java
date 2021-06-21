package com.majinnaibu.monstercards.ui.editmonster;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.databinding.FragmentEditConditionImmunitiesListItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link String}.
 */
public class EditConditionImmunitiesRecyclerViewAdapter extends RecyclerView.Adapter<EditConditionImmunitiesRecyclerViewAdapter.ViewHolder> {
    private final List<String> mValues;
    private final ItemCallback mOnClick;

    public EditConditionImmunitiesRecyclerViewAdapter(List<String> items, ItemCallback onClick) {
        mValues = items;
        mOnClick = onClick;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentEditConditionImmunitiesListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
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
        void onItemCallback(String condition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public String mItem;

        public ViewHolder(FragmentEditConditionImmunitiesListItemBinding binding) {
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