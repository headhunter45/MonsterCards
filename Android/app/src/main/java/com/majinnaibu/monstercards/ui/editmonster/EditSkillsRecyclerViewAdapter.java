package com.majinnaibu.monstercards.ui.editmonster;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.databinding.FragmentEditSkillsListItemBinding;
import com.majinnaibu.monstercards.models.Skill;
import com.majinnaibu.monstercards.utils.ItemCallback;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Skill}.
 */
public class EditSkillsRecyclerViewAdapter extends RecyclerView.Adapter<EditSkillsRecyclerViewAdapter.ViewHolder> {
    private final List<Skill> mValues;
    private final ItemCallback<Skill> mOnClick;

    public EditSkillsRecyclerViewAdapter(List<Skill> items, ItemCallback<Skill> onClick) {
        mValues = items;
        mOnClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentEditSkillsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).name);
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
        public Skill mItem;

        public ViewHolder(@NonNull FragmentEditSkillsListItemBinding binding) {
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
