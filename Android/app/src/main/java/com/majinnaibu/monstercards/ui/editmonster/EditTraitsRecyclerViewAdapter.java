package com.majinnaibu.monstercards.ui.editmonster;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.databinding.FragmentEditTraitsListItemBinding;
import com.majinnaibu.monstercards.models.Trait;
import com.majinnaibu.monstercards.utils.ItemCallback;

public class EditTraitsRecyclerViewAdapter extends ListAdapter<Trait, EditTraitsRecyclerViewAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<Trait> DIFF_CALLBACK = new DiffUtil.ItemCallback<Trait>() {

        @Override
        public boolean areItemsTheSame(@NonNull Trait oldItem, @NonNull Trait newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Trait oldItem, @NonNull Trait newItem) {
            return oldItem.equals(newItem);
        }
    };
    private final ItemCallback<Trait> mOnClick;

    protected EditTraitsRecyclerViewAdapter(ItemCallback<Trait> onClick) {
        super(DIFF_CALLBACK);
        mOnClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentEditTraitsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = getItem(position);
        holder.mContentView.setText(holder.mItem.name);
        holder.itemView.setOnClickListener(v -> {
            if (mOnClick != null) {
                mOnClick.onItem(holder.mItem);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public Trait mItem;

        public ViewHolder(@NonNull FragmentEditTraitsListItemBinding binding) {
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
