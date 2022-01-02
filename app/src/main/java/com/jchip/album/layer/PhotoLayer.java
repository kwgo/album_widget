package com.jchip.album.layer;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.jchip.album.ActivityPhotoPicker;
import com.jchip.album.R;
import com.jchip.album.common.GestureHelper;
import com.jchip.album.photo.common.PhotoConfig;
import com.jchip.album.photo.model.PhotoModel;
import com.jchip.album.view.AlbumView;
import com.jchip.album.view.PhotoView;

import java.util.List;

public class PhotoLayer extends FlowLayer {

    @Override
    public void initContentView() {
        super.initContentView();

        GestureHelper.setViewGesture(this.getView(R.id.photo_frame), () -> onSlipPhoto(+1), () -> onSlipPhoto(-1));

        this.getButtonView(R.id.photo_add).setOnClickListener((e) -> onSelectPhotos());
        this.getButtonView(R.id.photo_delete).setOnClickListener((v) -> this.onDeletePhoto());

        this.getButtonView(R.id.photo_scale).setOnClickListener(this::onScalePhoto);
        this.getButtonView(R.id.photo_rotation).setOnClickListener((v) -> this.onRotatePhoto());
        this.getButtonView(R.id.photo_flip).setOnClickListener((v) -> this.onFlipPhoto());

        this.getButtonView(R.id.photo_left).setOnClickListener((v) -> this.onSlipPhoto(-1));
        this.getButtonView(R.id.photo_right).setOnClickListener((v) -> this.onSlipPhoto(+1));
    }

    private void onSelectPhotos() {
        this.startActivity(ActivityPhotoPicker.class, this::onSelectedPhotos);
    }

    protected void onSelectedPhotos(Intent intent) {
        List<PhotoModel> photoModels = intent.getParcelableArrayListExtra(PhotoConfig.SELECTED_PHOTOS);
        if (photoModels != null && !photoModels.isEmpty()) {
            this.selectPhotos(photoModels);
        }
    }

    private void onDeletePhoto() {
        if (this.photo.isSaved()) {
            this.alert(R.string.photo_title, R.string.album_alert_delete, () -> {
                if (this.existPhotoWidget()) {
                    this.alert(R.string.photo_title, R.string.album_alert_link, this::removePhoto);
                } else {
                    removePhoto();
                }
            });
        }
    }

    private void removePhoto() {
        int photoIndex = this.album.getPhotoViews().indexOf(photo);
        if (this.photo.isSaved()) {
            this.album.removePhotoView(this.photo);
            this.deletePhoto();
        }
        photoIndex = photoIndex < this.album.getPhotoSize() ? photoIndex : this.album.getPhotoSize() - 1;
        this.setAlbumPhoto(this.album.getPhotoView(photoIndex));
    }

    public void setAlbumPhotos(AlbumView album) {
        this.album = album;
        if (album.isSaved()) {
            this.album.setPhotoViews(this.queryPhotos());
        }
        this.setAlbumPhoto(this.album.getPhotoView(0));
    }

    public void setAlbumPhoto(PhotoView photo) {
        this.photo = photo;

        // to set image view size
        this.photo.setFrameRect(this.getViewRect(R.id.photo_container_view));
        this.setPhotoView(this.getView(R.id.photo_view));
    }

    private void onSlipPhoto(int offset) {
        if (this.album.getPhotoSize() > 0) {
            List<PhotoView> photos = this.album.getPhotoViews();
            int postion = photos.indexOf(this.photo);
            postion = postion < 0 ? 0 : postion + offset;
            if (postion >= 0 && postion < this.album.getPhotoSize()) {
                this.setAlbumPhoto(photos.get(postion));
            }
        }
    }

    private void onScalePhoto(View v) {
        this.photo.setPhotoImage(-1, -1, (this.photo.getScaleIndex() + 1) % 4);
        this.setPhotoView(this.getView(R.id.photo_view));
        this.updatePhoto();
    }

    private void onFlipPhoto() {
        this.photo.setPhotoImage(this.photo.getFlipIndex() == 0 ? 1 : 0, -1, -1);
        this.setPhotoView(this.getView(R.id.photo_view));
        this.updatePhoto();
    }

    protected void onRotatePhoto() {
        this.photo.setPhotoImage(-1, (this.photo.getRotationIndex() + 1) % 4, -1);
        this.setPhotoView(this.getView(R.id.photo_view));
        this.updatePhoto();
    }

    private void selectPhotos(List<PhotoModel> photoModels) {
        if (photoModels != null && !photoModels.isEmpty()) {
            this.saveAlbum();
            PhotoView photoView = this.photo;
            for (PhotoModel photoModel : photoModels) {
                this.photo = new PhotoView(this, this.layer);
                this.photo.setPhotoInfo(this.album.getAlbumId(), photoModel.getPhotoPath(), photoModel.getPhotoWidth(), photoModel.getPhotoHeight());
                if (!this.album.isPhotoExisted(this.photo)) {
                    this.photo.setPhotoImage(-1, photoModel.getPhotoOrientation() % 360 / 90, -1);
                    this.photo.setPhotoFrame(photoView.getFrameIndex());
                    this.album.addPhotoView(this.createPhoto());
                }
            }
            this.setAlbumPhoto(this.album.getPhotoView(this.album.getPhotoSize() - 1));
            ((AlbumLayer) this).reloadAlbumList();
        }
    }
}