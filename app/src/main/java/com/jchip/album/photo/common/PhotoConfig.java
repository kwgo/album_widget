package com.jchip.album.photo.common;

import android.graphics.Color;

public final class PhotoConfig {
    public static final String RESULT_PHOTOS = "RESULT_PHOTOS";

    public static final String PREVIEW_ADD_PHOTOS = "PREVIEW_ADD_PHOTOS";
    public static final String PREVIEW_DELETE_PHOTOS = "PREVIEW_DELETE_PHOTOS";
    public static final String PREVIEW_ALL_PHOTOS = "PREVIEW_ALL_PHOTOS";
    public static final String PREVIEW_ITEM_POSITION = "PREVIEW_ITEM_POSITION";
    public static final String PREVIEW_PICK_COLOR = "PREVIEW_PICK_COLOR";
    public static final String PREVIEW_LIMIT_COUNT = "PREVIEW_LIMIT_COUNT";

    public static final int DEFAULT_PICK_COLOR = Color.parseColor("#ffffc107");
    public static final int DEFAULT_LIMIT_COUNT = 300;
    public static final int DEFAULT_SPAN_COUNT = 3;
    public static final boolean DEFAULT_SHOW_GIF = true;

    // public static final String JPEG = "image/jpeg";
    public static final String GIF = "image/gif";
}
