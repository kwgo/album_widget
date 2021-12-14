package com.jchip.album.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jchip.album.R;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.common.GestureHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;
import com.rayzhang.android.rzalbum.RZAlbum;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

import java.util.List;

public class PhotoActivity extends LayerActivity {

    @Override
    public void initContentView() {
        super.initContentView();

        GestureHelper.setViewGesture(this.getView(R.id.photo_frame_cover), () -> onSlipPhoto(+1), () -> onSlipPhoto(-1));

        this.getButtonView(R.id.photo_add).setOnClickListener((e) -> onAddPhotos());
        this.getButtonView(R.id.photo_delete).setOnClickListener((v) -> this.onDeletePhoto());

        this.getButtonView(R.id.photo_fit).setOnClickListener((v) -> this.onFitPhoto(v));
        this.getButtonView(R.id.photo_rotation).setOnClickListener((v) -> this.onRotatePhoto());
        this.getButtonView(R.id.photo_flip).setOnClickListener((v) -> this.onFlipPhoto());

        this.getButtonView(R.id.photo_left).setOnClickListener((v) -> this.onSlipPhoto(-1));
        this.getButtonView(R.id.photo_right).setOnClickListener((v) -> this.onSlipPhoto(+1));
    }

    private void onAddPhotos() {
        AlbumHelper.selectPhotos(this);
    }

    private void onDeletePhoto() {
        this.alert(R.string.photo_title, R.string.album_alert_delete, () -> {
            if (this.photo.isSaved()) {
                this.photos.remove(this.photo);
                DataHelper.getInstance(this).deletePhoto(this.photo);
            }
            if (!this.photos.isEmpty()) {
                this.setAlbumPhoto(photos.get(0));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AlbumHelper.ALBUM_REQUEST_CODE) {
            List<AlbumPhoto> albumPhotos = RZAlbum.parseResult(data);
            Log.d("", "albumPhotos size:::::::::::::::" + albumPhotos.size());
            if (albumPhotos != null && !albumPhotos.isEmpty()) {
                this.onSelectedPhotos(albumPhotos);
            }
        }
    }

    public void setAlbumPhotos(AlbumData album) {
        this.album = album;
        this.photos.clear();
        if (album.isSaved()) {
            this.photos = DataHelper.getInstance(this).queryPhotos(album.getAlbumId());
        }
        this.setAlbumPhoto(this.photos.isEmpty() ? new PhotoData() : this.photos.get(0));
    }

    public void setAlbumPhoto(PhotoData photo) {
        this.photo = photo;
        this.setViewPhoto(this.getImageView(R.id.photo_image));
    }

    private boolean onSlipPhoto(int offset) {
        if (!this.photos.isEmpty()) {
            int postion = this.photos.indexOf(this.photo);
            postion = postion < 0 ? 0 : postion + offset;
            if (postion >= 0 && postion < this.photos.size()) {
                this.setAlbumPhoto(this.photos.get(postion));
                return true;
            }
        }
        return false;
    }

    private void onFitPhoto(View v) {
        ImageView.ScaleType[] scaleTypies = new ImageView.ScaleType[]{ImageView.ScaleType.CENTER_CROP, ImageView.ScaleType.FIT_CENTER, ImageView.ScaleType.FIT_XY, ImageView.ScaleType.CENTER};
        this.photo.setScaleIndex((this.photo.getScaleIndex() + 1) % scaleTypies.length);
        this.getImageView(R.id.photo_image).setScaleType(scaleTypies[this.photo.getScaleIndex()]);
        DataHelper.getInstance(this).updatePhoto(this.photo);
    }

    private void onFlipPhoto() {
        this.photo.setFlipIndex((this.photo.getFlipIndex() + 1) % 2);
        DataHelper.getInstance(this).updatePhoto(this.photo);
        this.setViewPhoto(this.getImageView(R.id.photo_image));
    }

    protected void onRotatePhoto() {
        this.photo.setRotationIndex((this.photo.getRotationIndex() + 1) % 4);
        DataHelper.getInstance(this).updatePhoto(this.photo);
        this.setViewPhoto(this.getImageView(R.id.photo_image));
    }


    private void onSelectedPhotos(List<AlbumPhoto> albumPhotos) {
        if (albumPhotos != null && !albumPhotos.isEmpty()) {
            DataHelper.getInstance(this).saveAlbum(this.album);
            for (AlbumPhoto albumPhoto : albumPhotos) {
                PhotoData photo = new PhotoData(this.album.getAlbumId(), albumPhoto.getPhotoPath());
                if (!this.photos.contains(photo)) {
                    photo.setFrameIndex(this.photo.getFrameIndex());
                    this.photos.add(DataHelper.getInstance(this).createPhoto(photo));
                }
            }
            if (!this.photo.isSaved() && !this.photos.isEmpty()) {
                this.setAlbumPhoto(this.photos.get(0));
            }
        }
    }
}