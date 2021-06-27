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

import org.jetbrains.annotations.NotNull;

public class EditTraitsRecyclerViewAdapter extends ListAdapter<Trait, EditTraitsRecyclerViewAdapter.ViewHolder> {
    private static final DiffUtil.ItemCallback<Trait> DIFF_CALLBACK = new DiffUtil.ItemCallback<Trait>() {

        @Override
        public boolean areItemsTheSame(@NonNull @NotNull Trait oldItem, @NonNull @NotNull Trait newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull Trait oldItem, @NonNull @NotNull Trait newItem) {
            return oldItem.equals(newItem);
        }
    };
    private final ItemCallback mOnClick;

    protected EditTraitsRecyclerViewAdapter(ItemCallback onClick) {
        super(DIFF_CALLBACK);
        mOnClick = onClick;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentEditTraitsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = getItem(position);
        holder.mContentView.setText(holder.mItem.name);
        holder.itemView.setOnClickListener(v -> {
            if (mOnClick != null) {
                mOnClick.onItemCallback(holder.mItem);
            }
        });
    }

    public interface ItemCallback {
        void onItemCallback(Trait trait);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public Trait mItem;

        public ViewHolder(FragmentEditTraitsListItemBinding binding) {
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
