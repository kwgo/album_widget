package com.jchip.album.photo.adapter.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jchip.album.R;
import com.jchip.album.photo.adapter.base.BaseViewHolder;
import com.jchip.album.photo.model.PhotoModel;
import com.jchip.album.photo.utils.DrawableUtils;
import com.jchip.album.photo.view.PhotoBorderView;
import com.jchip.album.photo.view.PhotoNumberView;

/**
 * PhotoViewHolder
 */

public class PhotoViewHolder extends BaseViewHolder<PhotoModel> {
    private ImageView photoImage;
    private TextView gifText;
    private PhotoBorderView photoBorderView;
    private PhotoNumberView photoNumberView;

    private PhotoViewHolder(View itemView) {
        super(itemView);
    }

    public PhotoViewHolder(int wh, View itemView) {
        super(itemView);

        photoImage = (ImageView) getView(R.id.grid_photo_image);
        gifText = (TextView) getView(R.id.grid_gif_text);
        photoBorderView = (PhotoBorderView) getView(R.id.grid_border_view);
        photoNumberView = (PhotoNumberView) getView(R.id.photo_number_view);

        if (wh > 0) {
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = wh;
            lp.height = wh;
            itemView.setLayoutParams(lp);
        }
    }

    @Override
    public View[] getClickViews() {
        return new View[]{photoBorderView, photoNumberView};
    }

    @Override
    public View[] getLongClickViews() {
        return new View[0];
    }

    @Override
    public void bindViewData(Context context, PhotoModel data, int itemPosition) {
        gifText.setText(context.getResources().getString(R.string.photo_type_gif));

//        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.ic_place_img_50dp)
//                .error(R.drawable.ic_place_img_50dp);
        Glide.with(context)
                .asBitmap()
                .load(data.getPhotoPath())
                //.apply(options)
                .into(photoImage);

        if (data.getPhotoPath().endsWith(".gif") || data.getPhotoPath().endsWith(".GIF")) {
            float radius = gifText.getWidth() / 2;
            gifText.setBackground(DrawableUtils.drawableOfRoundRect(Color.argb(200, 255, 255, 255), radius));
            gifText.setVisibility(View.VISIBLE);
        } else {
            gifText.setVisibility(View.INVISIBLE);
        }
        photoBorderView.setDraw(data.getPickNumber() > 0, data.getPickColor());
        photoNumberView.setNumber(data.getPickNumber());
        photoNumberView.setPickColor(data.getPickColor());
    }
}
