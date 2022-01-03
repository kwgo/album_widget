package com.jchip.album.common;

import android.graphics.Bitmap;
import android.graphics.Rect;
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
        setPhotoFrame(photoView, view.findViewById(R.id.photo_container), view.findViewById(R.id.photo_board), view.findViewById(R.id.photo_frame));
        //   setPhotoBorder(photoView, view.findViewById(R.id.photo_image));
        setPhotoBorder(photoView, view.findViewById(R.id.photo_board));
        setPhotoImage(photoView, view.findViewById(R.id.photo_image));
        setPhotoFont(photoView, view.findViewById(R.id.photo_label));
    }

    public static void setPhotoImage(PhotoView photoView, ImageView imageView) {
        if (photoView.isImageOn()) {
            Bitmap bitmap = photoView.getPhotoImage();
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static void setPhotoBorder(PhotoView photoView, View view) {
        int gap = photoView.getImageGap();
        //   ((FrameLayout.LayoutParams) view.getLayoutParams()).setMargins(gap, gap, gap, gap);
        view.setPadding(gap, gap, gap, gap);
    }

    public static void setPhotoFont(PhotoView photoView, TextView textView) {
        textView.setVisibility(View.GONE);
        if (!photoView.isFontEmpty()) {
            textView.setText(photoView.getFontText());
            textView.setTextColor(photoView.getFontColor());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, photoView.getFontTextSize());
            textView.setTypeface(photoView.getFontFaceType());
            textView.setGravity(photoView.getFontLocation());
            ((LinearLayout) textView.getParent()).setGravity(photoView.getFontLocation());
            textView.setVisibility(View.VISIBLE);
        }
    }

    public static void setPhotoFrame(PhotoView photoView, View containerView, View boardView, View frameView) {
        Rect padding = new Rect();
        Drawable drawable = photoView.getFrameDrawable(padding);
        if (drawable != null) {
            containerView.setBackground(drawable);
            frameView.setBackground(drawable);
            Log.d("","photo helper drawable setPhotoPadding .. .. ..");
            photoView.setPhotoPadding(padding);
        } else {
            int frameId = photoView.getFrameIndex();
            containerView.setBackgroundResource(frameId);
            frameView.setBackgroundResource(frameId);
            Log.d("","photo helper resource setPhotoPadding .. .. ..");
            photoView.setPhotoPadding(new Rect(0, 0, boardView.getWidth(), boardView.getHeight()));
        }
        boardView.setVisibility(View.VISIBLE);
    }
}
