package com.jchip.album.photo.adapter.factory;

import android.view.View;

import com.jchip.album.R;
import com.jchip.album.photo.adapter.base.BaseViewHolder;
import com.jchip.album.photo.adapter.viewholder.FolderViewHolder;
import com.jchip.album.photo.adapter.viewholder.PhotoViewHolder;

/**
 * layout factory
 */

public class ItemTypeFactory {
    private static volatile ItemTypeFactory itemTypeFactory;
    private int size;
    //private static final int PHOTO_ITEM = PhotoModel.PHOTO_ITEM;
    // private static final int FOLDER_ITEM = FolderModel.FOLDER_ITEM;

    public static ItemTypeFactory instance(int wh) {
        if (itemTypeFactory == null) itemTypeFactory = new ItemTypeFactory(wh);
        return itemTypeFactory;
    }

    private ItemTypeFactory(int size) {
        this.size = size;
    }

    @SuppressWarnings("unchecked")
    public <T extends IItemType> BaseViewHolder<T> createViewHolder(int type, View itemView) {
        if (type == R.layout.photo_grid_item) {
            return (BaseViewHolder<T>) new PhotoViewHolder(itemView, size);
        } else if (type == R.layout.photo_folder_item) {
            return (BaseViewHolder<T>) new FolderViewHolder(itemView);
        } else {
            return null;
        }
    }
}
