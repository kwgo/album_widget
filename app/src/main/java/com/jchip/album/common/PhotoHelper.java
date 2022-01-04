package com.jchip.album.common;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.view.PhotoView;
import com.jchip.album.view.PhotoViewConfig;

public class PhotoHelper {
    // for app
    public static void setPhotoView(PhotoView photoView, View view) {
        setPhotoFrame(photoView, view.findViewById(R.id.photo_container), view.findViewById(R.id.photo_border), view.findViewById(R.id.photo_frame));
        //   setPhotoBorder(photoView, view.findViewById(R.id.photo_image));
        setPhotoBorder(photoView, view.findViewById(R.id.photo_border));
        setPhotoImage(photoView, view.findViewById(R.id.photo_image));
        setPhotoLabel(photoView, view.findViewById(R.id.photo_label));
    }

    public static void setPhotoFrame(PhotoView photoView, View containerView, View boardView, View frameView) {
        Rect padding = new Rect();
        Drawable drawable = photoView.getFrameDrawable(padding);
        if (drawable != null) {
            containerView.setBackground(drawable);
            frameView.setBackground(drawable);
            photoView.setPhotoPadding(padding);
        } else {
            int frameId = photoView.getFrameIndex();
            containerView.setBackgroundResource(frameId);
            frameView.setBackgroundResource(frameId);
            photoView.setPhotoPadding(new Rect(0, 0, boardView.getWidth(), boardView.getHeight()));
        }
        boardView.setVisibility(View.VISIBLE);
    }

    public static void setPhotoBorder(PhotoView photoView, View borderView) {
        borderView.setVisibility(photoView.isBorderOn() ? View.VISIBLE : View.GONE);
        if (photoView.isBorderOn()) {
            int border = photoView.getImageBorder();
            borderView.setPadding(border, border, border, border);
            // ((FrameLayout.LayoutParams) view.getLayoutParams()).setMargins(border, border, border, border);
        }
    }

    public static void setPhotoImage(PhotoView photoView, ImageView imageView) {
        if (photoView.isImageOn()) {
            Bitmap bitmap = photoView.getPhotoImage();
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static void setPhotoLabel(PhotoView photoView, TextView labelView) {
        labelView.setVisibility(View.GONE);
        if (!photoView.isFontEmpty()) {
            labelView.setText(photoView.getFontText());
            labelView.setTextColor(photoView.getFontColor());
            labelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, photoView.getFontTextSize());
            labelView.setTypeface(photoView.getFontFaceType());
            labelView.setGravity(photoView.getFontLocation());
            ((LinearLayout) labelView.getParent()).setGravity(photoView.getFontLocation());
            labelView.setVisibility(View.VISIBLE);
        }
    }

    // for widget
    public static void setPhotoView(PhotoView photoView, RemoteViews views) {
        setPhotoFrame(photoView, views, R.id.photo_container, R.id.photo_border, R.id.photo_frame);
        setPhotoBorder(photoView, views, R.id.photo_border);
        setPhotoImage(photoView, views, R.id.photo_image);
        setPhotoLabel(photoView, views, R.id.label_container, R.id.photo_label);
    }

    public static void setPhotoFrame(PhotoView photoView, RemoteViews views, int containerId, int boardId, int frameId) {
        views.setInt(containerId, "setBackgroundResource", photoView.getFrameIndex());
        views.setInt(frameId, "setBackgroundResource", photoView.getFrameIndex());
    }

    public static void setPhotoBorder(PhotoView photoView, RemoteViews views, int borderId) {
        Log.d("", "widget photoView.isBorderOn() = " + photoView.isBorderOn());
        views.setViewVisibility(borderId, photoView.isBorderOn() ? View.VISIBLE : View.INVISIBLE);
        if (photoView.isBorderOn()) {
            int border = photoView.getImageBorder();
            views.setViewPadding(borderId, border, border, border, border);
        }
        int border = photoView.getImageBorder();
        //views.setViewPadding(R.id.photo_image, border, border, border, border);
        views.setViewPadding(borderId, border, border, border, border);
        Log.d("", "widget this.border.border() = " + border);

    }

    public static void setPhotoImage(PhotoView photoView, RemoteViews views, int imageId) {
        Bitmap bitmap = photoView.getPhotoImage();
        if (bitmap != null) {
            views.setImageViewResource(imageId, 0);
            views.setImageViewBitmap(imageId, bitmap);
        } else {
            views.setImageViewBitmap(imageId, null);
            views.setImageViewResource(imageId, PhotoViewConfig.DEFAULT_PHOTO_ID);
        }
    }

    public static void setPhotoLabel(PhotoView photoView, RemoteViews views, int labelContainerId, int labelId) {
        views.setViewVisibility(R.id.photo_label, photoView.isFontEmpty() ? View.GONE : View.VISIBLE);
        if (!photoView.isFontEmpty()) {
            String text = photoView.getFontText();
            Typeface font = photoView.getFontFaceType();
            int color = photoView.getFontColor();
            int textSize = photoView.getFontTextSize();
            int location = photoView.getLocationIndex() % 3;
            Layout.Alignment alignment = location == 0 ? Layout.Alignment.ALIGN_NORMAL :
                    location == 2 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER;
            Bitmap bitmap = FontHelper.getTextBitmap(text, font, textSize, color, alignment);
            views.setImageViewBitmap(labelId, bitmap);
            views.setInt(labelContainerId, "setGravity", photoView.getFontLocation());
        }
    }
}
