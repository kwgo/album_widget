package com.jchip.album.photo.adapter.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.jchip.album.photo.adapter.factory.IItemType;

//import android.support.v7.widget.RecyclerView;

//import com.rayzhang.android.rzalbum.adapter.factory.IItemType;

/*
 * Created by Ray on 2017/4/26.
 * BaseViewHolder
 */

public abstract class BaseViewHolder<T extends IItemType> extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mItemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mViews = new SparseArray<>();
    }

    protected View getView(int redId) {
        View view = mViews.get(redId);
        if (view == null) {
            view = mItemView.findViewById(redId);
            mViews.put(redId, view);
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
