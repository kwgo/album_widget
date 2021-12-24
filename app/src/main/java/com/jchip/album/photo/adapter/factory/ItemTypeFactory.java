package com.jchip.album.photo.adapter.factory;

import android.view.View;

import com.jchip.album.photo.adapter.base.BaseViewHolder;
import com.jchip.album.photo.adapter.viewholder.FolderViewHolder;
import com.jchip.album.photo.adapter.viewholder.PhotoViewHolder;
import com.jchip.album.photo.model.FolderModel;
import com.jchip.album.photo.model.PhotoModel;

/**
 * layout factory
 */

public class ItemTypeFactory {
    private static volatile ItemTypeFactory itemTypeFactory;
    private int wh;
    private static final int PHOTO_ITEM = PhotoModel.PHOTO_ITEM;
    private static final int FOLDER_ITEM = FolderModel.FOLDER_ITEM;

    public static ItemTypeFactory instance(int wh) {
        if (itemTypeFactory == null) itemTypeFactory = new ItemTypeFactory(wh);
        return itemTypeFactory;
    }

    private ItemTypeFactory(int wh) {
        this.wh = wh;
    }

    @SuppressWarnings("unchecked")
    public <T extends IItemType> BaseViewHolder<T> createViewHolder(int type, View itemView) {
        if (type == PHOTO_ITEM) {
            return (BaseViewHolder<T>) new PhotoViewHolder(wh, itemView);
        } else if (type == FOLDER_ITEM) {
            return (BaseViewHolder<T>) new FolderViewHolder(itemView);
        } else {
            return null;
        }
    }
}
