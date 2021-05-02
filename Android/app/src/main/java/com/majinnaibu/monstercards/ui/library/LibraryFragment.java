package com.majinnaibu.monstercards.ui.library;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.majinnaibu.monstercards.R;
import com.majinnaibu.monstercards.data.MonsterRepository;
import com.majinnaibu.monstercards.models.Monster;
import com.majinnaibu.monstercards.ui.MCFragment;
import com.majinnaibu.monstercards.ui.MonsterListRecyclerViewAdapter;
import com.majinnaibu.monstercards.utils.Logger;

import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LibraryFragment extends MCFragment {
    // TODO: TOM: rename MonsterFragment MonsterDetailFragment
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_library, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Monster monster = new Monster();
            monster.name = "Unnamed Monster";
            MonsterRepository repository = this.getMonsterRepository();
            repository.addMonster(monster)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Snackbar.make(
                                getView(),
                                String.format("%s created", monster.name),
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", (_view) -> {
                                    navigateToMonsterDetail(monster.id);
                                })
                                .show();
                    }, throwable -> {
                        Logger.logError("Error creating monster", throwable);
                        Snackbar.make(getView(), "Failed to create monster", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    });
        });

        RecyclerView recyclerView = root.findViewById(R.id.monster_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        return root;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        MonsterRepository repository = this.getMonsterRepository();
        boolean mTwoPane = false;
        MonsterListRecyclerViewAdapter adapter = new MonsterListRecyclerViewAdapter(
                this,
                repository.getMonsters(),
                (monster) -> {
                    repository
                            .deleteMonster(monster)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> {
                                Logger.logDebug("deleted");
                            }, Logger::logError);
                },
                mTwoPane);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteMonsterCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    protected void navigateToMonsterDetail(UUID monsterId) {
        NavDirections action = LibraryFragmentDirections.actionNavigationLibraryToNavigationMonster(monsterId.toString());
        Navigation.findNavController(getView()).navigate(action);
    }

    public static class SwipeToDeleteMonsterCallback extends ItemTouchHelper.SimpleCallback {
        private final MonsterListRecyclerViewAdapter mAdapter;
        private final Drawable icon;
        private final ColorDrawable background;
        private final Paint clearPaint;

        public SwipeToDeleteMonsterCallback(MonsterListRecyclerViewAdapter adapter) {
            super(0, ItemTouchHelper.LEFT);
            mAdapter = adapter;
            icon = ContextCompat.getDrawable(mAdapter.getContext(), R.drawable.ic_delete_white_36);
            background = new ColorDrawable(Color.RED);
            clearPaint = new Paint();
            clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.deleteItem(position);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;
            int itemHeight = itemView.getBottom() - itemView.getTop();
            boolean isCancelled = dX == 0 && !isCurrentlyActive;

            if (isCancelled) {
                c.drawRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom(), clearPaint);
                return;
            }
            // Draw the red delete background
            background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            background.draw(c);

            // Calculate position of delete icon
            int iconHeight = icon.getIntrinsicHeight();
            int iconWidth = icon.getIntrinsicWidth();
            int iconTop = itemView.getTop() + (itemHeight - iconHeight) / 2;
            int iconMargin = (itemHeight - iconHeight) / 2;
            int iconLeft = itemView.getRight() - iconMargin - iconWidth;
            int iconRight = itemView.getRight() - iconMargin;
            int iconBottom = iconTop + iconHeight;

            // Draw the icon
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            icon.draw(c);

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
