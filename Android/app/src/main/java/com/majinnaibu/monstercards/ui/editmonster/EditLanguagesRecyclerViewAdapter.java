package com.majinnaibu.monstercards.ui.editmonster;

import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.databinding.FragmentEditLanguagesListHeaderBinding;
import com.majinnaibu.monstercards.databinding.FragmentEditLanguagesListItemBinding;
import com.majinnaibu.monstercards.models.Language;
import com.majinnaibu.monstercards.ui.components.Stepper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EditLanguagesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Language> mValues;
    private final ItemCallback mOnClick;
    private final int mTelepathyRange;
    private final String mUnderstandsBut;
    private final Stepper.OnValueChangeListener mOnTelepathyRangeChanged;
    private final TextWatcher mOnUnderstandsButChanged;

    private final int HEADER_VIEW_TYPE = 1;
    private final int ITEM_VIEW_TYPE = 2;
    private final String DISTANCE_IN_FEET_FORMAT = "%d ft.";

    public EditLanguagesRecyclerViewAdapter(List<Language> items, ItemCallback onClick, int telepathyRange, Stepper.OnValueChangeListener telepathyRangeChangedListener, String undderstandsBut, TextWatcher understandsButChangedListener) {
        mValues = items;
        mOnClick = onClick;
        mTelepathyRange = telepathyRange;
        mOnTelepathyRangeChanged = telepathyRangeChangedListener;
        mUnderstandsBut = undderstandsBut;
        mOnUnderstandsButChanged = understandsButChangedListener;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW_TYPE) {
            return new HeaderViewHolder(FragmentEditLanguagesListHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
        return new ItemViewHolder(FragmentEditLanguagesListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.telepathy.setOnFormatValueCallback(value -> String.format(DISTANCE_IN_FEET_FORMAT, value));
            headerViewHolder.telepathy.setValue(mTelepathyRange);
            headerViewHolder.telepathy.setOnValueChangeListener(mOnTelepathyRangeChanged);
            headerViewHolder.understandsBut.setText(mUnderstandsBut);
            headerViewHolder.understandsBut.addTextChangedListener(mOnUnderstandsButChanged);
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mItem = mValues.get(position - 1);
            itemViewHolder.mContentView.setText(itemViewHolder.mItem.getName());
            itemViewHolder.itemView.setOnClickListener(view -> {
                if (mOnClick != null) {
                    mOnClick.onItemCallback(itemViewHolder.mItem);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW_TYPE;
        }
        return ITEM_VIEW_TYPE;
    }

    public interface ItemCallback {
        void onItemCallback(Language language);
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final Stepper telepathy;
        public final EditText understandsBut;

        public HeaderViewHolder(FragmentEditLanguagesListHeaderBinding binding) {
            super(binding.getRoot());
            telepathy = binding.telepathy;
            understandsBut = binding.understandsBut;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView mContentView;
        public Language mItem;

        public ItemViewHolder(FragmentEditLanguagesListItemBinding binding) {
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
