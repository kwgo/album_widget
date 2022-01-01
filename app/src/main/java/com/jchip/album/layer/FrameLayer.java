package com.jchip.album.layer;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jchip.album.R;
import com.jchip.album.data.PhotoData;
import com.jchip.album.view.PhotoView;
import com.jchip.album.view.PhotoViewConfig;

import java.util.ArrayList;
import java.util.List;

public class FrameLayer extends RecyclerLayer {
    private static final int MAX_FRAME_NUMBER = 200;

    private List<Integer> frames;
    private List<Integer> frameShells;

    private PhotoView photoView;
    private Bitmap photoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(PhotoViewConfig.LAYER_FRAME_SETTING, R.layout.album_frame_layer);
        super.setStatusBarColor(android.R.color.transparent);
    }

    @Override
    public void initContentView() {
        PhotoData photoData = (PhotoData) this.getIntent().getSerializableExtra(PhotoData.tableName);
        this.photoView = new PhotoView(this, photoData, this.layer);

        Log.d("", "frame layer use ......");
        this.photoImage = this.photoView.getPhotoImage();

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

        this.initRecyclerView(R.id.album_frame_view, R.layout.album_frame_item, this.frames.size());

        this.getView(R.id.frame_setting_view).setOnClickListener((v) -> this.finish());
    }

    @Override
    protected void bindItemView(View itemView, final int position) {
        this.photo = new PhotoView(this, this.photoView.getPhotoData(), this.layer);
        this.photo.setPhotoFrame(frames.get(position));

        View view = itemView.findViewById(R.id.photo_view);
        Log.d("", "view.getWidth() ---------------------" + view.getWidth());
        Log.d("", "view.getHeight() ---------------------" + view.getHeight());
        this.photo.setFrameRect(new Rect(0, 0, view.getWidth(), view.getHeight()));
        this.setPhotoView(view);
        view.setOnClickListener((v) -> {
            this.photoView.setPhotoFrame(frames.get(position));
            this.setResult(RESULT_OK, this.getIntent());
            this.finish();
        });

        ImageView imageView = itemView.findViewById(R.id.photo_image);
        imageView.setImageBitmap(this.photoImage);
    }
}