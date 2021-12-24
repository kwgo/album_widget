package com.jchip.album.photo.adapter.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.jchip.album.photo.adapter.factory.IItemType;

public abstract class BaseViewHolder<T extends IItemType> extends RecyclerView.ViewHolder {
    private SparseArray<View> views;
    private View itemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.views = new SparseArray<>();
    }

    protected View getView(int redId) {
        View view = views.get(redId);
        if (view == null) {
            view = itemView.findViewById(redId);
            views.put(redId, view);
        }
        return view;
    }

    public abstract View[] getClickViews();

    public abstract View[] getLongClickViews();

    public abstract void bindViewData(Context context, T data, int itemPosition);
}
