package com.jchip.album.layer;

import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.SettingData;
import com.jchip.album.view.AlbumView;
import com.jchip.album.view.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class DataLayer extends AbstractLayer {

    protected List<AlbumView> queryAlbums() {
        List<AlbumView> albumViews = new ArrayList<>();
        for (AlbumData albumData : DataHelper.getInstance(this).queryAlbums()) {
            AlbumView albumView = new AlbumView(this, albumData, this.layer);
            albumViews.add(albumView);
        }
        return albumViews;
    }

    protected AlbumView saveAlbum() {
        DataHelper.getInstance(this).saveAlbum(this.album.getAlbumData());
        return this.album;
    }

    protected AlbumView updateAlbum() {
        DataHelper.getInstance(this).updateAlbum(this.album.getAlbumData());
        return this.album;
    }

    protected AlbumView deleteAlbum() {
        DataHelper.getInstance(this).deleteAlbum(this.album.getAlbumData());
        this.updateWidget();
        return this.album;
    }

    protected List<PhotoView> queryPhotos() {
        List<PhotoView> photoViews = new ArrayList<>();
        for (PhotoData photoData : DataHelper.getInstance(this).queryPhotos(this.album.getAlbumId())) {
            PhotoView PhotoView = new PhotoView(this, photoData, this.layer);
            photoViews.add(PhotoView);
        }
        return photoViews;
    }

    protected PhotoView createPhoto() {
        DataHelper.getInstance(this).createPhoto(this.photo.getPhotoData());
        return this.photo;
    }

    protected PhotoView deletePhoto() {
        DataHelper.getInstance(this).deletePhoto(this.photo.getPhotoData());
        this.updateWidget();
        return this.photo;
    }

    protected PhotoView updatePhoto() {
        DataHelper.getInstance(this).updatePhoto(this.photo.getPhotoData());
        this.updateWidget();
        return this.photo;
    }

    protected boolean existAlbumWidget() {
        return DataHelper.getInstance(this).existAlbumWidget(this.album.getAlbumId());
    }

    protected boolean existPhotoWidget() {
        return DataHelper.getInstance(this).existPhotoWidget(this.photo.getPhotoId());
    }

    protected List<AlbumView> queryPhotoAlbum() {
        List<AlbumView> albumViews = new ArrayList<>();
        for (AlbumData albumData : DataHelper.getInstance(this).queryPhotoAlbum()) {
            AlbumView albumView = new AlbumView(this, albumData, this.layer);
            albumViews.add(albumView);
        }
        return albumViews;
    }

    protected List<AlbumView> queryAlbumPhotos() {
        List<AlbumView> albumViews = new ArrayList<>();
        for (AlbumData albumData : DataHelper.getInstance(this).queryAlbumPhotos()) {
            AlbumView albumView = new AlbumView(this, albumData, this.layer);
            albumViews.add(albumView);
        }
        return albumViews;
    }

    protected SettingData querySetting() {
        return DataHelper.getInstance(this).querySetting();
    }

    protected SettingData saveSetting() {
        return DataHelper.getInstance(this).saveSetting(this.settingData);
    }
}