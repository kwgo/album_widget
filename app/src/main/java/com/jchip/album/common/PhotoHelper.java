package com.jchip.album.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.view.PhotoView;

public class PhotoHelper {

    public static void setPhotoView(PhotoView photoView, View view) {
        if (photoView.isFrameOn()) {
            setPhotoFrame(photoView, view.findViewById(R.id.photo_container), view.findViewById(R.id.photo_frame));
        }
        if (photoView.isImageOn()) {
            setPhotoImage(photoView, view.findViewById(R.id.photo_image));
        }
        if (photoView.isFontOn()) {
            setPhotoLabel(photoView, view.findViewById(R.id.photo_label));
        }
    }

    public static void setPhotoImage(PhotoView photoView, ImageView imageView) {
        Bitmap bitmap = photoView.getPhotoImage();
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        setPhotoScale(photoView, imageView);
    }


    public static void setPhotoScale(PhotoView photoView, ImageView imageView) {
        int gap = dpToPx(photoView.getImageGap());
        imageView.setPadding(gap, gap, gap, gap);
        imageView.setScaleType(photoView.getPhotoScale());
    }

    public static void setPhotoLabel(PhotoView photoView, TextView textView) {
        Log.d("","setPhotoLabel   =====   "+photoView.getFontText());
        textView.setVisibility(View.GONE);
        if (!photoView.isFontEmpty()) {
            Log.d("","setPhotoLabel  xxxx =====   "+photoView.getFontText());
            textView.setText(photoView.getFontText());
            textView.setTextColor(photoView.getFontColor());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, photoView.getFontSize());
            setPhotoFont(photoView, textView);
            setFontLocation(photoView, textView);
            textView.setVisibility(View.VISIBLE);
        }
    }

    public static void setPhotoFont(PhotoView photoView, TextView textView) {
        textView.setTypeface(photoView.getFontFaceType());
    }


    public static void setPhotoFrame(PhotoView photoView, View containerView, View frameView) {
        Drawable drawable = photoView.getFrameDrawable();
        if (drawable != null) {
            containerView.setBackground(drawable);
            frameView.setBackground(drawable);
        } else {
            int frameId = photoView.getFrameIndex();
            containerView.setBackgroundResource(frameId);
            frameView.setBackgroundResource(frameId);
        }
    }

    public static void setFontLocation(PhotoView photoView, TextView textView) {
        int fontLocation = photoView.getFontLocation();
        textView.setGravity(fontLocation);
        ((LinearLayout) textView.getParent()).setGravity(fontLocation);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
