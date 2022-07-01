package com.jchip.album;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.jchip.album.layer.AbstractLayer;


public class ActivityAbout extends AbstractLayer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.album_about_layer);
        super.setStatusBarColor(android.R.color.transparent);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        Uri storeUri = Uri.parse("market://search?q=pub:JChip+Games");
        Uri gameUri = Uri.parse("market://details?id=com.jchip.sokomon");
        TextView textView = this.getTextView(R.id.store_link);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setOnClickListener((e) -> this.startActivity(new Intent(Intent.ACTION_VIEW, storeUri)));
        ImageView imageView = this.getImageView(R.id.about_image);
        imageView.setClipToOutline(true);
        imageView.setOnClickListener((e) -> this.startActivity(new Intent(Intent.ACTION_VIEW, gameUri)));
        this.getTextView(R.id.version_code).setText(BuildConfig.VERSION_NAME);
        this.getView(R.id.album_about_view).setOnClickListener((v) -> this.finish());
    }
}