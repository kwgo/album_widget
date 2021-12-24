package com.jchip.album.photo.adapter.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jchip.album.R;
import com.jchip.album.photo.adapter.base.BaseViewHolder;
import com.jchip.album.photo.model.FolderModel;
import com.jchip.album.photo.utils.DrawableUtils;

import java.util.Locale;

/**
 * FolderViewHolder
 */

public class FolderViewHolder extends BaseViewHolder<FolderModel> {
    private RelativeLayout folderViewHolder;
    private ImageView imageView;
    private TextView folderText;
    private RadioButton radioButton;

    public FolderViewHolder(View itemView) {
        super(itemView);

        folderViewHolder = (RelativeLayout) getView(R.id.folder_view_holder);
        imageView = (ImageView) getView(R.id.folder_image);
        folderText = (TextView) getView(R.id.folder_text);
        radioButton = (RadioButton) getView(R.id.folder_radio_button);
    }

    @Override
    public View[] getClickViews() {
        return new View[]{folderViewHolder};
    }

    @Override
    public View[] getLongClickViews() {
        return new View[0];
    }

    @Override
    public void bindViewData(Context context, FolderModel data, int itemPosition) {
        Glide.with(context)
                .asBitmap()
                .load(data.getFolderPhotos().get(0).getPhotoPath())
                .into(imageView);

        folderText.setText(String.format(Locale.TAIWAN,
                context.getResources().getString(R.string.photo_folder_count),
                data.getFolderName(), data.getFolderPhotos().size()));
        radioButton.setChecked(data.isCheck());
        radioButton.setClickable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int[] colors = new int[]{data.getPickColor(), Color.argb(255, 77, 77, 77)};
            radioButton.setButtonTintList(DrawableUtils.drawableOfColorStateList(colors, android.R.attr.state_checked));
        }
    }
}
