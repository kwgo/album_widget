package com.jchip.album.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jchip.album.R;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.common.GestureHelper;
import com.jchip.album.common.ImageHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;
import com.rayzhang.android.rzalbum.RZAlbum;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends LayerActivity {

    private ImageView photoView;

    @Override
    public void initContentView() {
        super.initContentView();

        this.photoView = this.findViewById(R.id.photo_image);

        GestureHelper.setViewGesture(this.photoView, () -> onSlipPhoto(+1), () -> onSlipPhoto(-1));

        this.findViewById(R.id.photo_add).setOnClickListener((e) -> onAddPhotos());
        this.findViewById(R.id.photo_delete).setOnClickListener((v) -> this.onDeletePhoto());

        this.findViewById(R.id.photo_fit).setOnClickListener((v) -> this.onFitPhoto(v));
        this.findViewById(R.id.photo_rotation).setOnClickListener((v) -> this.onRotatePhoto());
        this.findViewById(R.id.photo_flip).setOnClickListener((v) -> this.onFlipPhoto());

        this.findViewById(R.id.photo_left).setOnClickListener((v) -> this.onSlipPhoto(-1));
        this.findViewById(R.id.photo_right).setOnClickListener((v) -> this.onSlipPhoto(+1));
    }

    private void onAddPhotos() {
        AlbumHelper.selectPhotos(this);
    }

    private void onDeletePhoto() {
        this.alert(R.string.photo_title, R.string.album_alert_delete, () -> {
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AlbumHelper.ALBUM_REQUEST_CODE) {
            List<AlbumPhoto> albumPhotos = RZAlbum.parseResult(data);
            Log.d("", "albumPhotos size:::::::::::::::" + albumPhotos.size());
            if (!this.isEmpty(albumPhotos)) {
                this.onSelectedPhotos(albumPhotos);
            }
        }
    }

    public void setAlbumPhotos(AlbumData album) {
        this.album = album;
        if (album.isSaved()) {
            this.photos = DataHelper.getInstance(this).queryPhotos(album.getAlbumId());
            this.setAlbumPhoto(this.photos.isEmpty() ? null : this.photos.get(0));
        } else {
            this.photos = new ArrayList<>();
            this.setAlbumPhoto(null);
        }
    }

    public void setAlbumPhoto(PhotoData photo) {
        this.photo = photo;
        if (!this.isEmpty(photo)) {
            this.refreshPhoto();
        } else {
            this.photoView.setImageBitmap(null);
        }
    }

    private boolean onSlipPhoto(int offset) {
        if (!this.isEmpty(this.photo) && !this.isEmpty(this.photos)) {
            int postion = this.photos.indexOf(this.photo);
            postion += offset;
            if (postion >= 0 && postion < this.photos.size()) {
                this.setAlbumPhoto(this.photos.get(postion));
                return true;
            }
        }
        return false;
    }

    private void onFitPhoto(View v) {
        ImageView.ScaleType[] scaleTypies = new ImageView.ScaleType[]{ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.FIT_CENTER, ImageView.ScaleType.FIT_XY};
        this.photo.setScaleIndex((this.photo.getScaleIndex() + 1) % scaleTypies.length);
        ((ImageView) findViewById(R.id.photo_image)).setScaleType(scaleTypies[this.photo.getScaleIndex()]);
    }

    private void onFlipPhoto() {
        this.photo.setFlipIndex((this.photo.getFlipIndex() + 1) % 2);
        this.refreshPhoto();
    }

    protected void onRotatePhoto() {
        this.photo.setRotationIndex((this.photo.getRotationIndex() + 1) % 4);
        this.refreshPhoto();
    }

    protected void refreshPhoto() {
        Bitmap bitmap = AlbumHelper.loadBitmap(this.photo.getPhotoPath());
        bitmap = ImageHelper.convertBitmap(bitmap, this.photo.getRotationIndex(), this.photo.getFlipIndex());
        this.photoView.setImageBitmap(bitmap);
    }

    private void onSelectedPhotos(List<AlbumPhoto> albumPhotos) {
        if (!this.isEmpty(this.album)) {
            this.album = DataHelper.getInstance(this).saveAlbum(this.album);
            if (this.album.isSaved()) {
                for (AlbumPhoto albumPhoto : albumPhotos) {
                    PhotoData photo = new PhotoData(this.album.getAlbumId(), albumPhoto.getPhotoPath());
                    if (!this.photos.contains(photo)) {
                        this.photos.add(DataHelper.getInstance(this).createPhoto(photo));
                        this.setAlbumPhoto(this.isEmpty(this.photo) ? photo : this.photo);
                    }
                }
            }
        }
    }
}