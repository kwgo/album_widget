package com.jchip.album.photo.common;

import android.graphics.Color;

public final class PhotoConfig {
    public static final String SELECTED_PHOTOS = "selectedPhotos";

    public static final String PREVIEW_ADD_PHOTOS = "previewAddPhotos";
    public static final String PREVIEW_DELETE_PHOTOS = "previewDeletePhotos";
    public static final String PREVIEW_ALL_PHOTOS = "previewAllPhotos";
    public static final String PREVIEW_ITEM_POSITION = "previewItemPosition";
    public static final String PREVIEW_PICK_COLOR = "previewPickColor";
    public static final String PREVIEW_LIMIT_COUNT = "previewLimitCount";

    public static final int PICK_COLOR = Color.parseColor("#ffffc107");
    public static final int LIMIT_COUNT = 300;
    public static final int SPAN_COUNT = 3;
    public static final boolean SHOW_GIF = true;

    public static final int PHOTO_VIEW_GAP = 3;
    public static final int PHOTO_VIEW_BORDER = 24;

    // public static final String JPEG = "image/jpeg";
    public static final String GIF = "image/gif";
}
