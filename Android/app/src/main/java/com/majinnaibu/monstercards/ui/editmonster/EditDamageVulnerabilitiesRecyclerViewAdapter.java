package com.majinnaibu.monstercards.ui.editmonster;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.databinding.FragmentEditDamageVulnerabilitiesListItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * x
 * {@link RecyclerView.Adapter} that can display a {@link String}.
 */
public class EditDamageVulnerabilitiesRecyclerViewAdapter extends RecyclerView.Adapter<EditDamageVulnerabilitiesRecyclerViewAdapter.ViewHolder> {
    private final List<String> mValues;
    private final ItemCallback mOnClick;

    public EditDamageVulnerabilitiesRecyclerViewAdapter(List<String> items, ItemCallback onClick) {
        mValues = items;
        mOnClick = onClick;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentEditDamageVulnerabilitiesListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        void onItemCallback(String sense);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public String mItem;

        public ViewHolder(FragmentEditDamageVulnerabilitiesListItemBinding binding) {
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