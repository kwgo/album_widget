package com.jchip.album.common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.jchip.album.R;
import com.jchip.album.data.PhotoData;

import java.io.FileInputStream;

public class PhotoHelper {
    public static void setPhotoView(Context context, View view, PhotoData photo) {
        setPhotoFrame(view.findViewById(R.id.photo_container), photo);
        setPhotoFrame(view.findViewById(R.id.photo_frame), photo);
        setPhotoImage(view.findViewById(R.id.photo_image), photo);
        setPhotoScale(view.findViewById(R.id.photo_image), photo, true);
        setPhotoLabel(context, view.findViewById(R.id.photo_label), photo);
    }

    public static void setPhotoLook(Context context, View view, PhotoData photo, boolean label) {
        setPhotoFrameLook(view.findViewById(R.id.photo_container), photo);
        setPhotoFrameLook(view.findViewById(R.id.photo_frame), photo);
        setPhotoImageLook(view.findViewById(R.id.photo_image), photo);
        setPhotoScale(view.findViewById(R.id.photo_image), photo, false);
        if (label) {
            setPhotoLabel(context, view.findViewById(R.id.photo_label), photo);
        }
    }

    public static void setPhotoImage(ImageView imageView, PhotoData photo) {
        setPhotoImage(imageView, photo, 1600);
    }

    public static void setPhotoImageLook(ImageView imageView, PhotoData photo) {
        setPhotoImage(imageView, photo, 640);
    }

    public static void setPhotoImage(ImageView imageView, PhotoData photo, int maxSize) {
        if (photo.getPhotoPath() != null && !photo.getPhotoPath().trim().isEmpty()) {
            Bitmap bitmap = loadBitmap(photo.getPhotoPath());
            if (bitmap != null) {
                bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), maxSize);
                imageView.setImageResource(0);
                imageView.setImageBitmap(bitmap);
                return;
            }
        }
        imageView.setImageBitmap(null);
        imageView.setImageResource(R.drawable.photo_default);
    }

    public static void setPhotoScale(ImageView imageView, PhotoData photo, boolean gap) {
        ImageView.ScaleType[] scale = {
                ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.FIT_CENTER,
                ImageView.ScaleType.FIT_XY, ImageView.ScaleType.CENTER
        };
        int FIT_PADDING = gap ? 16 : 0;
        int gapBorder = scale[photo.getScaleIndex()] == ImageView.ScaleType.FIT_CENTER ? dpToPx(FIT_PADDING) : 0;
        imageView.setPadding(gapBorder, gapBorder, gapBorder, gapBorder);
        imageView.setScaleType(scale[photo.getScaleIndex()]);
    }

    public static void setPhotoLabel(Context context, TextView textView, PhotoData photo) {
        textView.setText(photo.getFontText());
        textView.setTextColor(photo.getFontColor());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, photo.getFontSize());
        setPhotoFont(context, textView, photo);
        setFontLocation(textView, photo);
    }

    public static void setPhotoFont(Context context, TextView textView, PhotoData photo) {
        int[] font = {
                R.font.niconne_regular, R.font.anton_regular, R.font.macondo_egular,
                R.font.frederickathe_great, R.font.ole_regular, R.font.wind_song_medium
        };
        int fontType = font[photo.getFontType() % font.length];
        Typeface typeface = fontType == 0 ? null : ResourcesCompat.getFont(context, fontType);
        textView.setTypeface(typeface);
    }


    public static void setPhotoFrame(View view, PhotoData photo) {
        view.setBackgroundResource(photo.getFrameIndex() > 0 ? photo.getFrameIndex() : R.drawable.frame_default);
    }

    public static void setPhotoFrameLook(View view, PhotoData photo) {
        view.setBackgroundResource(photo.getFrameLook() > 0 ? photo.getFrameLook() : R.drawable.frame_look_default);
    }

    public static void setFontLocation(TextView view, PhotoData photo) {
        int[] gravity = {
                Gravity.START | Gravity.TOP, Gravity.CENTER_HORIZONTAL | Gravity.TOP, Gravity.END | Gravity.TOP,
                Gravity.START | Gravity.CENTER_VERTICAL, Gravity.CENTER, Gravity.END | Gravity.CENTER_VERTICAL,
                Gravity.START | Gravity.BOTTOM, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, Gravity.END | Gravity.BOTTOM
        };
        view.setGravity(gravity[photo.getFontLocation() % gravity.length]);
        view.setGravity(gravity[photo.getFontLocation() % gravity.length]);
        ((LinearLayout) view.getParent()).setGravity(gravity[photo.getFontLocation() % gravity.length]);
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
}
