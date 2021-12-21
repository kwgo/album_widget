package com.jchip.album.activity;

import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;

import java.util.List;

public class DataActivity extends AbstractActivity {

    @Override
    public void initContentView() {
        super.initContentView();
    }

    protected List<AlbumData> queryAlbums() {
        return DataHelper.getInstance(this).queryAlbums();
    }

    protected AlbumData saveAlbum() {
        // this.updateWidget();
        return DataHelper.getInstance(this).saveAlbum(this.album);
    }

    protected AlbumData updateAlbum() {
        // this.updateWidget();
        return DataHelper.getInstance(this).updateAlbum(this.album);
    }

    protected AlbumData deleteAlbum() {
        this.updateWidget();
        return DataHelper.getInstance(this).deleteAlbum(this.album);
    }

    protected List<PhotoData> queryPhotos() {
        return DataHelper.getInstance(this).queryPhotos(this.album.getAlbumId());
    }

    protected PhotoData createPhoto() {
        DataHelper.getInstance(this).createPhoto(this.photo);
        this.updateWidget();
        return this.photo;
    }

    protected PhotoData deletePhoto() {
        DataHelper.getInstance(this).deletePhoto(this.photo);
        this.updateWidget();
        return this.photo;
    }

    protected PhotoData updatePhoto() {
        DataHelper.getInstance(this).updatePhoto(this.photo);
        this.updateWidget();
        return this.photo;
    }

    protected boolean existAlbumWidget() {
        return DataHelper.getInstance(this).existAlbumWidget(this.album.getAlbumId());
    }

    protected boolean existPhotoWidget() {
        return DataHelper.getInstance(this).existPhotoWidget(this.photo.getPhotoId());
    }

}