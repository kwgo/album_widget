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

    // 實現view的點擊事件
    public abstract View[] getClickViews();

    // 實現view的長按點擊事件
    public abstract View[] getLongClickViews();

    // 綁定資料
    public abstract void bindViewData(Context context, T data, int itemPosition);
}
