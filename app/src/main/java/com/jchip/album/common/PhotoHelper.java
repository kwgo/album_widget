package com.jchip.album.common;

import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.data.PhotoData;

public class PhotoHelper {
    public static void setPhotoView(View view, PhotoData photo, int imageId, int labelId, int containerId, int frameId) {
        if (imageId > 0) {
            setImagePhoto(view.findViewById(imageId), photo);
            setImageScale(view.findViewById(imageId), photo);
        }
        if (labelId > 0) {
            setPhotoFont(view.findViewById(labelId), photo);
        }
        if (containerId > 0 && frameId > 0) {
            setPhotoFrame(view.findViewById(containerId), photo);
            setPhotoFrame(view.findViewById(frameId), photo);
        }
    }

    public static void setImagePhoto(ImageView imageView, PhotoData photo) {
        setImagePhoto(imageView, photo, 0);
    }

    public static void setImagePhoto(ImageView imageView, PhotoData photo, int maxSize) {
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

    public static void setImageScale(ImageView imageView, PhotoData photo) {
        ImageView.ScaleType[] scaleTypies = new ImageView.ScaleType[]{
                ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.FIT_CENTER,
                ImageView.ScaleType.FIT_XY, ImageView.ScaleType.CENTER
        };
        imageView.setScaleType(scaleTypies[photo.getScaleIndex()]);
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

    public static void setFontLocation(View view, PhotoData photo) {
        int fontLocation = photo.getFontLocation();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(fontLocation % 2 == 0 ? RelativeLayout.ALIGN_PARENT_START : RelativeLayout.ALIGN_PARENT_END);
        layoutParams.addRule(fontLocation / 2 == 0 ? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);
    }
}
