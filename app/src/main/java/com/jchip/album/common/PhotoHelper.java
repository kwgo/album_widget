package com.jchip.album.common;

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
import com.jchip.album.view.PhotoViewConfig;

public class PhotoHelper {

    public static void setPhotoView(PhotoView photoView, View view) {
        if (photoView.isFrameOn()) {
            //setPhotoFrame(photoView, view.findViewById(R.id.photo_container), view.findViewById(R.id.photo_frame));
        }
        if (photoView.isImageOn()) {
            Log.d("", " **************** 1 111 ******************888");
            setPhotoImage(photoView, view.findViewById(R.id.photo_image));
        }
        if (photoView.isFontOn()) {
            setPhotoFont(photoView, view.findViewById(R.id.photo_label));
        }
    }

    public static void setPhotoImage(PhotoView photoView, ImageView imageView) {
        Log.d("","setPhotoImage ========================== photo helper use");
        Bitmap bitmap = photoView.getPhotoImage();
        if (bitmap != null) {
            Log.d("","setPhotoImage ==================ddddddd=========");
            imageView.setImageBitmap(bitmap);
            Log.d("","setPhotoImage ==================hhhhhh=========");
        }
        Log.d("","setPhotoImage =========gggddd=========");
        setPhotoScale(photoView, imageView);
        Log.d("","setPhotoImage ===============ttttt============");
    }

    public static void setPhotoScale(PhotoView photoView, ImageView imageView) {
        int gap = photoView.getImageGap();
        imageView.setPadding(gap, gap, gap, gap);
      //  imageView.setScaleType(photoView.getPhotoScale());
    }

    public static void setPhotoFont(PhotoView photoView, TextView textView) {
        textView.setVisibility(View.GONE);
        if (photoView.isFontEmpty()) {
            textView.setText(photoView.getFontText());
            textView.setTextColor(photoView.getFontColor());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, photoView.getFontTextSize());
            textView.setTypeface(photoView.getFontFaceType());
            textView.setGravity(photoView.getFontLocation());
            ((LinearLayout) textView.getParent()).setGravity(photoView.getFontLocation());
            textView.setVisibility(View.VISIBLE);
        }
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

    public static int dpToPx(int dp) {
        return PhotoViewConfig.dpToPx(dp);
    }

}
