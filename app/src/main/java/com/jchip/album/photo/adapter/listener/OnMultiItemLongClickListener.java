package com.jchip.album.photo.adapter.listener;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface OnMultiItemLongClickListener {
    void onItemLongClick(RecyclerView.ViewHolder viewHolder, View view, int viewPosition, int itemPosition);
}
