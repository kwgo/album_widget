package com.jchip.album.common;

import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.data.PhotoData;

public class PhotoHelper {
    public static void setPhotoView(View view, PhotoData photo, boolean label, boolean frame) {
        setPhotoImage(view.findViewById(R.id.photo_image), photo);
        setPhotoScale(view.findViewById(R.id.photo_image), photo);
        if (label) {
            setPhotoFont(view.findViewById(R.id.photo_label), photo);
        }
        if (frame) {
            setPhotoFrame(view.findViewById(R.id.photo_container), photo);
            setPhotoFrame(view.findViewById(R.id.photo_frame), photo);
        }
    }

    public static void setPhotoImage(ImageView imageView, PhotoData photo) {
        setPhotoImage(imageView, photo, 0);
    }

    public static void setPhotoImage(ImageView imageView, PhotoData photo, int maxSize) {
        if (photo.getPhotoPath() != null && !photo.getPhotoPath().trim().isEmpty()) {
            Bitmap bitmap = AlbumHelper.loadBitmap(photo.getPhotoPath());
            if (bitmap != null) {
                bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), maxSize);
                imageView.setImageBitmap(bitmap);
                return;
            }
        }
        imageView.setImageBitmap(null);
    }

    public static void setPhotoScale(ImageView imageView, PhotoData photo) {
        ImageView.ScaleType[] scale = {
                ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.FIT_CENTER,
                ImageView.ScaleType.FIT_XY, ImageView.ScaleType.CENTER
        };
        imageView.setScaleType(scale[photo.getScaleIndex()]);
    }

    public static void setPhotoFont(TextView textView, PhotoData photo) {
        textView.setText(photo.getFontText());
        textView.setTextColor(photo.getFontColor());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, photo.getFontSize());
        setFontLocation(textView, photo);
    }

    public static void setPhotoFrame(View view, PhotoData photo) {
        view.setBackgroundResource(photo.getFrameIndex() > 0 ? photo.getFrameIndex() : R.drawable.frame_default);
    }

    public static void setPhotoText(TextView textView, PhotoData photo) {
        textView.setText(photo.getFontText());
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
}
