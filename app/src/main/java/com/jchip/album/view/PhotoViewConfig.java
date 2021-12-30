package com.jchip.album.view;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

public class PhotoViewConfig {
    public static final float DEFAULT_IMAGE_ZOOM = 0.5f;

    private static final Map<Integer, Integer> densitySizeFactors = new HashMap<Integer, Integer>() {{
        this.put(PhotoView.LAYER_ALBUM_PHOTO, 1);
        this.put(PhotoView.LAYER_FRAME_SETTING, 4);
        this.put(PhotoView.LAYER_FONT_SETTING, 4);
        this.put(PhotoView.WIDGET_ALBUM_PHOTO, 0);
        this.put(PhotoView.WIDGET_ALBUM_SETTING, 25);
        this.put(PhotoView.WIDGET_PHOTO_SETTING, 12);
    }};

    public static int getDensitySizeFactor(int layer) {
        Integer value = densitySizeFactors.get(layer);
        return value != null ? value : 0;
    }

    private static final Map<Integer, Float> fontSizeFactors = new HashMap<Integer, Float>() {{
        this.put(PhotoView.LAYER_ALBUM_PHOTO, 0.70f);
        this.put(PhotoView.LAYER_FRAME_SETTING, 0.45f);
        this.put(PhotoView.LAYER_FONT_SETTING, 0.45f);
        this.put(PhotoView.WIDGET_ALBUM_PHOTO, 0.85f);
        this.put(PhotoView.WIDGET_ALBUM_SETTING, 0.1f);
        this.put(PhotoView.WIDGET_PHOTO_SETTING, 0.21f);
    }};

    public static float getFontSizeFactor(int layer) {
        Float value = fontSizeFactors.get(layer);
        return value != null ? value : 1.0f;
    }

    private static final Map<Integer, Integer> imageGaps = new HashMap<Integer, Integer>() {{
        this.put(PhotoView.LAYER_ALBUM_PHOTO, 16);
        this.put(PhotoView.LAYER_FRAME_SETTING, 8);
        this.put(PhotoView.LAYER_FONT_SETTING, 8);
        this.put(PhotoView.WIDGET_ALBUM_PHOTO, 12);
        this.put(PhotoView.WIDGET_ALBUM_SETTING, 2);
        this.put(PhotoView.WIDGET_PHOTO_SETTING, 4);
    }};

    public static int getImageGap(int layer) {
        Integer value = imageGaps.get(layer);
        return value != null ? value : 0;
    }

    private static final Map<Integer, Integer> imageMaxWidths = new HashMap<Integer, Integer>() {{
        this.put(PhotoView.LAYER_ALBUM_PHOTO, getScreenWidth());
        this.put(PhotoView.LAYER_FRAME_SETTING, getScreenWidth());
        this.put(PhotoView.LAYER_FONT_SETTING, getScreenWidth());
        this.put(PhotoView.WIDGET_ALBUM_PHOTO, getScreenWidth());
        this.put(PhotoView.WIDGET_ALBUM_SETTING, getScreenWidth() / 5);
        this.put(PhotoView.WIDGET_PHOTO_SETTING, getScreenWidth() / 2);
    }};

    public static int getImageMaxWidth(int layer) {
        Integer value = imageMaxWidths.get(layer);
        return value != null ? (int) (value * DEFAULT_IMAGE_ZOOM) : 0;
    }

    private static final Map<Integer, Integer> imageMaxHeights = new HashMap<Integer, Integer>() {{
        this.put(PhotoView.LAYER_ALBUM_PHOTO, (int) (0.70 * getScreenHeight()));
        this.put(PhotoView.LAYER_FRAME_SETTING, dpToPx(220));
        this.put(PhotoView.LAYER_FONT_SETTING, dpToPx(220));
        this.put(PhotoView.WIDGET_ALBUM_PHOTO, (int) (0.70 * getScreenHeight()));
        this.put(PhotoView.WIDGET_ALBUM_SETTING, dpToPx(45));
        this.put(PhotoView.WIDGET_PHOTO_SETTING, dpToPx(100));
    }};

    public static int getImageMaxHeight(int layer) {
        Integer value = imageMaxHeights.get(layer);
        return value != null ? (int) (value * DEFAULT_IMAGE_ZOOM) : 0;
    }

    private static final Map<Integer, Boolean> defaultImageRotations = new HashMap<Integer, Boolean>() {{
        this.put(PhotoView.LAYER_ALBUM_PHOTO, true);
        this.put(PhotoView.LAYER_FRAME_SETTING, true);
        this.put(PhotoView.LAYER_FONT_SETTING, true);
        this.put(PhotoView.WIDGET_ALBUM_PHOTO, false);
        this.put(PhotoView.WIDGET_ALBUM_SETTING, false);
        this.put(PhotoView.WIDGET_PHOTO_SETTING, false);
    }};

    public static boolean getDefaultImageRotation(int layer) {
        Boolean value = defaultImageRotations.get(layer);
        return value != null ? value : true;
    }

    public static int getScreenWidth() {
        try {
            return Resources.getSystem().getDisplayMetrics().widthPixels;
        } catch (Exception ex) {
            return 0;
        }
    }

    public static int getScreenHeight() {
        try {
            return Resources.getSystem().getDisplayMetrics().heightPixels;
        } catch (Exception ex) {
            return 0;
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxTodp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static float getDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }


}
