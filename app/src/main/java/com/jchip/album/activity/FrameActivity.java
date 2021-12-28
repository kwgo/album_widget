package com.jchip.album.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jchip.album.R;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.data.PhotoData;

import java.util.ArrayList;
import java.util.List;

public class FrameActivity extends RecyclerActivity {
    private static final int MAX_FRAME_NUMBER = 200;

    private List<Integer> frames;
    private List<Integer> frameShells;

    private Bitmap photoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.album_frame_layer);
        super.setStatusBarColor(android.R.color.transparent);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.photo = (PhotoData) this.getIntent().getSerializableExtra(PhotoData.tableName);

        this.frames = new ArrayList<>();
        this.frameShells = new ArrayList<>();
        for (int index = 0; index < MAX_FRAME_NUMBER; index++) {
            int frameId = this.getResources().getIdentifier("frame_item_" + index, "drawable", this.getPackageName());
            int shellId = this.getResources().getIdentifier("frame_shell_" + index, "drawable", this.getPackageName());
            if (frameId > 0) {
                this.frames.add(frameId);
                this.frameShells.add(shellId <= 0 ? frameId : shellId);
            }
        }

        this.initRecyclerView(R.id.album_frame_view, R.layout.album_frame_item, frames.size());

        this.getView(R.id.frame_setting_view).setOnClickListener((v) -> this.finish());
    }

    @Override
    protected void bindItemView(View itemView, int position) {
        this.photo.setFrameIndex(frames.get(position));
        this.photo.setFrameShell(frameShells.get(position));

        View photoView = itemView.findViewById(R.id.photo_view);
        this.setPhotoView(photoView, false, false, true);
        photoView.setOnClickListener((view) -> {
            Intent intent = new Intent();
            intent.putExtra(FRAME_INDEX, position);
            intent.putExtra(FRAME_ID, (Integer) this.frames.get(position));
            intent.putExtra(SHELL_ID, (Integer) this.frameShells.get(position));
            this.setResult(RESULT_OK, intent);
            this.finish();
        });

        ImageView imageView = itemView.findViewById(R.id.photo_image);
        if (this.photoImage == null) {
            this.photoImage = PhotoHelper.loadPhotoImage(imageView, this.photo, PhotoHelper.getScreenWidth() / 2);
        }
        imageView.setImageBitmap(photoImage);
    }
}