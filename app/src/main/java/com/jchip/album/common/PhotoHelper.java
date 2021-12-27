package com.jchip.album.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.jchip.album.R;
import com.jchip.album.data.PhotoData;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

public class PhotoHelper {
    public static void setPhotoView(Context context, View view, PhotoData photo, boolean frame, boolean image, boolean label) {
        setPhotoFrame(view.findViewById(R.id.photo_container), photo, frame);
        setPhotoFrame(view.findViewById(R.id.photo_frame), photo, frame);
        if (image) {
            setPhotoImage(view.findViewById(R.id.photo_image), photo);
            setPhotoScale(view.findViewById(R.id.photo_image), photo, frame);
        }
        if (label) {
            setPhotoLabel(context, view.findViewById(R.id.photo_label), photo);
        }
    }

    public static void setPhotoImage(ImageView imageView, PhotoData photo) {
        setPhotoImage(imageView, photo, getScreenHeight());
    }

    public static void setPhotoImageLook(ImageView imageView, PhotoData photo) {
        setPhotoImage(imageView, photo, getScreenHeight() / 2);
    }

    public static void setPhotoImage(ImageView imageView, PhotoData photo, int maxSize) {
        Bitmap bitmap = loadPhotoImage(imageView, photo, maxSize);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    public static Bitmap loadPhotoImage(ImageView imageView, PhotoData photo, int maxSize) {
        Bitmap bitmap = null;
        if (photo.getPhotoPath() != null && !photo.getPhotoPath().trim().isEmpty()) {
            bitmap = loadBitmap(photo.getPhotoPath());
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(imageView.getResources(), R.drawable.photo_default);
        }
        if (bitmap != null) {
            bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), maxSize);
        }
        return bitmap;
    }

    public static void setPhotoScale(ImageView imageView, PhotoData photo, boolean gap) {
        ImageView.ScaleType[] scale = {
                ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.FIT_CENTER,
                ImageView.ScaleType.FIT_XY, ImageView.ScaleType.CENTER
        };
        int FIT_PADDING = dpToPx(gap ? 16 : 2);
        int gapBorder = scale[photo.getScaleIndex()] == ImageView.ScaleType.FIT_CENTER ? FIT_PADDING : 0;
        imageView.setPadding(gapBorder, gapBorder, gapBorder, gapBorder);
        imageView.setScaleType(scale[photo.getScaleIndex()]);
    }

    public static void setPhotoLabel(Context context, TextView textView, PhotoData photo) {
        textView.setVisibility(View.INVISIBLE);
        if (photo.getFontText() != null && !photo.getFontText().trim().isEmpty()) {
            textView.setText(photo.getFontText());
            textView.setTextColor(photo.getFontColor());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, photo.getFontSize());
            setPhotoFont(context, textView, photo);
            setFontLocation(textView, photo);
            textView.setVisibility(View.VISIBLE);
            ;
        }
    }

    public static void setPhotoFont(Context context, TextView textView, PhotoData photo) {
        textView.setTypeface(getFontTypeface(context, photo.getFontType()));
    }


    public static void setPhotoFrame(View view, PhotoData photo, boolean frame) {
        int frameId = frame ? photo.getFrameIndex() : photo.getFrameLook();
        frameId = frameId > 0 ? frameId : (frame ? R.drawable.frame_default : R.drawable.frame_look_default);
        view.setBackgroundResource(frameId);
    }

    public static void setFontLocation(TextView view, PhotoData photo) {
        view.setGravity(photo.getFontLocation());
        ((LinearLayout) view.getParent()).setGravity(photo.getFontLocation());
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static Bitmap loadBitmap(String path) {
        try (FileInputStream inputStream = new FileInputStream(path)) {
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception ex) {
        }
        return null;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static Typeface getFontTypeface(Context context, int fontId) {
        try {
            return ResourcesCompat.getFont(context, fontId);
        } catch (Exception ex) {
        }
        return null;
    }

    public static int getFontIndex(int fontId) {
        List<Integer> fonts = getFonts();
        return fonts.indexOf(fontId) < 0 ? 0 : fonts.indexOf(fontId);
    }

    public static List<Integer> getFonts() {
        return Arrays.asList(R.font.niconne_regular, R.font.anton_regular, R.font.macondo_egular,
                R.font.abril_fatface_regular, R.font.ole_regular, R.font.wind_song_medium);
    }
}
