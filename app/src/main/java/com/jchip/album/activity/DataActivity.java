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
        return DataHelper.getInstance(this).saveAlbum(this.album);
    }

    protected AlbumData updateAlbum() {
        return DataHelper.getInstance(this).updateAlbum(this.album);
    }

    protected AlbumData deleteAlbum() {
        return DataHelper.getInstance(this).deleteAlbum(this.album);
    }

    protected List<PhotoData> queryPhotos() {
        return DataHelper.getInstance(this).queryPhotos(this.album.getAlbumId());
    }

    protected PhotoData createPhoto() {
        DataHelper.getInstance(this).createPhoto(this.photo);
        return this.photo;
    }

    protected PhotoData deletePhoto() {
        DataHelper.getInstance(this).deletePhoto(this.photo);
        return this.photo;
    }

    protected PhotoData updatePhoto() {
        DataHelper.getInstance(this).updatePhoto(this.photo);
        return this.photo;
    }

    public List<AlbumData> queryPhotoAlbum() {
        return DataHelper.getInstance(this).queryPhotoAlbum();
    }

    public List<AlbumData> queryAlbumPhotos() {
        return DataHelper.getInstance(this).queryAlbumPhotos();
    }
}