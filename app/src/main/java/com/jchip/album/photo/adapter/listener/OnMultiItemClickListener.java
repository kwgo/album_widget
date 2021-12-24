package com.jchip.album.photo.adapter.listener;

/**
 * ViewHolder click event
 */

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface OnMultiItemClickListener {
    void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int viewPosition, int itemPosition);
}
