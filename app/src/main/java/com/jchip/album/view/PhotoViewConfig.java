package com.jchip.album.view;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.ImageView;

import com.jchip.album.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoViewConfig {
    public static final float DEFAULT_IMAGE_ZOOM = 0.95f;

    public static final int DEFAULT_FRAME_ID = R.drawable.frame_item_0;
    public static final int DEFAULT_PHOTO_ID = R.drawable.photo_default;

    public static final int DEFAULT_FONT_INDEX = 1;
    public static final int DEFAULT_FONT_LOCATION = 4;
    public static final int DEFAULT_FONT_SIZE = 120;

    public static final int LAYER_ALBUM_PHOTO = 0;
    public static final int LAYER_FRAME_SETTING = 1;
    public static final int LAYER_FONT_SETTING = 2;

    public static final int WIDGET_ALBUM_PHOTO = 3;
    public static final int WIDGET_ALBUM_SETTING = 4;
    public static final int WIDGET_PHOTO_SETTING = 5;

    public static final List<ImageView.ScaleType> scaleTypes = Arrays.asList(
            ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_XY, ImageView.ScaleType.CENTER
    );

    public static final List<Integer> fonts = Arrays.asList(
            R.font.niconne_regular, R.font.anton_regular, R.font.macondo_egular,
            R.font.abril_fatface_regular, R.font.ole_regular, R.font.wind_song_medium
    );

    public static final List<Integer> locations = Arrays.asList(
            Gravity.START | Gravity.TOP, Gravity.CENTER_HORIZONTAL | Gravity.TOP, Gravity.END | Gravity.TOP,
            Gravity.START | Gravity.CENTER_VERTICAL, Gravity.CENTER, Gravity.END | Gravity.CENTER_VERTICAL,
            Gravity.START | Gravity.BOTTOM, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, Gravity.END | Gravity.BOTTOM
    );

    private static final Map<Integer, Integer> densitySizeFactors = new HashMap<Integer, Integer>() {{
        this.put(LAYER_ALBUM_PHOTO, 1);
        this.put(LAYER_FRAME_SETTING, 4);
        this.put(LAYER_FONT_SETTING, 4);
        this.put(WIDGET_ALBUM_PHOTO, 0);
        this.put(WIDGET_ALBUM_SETTING, 25);
        this.put(WIDGET_PHOTO_SETTING, 12);
    }};

    public static int getDensitySizeFactor(int layer) {
        Integer value = densitySizeFactors.get(layer);
        return value != null ? value : 0;
    }

    private static final Map<Integer, Float> fontSizeFactors = new HashMap<Integer, Float>() {{
        this.put(LAYER_ALBUM_PHOTO, 0.70f);
        this.put(LAYER_FRAME_SETTING, 0.45f);
        this.put(LAYER_FONT_SETTING, 0.45f);
        this.put(WIDGET_ALBUM_PHOTO, 0.85f);
        this.put(WIDGET_ALBUM_SETTING, 0.1f);
        this.put(WIDGET_PHOTO_SETTING, 0.21f);
    }};

    public static float getFontSizeFactor(int layer) {
        Float value = fontSizeFactors.get(layer);
        return value != null ? value : 1.0f;
    }

    private static final Map<Integer, Integer> imageGaps = new HashMap<Integer, Integer>() {{
        this.put(LAYER_ALBUM_PHOTO, 16);
        this.put(LAYER_FRAME_SETTING, 8);
        this.put(LAYER_FONT_SETTING, 8);
        this.put(WIDGET_ALBUM_PHOTO, 12);
        this.put(WIDGET_ALBUM_SETTING, 2);
        this.put(WIDGET_PHOTO_SETTING, 4);
    }};

    public static int getImageGap(int layer) {
        Integer value = imageGaps.get(layer);
        return value != null ? dpToPx(value) : 0;
    }

    private static final Map<Integer, Integer> imageWidths = new HashMap<Integer, Integer>() {{
        this.put(LAYER_ALBUM_PHOTO, getScreenWidth());
        this.put(LAYER_FRAME_SETTING, getScreenWidth());
        this.put(LAYER_FONT_SETTING, getScreenWidth());
        this.put(WIDGET_ALBUM_PHOTO, getScreenWidth());
        this.put(WIDGET_ALBUM_SETTING, getScreenWidth() / 5);
        this.put(WIDGET_PHOTO_SETTING, getScreenWidth() / 2);
    }};

    public static int getImageWidth(int layer) {
        Integer value = imageWidths.get(layer);
        return value != null ? (int) (value * DEFAULT_IMAGE_ZOOM) : 0;
    }

    private static final Map<Integer, Integer> imageHeights = new HashMap<Integer, Integer>() {{
        this.put(LAYER_ALBUM_PHOTO, (int) (0.70 * getScreenHeight()));
        this.put(LAYER_FRAME_SETTING, dpToPx(220));
        this.put(LAYER_FONT_SETTING, dpToPx(220));
        this.put(WIDGET_ALBUM_PHOTO, (int) (0.70 * getScreenHeight()));
        this.put(WIDGET_ALBUM_SETTING, dpToPx(45));
        this.put(WIDGET_PHOTO_SETTING, dpToPx(100));
    }};

    public static int getImageHeight(int layer) {
        Integer value = imageHeights.get(layer);
        return value != null ? (int) (value * DEFAULT_IMAGE_ZOOM) : 0;
    }

    public static Rect getImageRect(int layer) {
        return new Rect(0,0, getImageWidth(layer), getImageHeight(layer));
    }

    private static final Map<Integer, Boolean> imageSolids = new HashMap<Integer, Boolean>() {{
        this.put(LAYER_ALBUM_PHOTO, false);
        this.put(LAYER_FRAME_SETTING, false);
        this.put(LAYER_FONT_SETTING, false);
        this.put(WIDGET_ALBUM_PHOTO, true);
        this.put(WIDGET_ALBUM_SETTING, true);
        this.put(WIDGET_PHOTO_SETTING, true);
    }};

    public static boolean isImageSolid(int layer) {
        Boolean value = imageSolids.get(layer);
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

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static Rect dpToPx(Rect rect) {
        float density = getDensity();
        return new Rect((int) (rect.left * density), (int) (rect.top * density), (int) (rect.right * density), (int) (rect.bottom * density));
    }

    public static Rect pxToDp(Rect rect) {
        float density = getDensity();
        return new Rect((int) (rect.left / density), (int) (rect.top / density), (int) (rect.right / density), (int) (rect.bottom / density));
    }

    public static float getDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }


}
