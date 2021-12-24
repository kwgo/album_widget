package com.jchip.album.photo.adapter.listener;

/**
 * Created by Ray on 2017/3/5.
 * 實作ViewHolder的點擊事件
 */

//import android.support.v7.widget.RecyclerView;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface OnMultiItemClickListener {
    void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int viewPosition, int itemPosition);
}
