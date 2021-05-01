package com.majinnaibu.monstercards.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.library.LibraryFragment;
import com.majinnaibu.monstercards.ui.library.LibraryFragmentDirections;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MonsterListRecyclerViewAdapter extends RecyclerView.Adapter<MonsterListRecyclerViewAdapter.ViewHolder> {
    public interface ItemCallback {
        void onItem(Monster monster);
    }

    // TODO: Replace SimpleItemRecyclerViewAdapter with something better like MonsterListRecyclerViewAdapter that can be reused in search

    private final LibraryFragment mParentActivity;
    private List<Monster> mValues;
    private final boolean mTwoPane;
    private final Context mContext;
    private final ItemCallback mOnDelete;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Monster monster = (Monster) view.getTag();
            // TODO: I would like to call navigateToMonsterDetail(item.id) here
            if (mTwoPane) {
                // TODO: Figure out how to navigate to a MonsterDetailFragment when in two pane view.
//                    Bundle arguments = new Bundle();
//                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, monster.id.toString());
//                    ItemDetailFragment fragment = new ItemDetailFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit();
            } else {
                NavDirections action = LibraryFragmentDirections.actionNavigationLibraryToNavigationMonster(monster.id.toString());
                Navigation.findNavController(view).navigate(action);
            }
        }
    };

    public MonsterListRecyclerViewAdapter(LibraryFragment parent,
                                          Flowable<List<Monster>> itemsObservable,
                                          ItemCallback onDelete,
                                          boolean twoPane) {
        mValues = new ArrayList<>();
        mParentActivity = parent;
        mTwoPane = twoPane;
        mContext = parent.getContext();
        mOnDelete = onDelete;

        itemsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monsters -> {
                    mValues = monsters;
                    notifyDataSetChanged();
                });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.monster_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mIdView.setText(mValues.get(position).id.toString().substring(0, 6));
        holder.mContentView.setText(mValues.get(position).name);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public Context getContext() {
        return mContext;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.id_text);
            mContentView = view.findViewById(R.id.content);
        }
    }

}
