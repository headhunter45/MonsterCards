package com.majinnaibu.monstercards.ui.shared;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.majinnaibu.monstercards.R;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final Drawable icon;
    private final ColorDrawable background;
    private final Paint clearPaint;
    private final OnSwipeCallback mOnDelete;
    private final OnMoveCallback mOnMove;
    private final Context mContext;

    public SwipeToDeleteCallback(@NonNull Context context, OnSwipeCallback onDelete, OnMoveCallback onMove) {
        super(onMove == null ? 0 : ItemTouchHelper.UP | ItemTouchHelper.DOWN, onDelete == null ? 0 : ItemTouchHelper.LEFT);
        mOnDelete = onDelete;
        mOnMove = onMove;
        mContext = context;
        icon = ContextCompat.getDrawable(mContext, R.drawable.ic_delete_white_36);
        background = new ColorDrawable(context.getResources().getColor(R.color.red));
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder target
    ) {
        if (mOnMove != null) {
            int from = viewHolder.getAdapterPosition();
            int to = target.getAdapterPosition();
            return mOnMove.onMove(from, to);
        }
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (mOnDelete != null) {
            int position = viewHolder.getAdapterPosition();
            mOnDelete.onSwipe(position, direction);
        }
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

    public interface OnSwipeCallback {
        void onSwipe(int position, int direction);
    }

    public interface OnMoveCallback {
        boolean onMove(int from, int to);
    }
}
