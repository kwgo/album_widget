package com.jchip.album.activity;

import android.content.Intent;
import android.view.View;

import com.jchip.album.R;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.common.GestureHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.PhotoData;
import com.rayzhang.android.rzalbum.RZAlbum;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

import java.util.List;

public class PhotoActivity extends LayerActivity {

    @Override
    public void initContentView() {
        super.initContentView();

        GestureHelper.setViewGesture(this.getView(R.id.photo_frame), () -> onSlipPhoto(+1), () -> onSlipPhoto(-1));

        this.getButtonView(R.id.photo_add).setOnClickListener((e) -> onAddPhotos());
        this.getButtonView(R.id.photo_delete).setOnClickListener((v) -> this.onDeletePhoto());

        this.getButtonView(R.id.photo_scale).setOnClickListener((v) -> this.onScalePhoto(v));
        this.getButtonView(R.id.photo_rotation).setOnClickListener((v) -> this.onRotatePhoto());
        this.getButtonView(R.id.photo_flip).setOnClickListener((v) -> this.onFlipPhoto());

        this.getButtonView(R.id.photo_left).setOnClickListener((v) -> this.onSlipPhoto(-1));
        this.getButtonView(R.id.photo_right).setOnClickListener((v) -> this.onSlipPhoto(+1));
    }

    private void onAddPhotos() {
        AlbumHelper.selectPhotos(this);
    }

    private void onDeletePhoto() {
        if (this.photo.isSaved()) {
            this.alert(R.string.photo_title, R.string.album_alert_delete, () -> {
                if (this.existPhotoWidget()) {
                    this.alert(R.string.photo_title, R.string.album_alert_link, () -> {
                        removePhoto();
                    });
                } else {
                    removePhoto();
                }
            });
        }
    }

    private void removePhoto() {
        if (this.photo.isSaved()) {
            this.album.removePhoto(this.photo);
            this.deletePhoto();
        }
        this.setAlbumPhoto(this.album.getPhoto(0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AlbumHelper.ALBUM_REQUEST_CODE) {
            List<AlbumPhoto> albumPhotos = RZAlbum.parseResult(data);
            if (albumPhotos != null && !albumPhotos.isEmpty()) {
                this.onSelectedPhotos(albumPhotos);
            }
        }
    }

    public void setAlbumPhotos(AlbumData album) {
        this.album = album;
        this.album.clearPhotos();
        if (album.isSaved()) {
            this.album.setPhotos(this.queryPhotos());
        }
        this.setAlbumPhoto(this.album.getPhoto(0));
    }

    public void setAlbumPhoto(PhotoData photo) {
        this.photo = photo;
        this.setPhotoView(this.getView(R.id.photos_view));
    }

    private boolean onSlipPhoto(int offset) {
        if (!this.album.isPhotoEmpty()) {
            int postion = this.album.getPhotoIndex(this.photo);
            postion = postion < 0 ? 0 : postion + offset;
            if (postion >= 0 && postion < this.album.getPhotoSize()) {
                this.setAlbumPhoto(this.album.getPhoto(postion));
                return true;
            }
        }
        return false;
    }

    private void onScalePhoto(View v) {
        this.photo.setScaleIndex((this.photo.getScaleIndex() + 1) % 4);
        this.setPhotoScale(this.getImageView(R.id.photo_image));
        this.updatePhoto();
    }

    private void onFlipPhoto() {
        this.photo.setFlipIndex((this.photo.getFlipIndex() + 1) % 2);
        this.setPhotoImage(this.getImageView(R.id.photo_image));
        this.updatePhoto();
    }

    protected void onRotatePhoto() {
        this.photo.setRotationIndex((this.photo.getRotationIndex() + 1) % 4);
        this.setPhotoImage(this.getImageView(R.id.photo_image));
        this.updatePhoto();
    }

    private void onSelectedPhotos(List<AlbumPhoto> albumPhotos) {
        if (albumPhotos != null && !albumPhotos.isEmpty()) {
            this.saveAlbum();
            PhotoData photo = this.photo;
            for (AlbumPhoto albumPhoto : albumPhotos) {
                this.photo = new PhotoData(this.album.getAlbumId(), albumPhoto.getPhotoPath());
                if (!this.album.isPhotoPathExisted(this.photo)) {
                    this.photo.setFrameIndex(photo.getFrameIndex());
                    this.album.addPhoto(this.createPhoto());
                }
            }
            this.photo = photo;
            if (!this.photo.isSaved()) {
                this.setAlbumPhoto(this.album.getPhoto(0));
            }
            ((AlbumActivity) this).reloadAlbumList();
        }
    }
}