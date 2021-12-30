package com.jchip.album.photo.adapter.common;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecycleItemDecoration
 */
public class RecycleItemDecoration extends RecyclerView.ItemDecoration {
    private static final int NONE = Integer.MAX_VALUE;
    public static final int BOTH = Integer.MAX_VALUE - 1;
    public static final int TOP_LEFT = Integer.MAX_VALUE - 2;
    public static final int BOTTOM_RIGHT = Integer.MAX_VALUE - 3;
    private int dividerSize;
    private int linearMode = NONE;
    private Paint paint;

    public RecycleItemDecoration(int dividerSize, int dividerColor) {
        this.dividerSize = dividerSize;
        initPaint(dividerColor);
    }

    public RecycleItemDecoration(int dividerSize, int dividerColor, int linearMode) {
        this.dividerSize = dividerSize;
        this.linearMode = linearMode;
        initPaint(dividerColor);
    }

    private void initPaint(int dividerColor) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(dividerColor);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            setGridItemOutRect(outRect, view, parent);
        } else if (manager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) manager).getOrientation() == LinearLayoutManager.VERTICAL) {
                setVerticalItemOutRect(outRect, view, parent);
            } else {
                setHorizontalItemOutRect(outRect, view, parent);
            }
        }
    }

    private void setVerticalItemOutRect(Rect outRect, View view, RecyclerView parent) {
        int count = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        switch (linearMode) {
            case NONE:
                // not first item
                if (position != count - 1) outRect.set(0, 0, 0, dividerSize);
                break;
            case BOTH:
                if (position == 0) {
                    // first item
                    outRect.set(0, dividerSize, 0, dividerSize);
                } else {
                    outRect.set(0, 0, 0, dividerSize);
                }
                break;
            case TOP_LEFT:
                if (position == 0) {
                    outRect.set(0, dividerSize, 0, dividerSize);
                } else if (position != count - 1) {
                    outRect.set(0, 0, 0, dividerSize);
                }
                break;
            case BOTTOM_RIGHT:
                outRect.set(0, 0, 0, dividerSize);
                break;
        }
    }

    private void setHorizontalItemOutRect(Rect outRect, View view, RecyclerView parent) {
        int count = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        switch (linearMode) {
            case NONE:
                if (position != count - 1) outRect.set(0, 0, dividerSize, 0);
                break;
            case BOTH:
                if (position == 0) {
                    outRect.set(dividerSize, 0, dividerSize, 0);
                } else {
                    outRect.set(0, 0, dividerSize, 0);
                }
                break;
            case TOP_LEFT:
                if (position == 0) {
                    outRect.set(dividerSize, 0, dividerSize, 0);
                } else if (position != count - 1) {
                    outRect.set(0, 0, dividerSize, 0);
                }
                break;
            case BOTTOM_RIGHT:
                outRect.set(0, 0, dividerSize, 0);
                break;
        }
    }

    private void setGridItemOutRect(Rect outRect, View view, RecyclerView parent) {
        int right = dividerSize;
        int bottom = dividerSize;
        if (isLastSpan(view, parent)) {
            right = 0;
        }

        if (isLastRow(view, parent)) {
            bottom = 0;
        }
        outRect.set(0, 0, right, bottom);
    }

    private Boolean isLastRow(View view, RecyclerView parent) {
        // bottom item
        int count = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        return (count - position - 1) < spanCount;
    }

    private Boolean isLastSpan(View view, RecyclerView parent) {
        // right item
        int position = parent.getChildAdapterPosition(view);
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        return position % spanCount == (spanCount - 1);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            drawVerticalDivider(c, parent);
            drawHorizontalDivider(c, parent);
        } else if (manager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) manager).getOrientation() == LinearLayoutManager.VERTICAL) {
                drawVerticalDivider(c, parent);
            } else {
                drawHorizontalDivider(c, parent);
            }
        }
    }

    private void drawVerticalDivider(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (i == 0 && linearMode == BOTH || i == 0 && linearMode == TOP_LEFT) {
                // first item and LinearMode are BOTH or TOP_LEFT
                int top = parent.getPaddingTop() + params.topMargin;
                int bottom = top + dividerSize;
                c.drawRect(left, top, right, bottom, paint);
            }
            // other item
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + dividerSize;
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (i == 0 && linearMode == BOTH || i == 0 && linearMode == TOP_LEFT) {
                // first item and LinearMode are BOTH or TOP_LEFT
                int left = parent.getPaddingLeft() + params.leftMargin;
                int right = left + dividerSize;
                c.drawRect(left, top, right, bottom, paint);
            }
            int left = child.getRight() + params.rightMargin;
            int right = left + dividerSize;
            c.drawRect(left, top, right, bottom, paint);
        }
    }
}
