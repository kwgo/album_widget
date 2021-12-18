package com.jchip.album.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.jchip.album.R;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.common.ImageHelper;
import com.jchip.album.data.PhotoData;

import java.util.Random;

public class WidgetPhotoView {

    public static final String ACTION_APP = "actionApp";
    public static final String ACTION_SETTING = "actionSetting";
    public static final String ACTION_NEXT = "actionNext";
    public static final String ACTION_TOAST = "actionToast";

    public static final String WIDGET_ITEM = "widgetItem";
    public static final String WIDGET_TEXT = "widgetText";

    private Context context;
    private PhotoData photoData;
    private RemoteViews views;
    private int appWidgetId;

    private static Random random;

    static {
        random = random != null ? random : new Random();
    }

    public WidgetPhotoView(Context context, RemoteViews views, PhotoData photoData) {
        this.context = context;
        this.views = views;
        this.photoData = photoData;
    }

    public void updateView() {
        this.setPhotoView();

//        Map<String, String[]> info = MainHelper.getISOInfo();
//        int index = this.random.nextInt(info.size());
//        String item = String.valueOf(info.keySet().toArray()[index]);
//        String[] iso = info.get(item);
//        this.setTextView(R.id.widget_title, FallUtility.getSourceText(this.context, item, "string", "short"));
//        this.setTextView(R.id.widget_marker, iso[FallHelper.OFFICIAL]);
//        this.setTextView(R.id.widget_symbol, iso[FallHelper.ALPHA_2] + " " + iso[FallHelper.CURRENCY]);
//        this.setTextView(R.id.widget_detail, FallUtility.getSourceText(this.context, item, "string", "capital"));
//        this.setTextView(R.id.widget_patch, ("(") + iso[FallHelper.POPULATION] + ")");
//
//        int orientation = context.getResources().getConfiguration().orientation;
//        boolean isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE;
//        this.setImageView(isLandscape ? R.id.widget_image_landscape : R.id.widget_image_portrait, FallUtility.getSourceId(this.context, item, "drawable", "flag"));
//        this.setVisibility(isLandscape ? R.id.widget_image_landscape : R.id.widget_image_portrait, true);
//        this.setVisibility(isLandscape ? R.id.widget_image_portrait : R.id.widget_image_landscape, false);
//
//        intent.putExtra(WIDGET_ITEM, item);
//        intent.putExtra(WIDGET_TEXT, FallUtility.getSourceText(this.context, item, "string", "short"));
//
//        this.setViewAction(R.id.widget_view, ACTION_NEXT);
//        this.setViewAction(isLandscape ? R.id.widget_image_landscape : R.id.widget_image_portrait, ACTION_APP);
    }

    public void setPhotoView() {
        //this.setImageScale(R.id.photo_image, this.photoData);
        this.setScalePhoto(this.photoData);

        this.setPhotoFont(R.id.photo_label, this.photoData);

        this.setPhotoFrame(R.id.photo_container, this.photoData);
        this.setPhotoFrame(R.id.photo_frame, this.photoData);
    }

    public void setScalePhoto(PhotoData photo) {
        this.views.setViewVisibility(R.id.photo_image_0, View.INVISIBLE);
        this.views.setViewVisibility(R.id.photo_image_1, View.INVISIBLE);
        this.views.setViewVisibility(R.id.photo_image_2, View.INVISIBLE);
        this.views.setViewVisibility(R.id.photo_image_3, View.INVISIBLE);

        int scaleIndex = photo.getScaleIndex();
        int photoViewId = scaleIndex == 3 ? R.id.photo_image_3 :
                scaleIndex == 2 ? R.id.photo_image_2 :
                        scaleIndex == 1 ? R.id.photo_image_1 : R.id.photo_image_0;
        this.setPhotoImage(photoViewId, photo);
    }


    public void setPhotoImage(int photoViewId, PhotoData photo) {
        Bitmap bitmap = null;
        if (photo.getPhotoPath() != null && !photo.getPhotoPath().trim().isEmpty()) {
            bitmap = AlbumHelper.loadBitmap(photo.getPhotoPath());
            if (bitmap != null) {
                bitmap = ImageHelper.convertBitmap(bitmap, 1f, photo.getRotationIndex(), photo.getFlipIndex(), 0);
            }
        }
        this.views.setImageViewBitmap(photoViewId, bitmap);
        this.views.setViewVisibility(photoViewId, View.VISIBLE);
    }

    public void setImageScale(int imageViewId, PhotoData photo) {
        ImageView.ScaleType[] scales = new ImageView.ScaleType[]{
                ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.FIT_CENTER,
                ImageView.ScaleType.FIT_XY, ImageView.ScaleType.CENTER
        };
        //      this.views.
        //this.views.setInt(imageViewId, "setScaleType", scales[photo.getScaleIndex()].ordinal());
        // imageView.setScaleType(scaleTypies[photo.getScaleIndex()]);
    }

    public void setPhotoFont(int textViewId, PhotoData photo) {
        this.views.setTextViewText(textViewId, photo.getFontText());
        this.views.setTextColor(textViewId, photo.getFontColor());
        this.views.setTextViewTextSize(textViewId, TypedValue.COMPLEX_UNIT_PX, photo.getFontSize());
        this.setFontLocation(textViewId, photo);
    }

    public void setPhotoFrame(int imageViewId, PhotoData photo) {
        int frameIndex = photo.getFrameIndex() > 0 ? photo.getFrameIndex() : R.drawable.frame_default;
        this.views.setInt(imageViewId, "setBackgroundResource", frameIndex);
        // view.setBackgroundResource(photo.getFrameIndex() > 0 ? photo.getFrameIndex() : R.drawable.frame_default);
    }

//    public void setPhotoText(TextView textView, PhotoData photo) {
//        textView.setText(photo.getFontText());
//    }

    public void setFontLocation(int textViewId, PhotoData photo) {
        int fontLocation = photo.getFontLocation();
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
//        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
//        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
//        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
//        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        layoutParams.addRule(fontLocation % 2 == 0 ? RelativeLayout.ALIGN_PARENT_START : RelativeLayout.ALIGN_PARENT_END);
//        layoutParams.addRule(fontLocation / 2 == 0 ? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.ALIGN_PARENT_BOTTOM);
        // this.views.setViewLayoutHeight();
        //view.setLayoutParams(layoutParams);
    }

//    public final void setTextView(int textViewId, String text) {
//        this.views.setTextViewText(textViewId, text);
//    }
//
//    public final void setImagePhoto(int imageViewId, int imageSourceId) {
//        this.views.setImageViewResource(imageViewId, imageSourceId);
//    }

//    public void setVisibility(int viewId, boolean isOn) {
//        this.views.setViewVisibility(viewId, isOn ? View.VISIBLE : View.GONE);
//    }
//    private void setBackground(Context context, RemoteViews remoteViews) {
//        String background = Utils.getStringValue(context, R.string.pref_widget_backgroundcolor_key,
//                R.string.pref_widget_backgroundcolor_default);
//
//        if (!TextUtils.isEmpty(background)) {
//            int identifier = context.getResources().getIdentifier(
//                    background, "drawable", context.getPackageName());
//            remoteViews.setImageViewResource(R.id.iv_widget_background, identifier);
//        }
//    }
}
