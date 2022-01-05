package com.jchip.album.view;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.common.FontHelper;

public class PhotoViewHelper {
    // for app
    public static void setPhotoView(PhotoView photoView, View view) {
        setPhotoFrame(photoView, view, R.id.photo_container, R.id.photo_frame, R.id.photo_board);
        setPhotoImage(photoView, view, R.id.photo_image);
        setPhotoLabel(photoView, view, R.id.photo_label);
        setPhotoBorder(photoView, view, R.id.photo_board, R.id.photo_border);
    }

    public static void setPhotoFrame(PhotoView photoView, View view, int containerId, int frameId, int boardId) {
        Rect padding = new Rect();
        Drawable drawable = photoView.getFrameDrawable(padding);
        if (drawable != null) {
            view.findViewById(containerId).setBackground(drawable);
            view.findViewById(frameId).setBackground(drawable);
            photoView.setPhotoPadding(padding);
        } else {
            view.findViewById(containerId).setBackgroundResource(photoView.getFrameIndex());
            view.findViewById(frameId).setBackgroundResource(photoView.getFrameIndex());
            photoView.setPhotoPadding(new Rect(0, 0, view.findViewById(boardId).getWidth(), view.findViewById(boardId).getHeight()));
        }
    }

    public static void setPhotoBorder(PhotoView photoView, View view, int boardId, int borderId) {
        int border = photoView.isFullSize() ? 0 : photoView.getImageBorder();
        view.findViewById(borderId).setPadding(border, border, border, border);
        view.findViewById(boardId).setVisibility(photoView.isFullSize() ? View.INVISIBLE : View.VISIBLE);
    }

    public static void setPhotoImage(PhotoView photoView, View view, int imageId) {
        if (photoView.isImageOn()) {
            Bitmap bitmap = photoView.getPhotoImage();
            if (bitmap != null) {
                ((ImageView) view.findViewById(imageId)).setImageBitmap(bitmap);
            }
        }
    }

    public static void setPhotoLabel(PhotoView photoView, View view, int labelId) {
        TextView labelView = view.findViewById(labelId);
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
        setPhotoFrame(photoView, views, R.id.photo_container, R.id.photo_frame, R.id.photo_board);
        setPhotoImage(photoView, views, R.id.photo_image);
        setPhotoLabel(photoView, views, R.id.label_container, R.id.photo_label);
        setPhotoBorder(photoView, views, R.id.photo_board, R.id.photo_border);
    }

    public static void setPhotoFrame(PhotoView photoView, RemoteViews views, int containerId, int frameId, int boardId) {
        views.setInt(containerId, "setBackgroundResource", photoView.getFrameIndex());
        views.setInt(frameId, "setBackgroundResource", photoView.getFrameIndex());
    }

    public static void setPhotoBorder(PhotoView photoView, RemoteViews views, int boardId, int borderId) {
        int border = photoView.isFullSize() ? 0 : photoView.getImageBorder();
        views.setViewPadding(borderId, border, border, border, border);
        views.setViewVisibility(boardId, photoView.isFullSize() ? View.INVISIBLE : View.VISIBLE);
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
