package com.majinnaibu.monstercards.ui.editmonster;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.databinding.FragmentEditSkillsListItemBinding;
import com.majinnaibu.monstercards.models.Skill;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Skill}.
 */
public class EditSkillsRecyclerViewAdapter extends RecyclerView.Adapter<EditSkillsRecyclerViewAdapter.ViewHolder> {
    private final List<Skill> mValues;

    public EditSkillsRecyclerViewAdapter(List<Skill> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentEditSkillsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public Skill mItem;

        public ViewHolder(FragmentEditSkillsListItemBinding binding) {
            super(binding.getRoot());
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}