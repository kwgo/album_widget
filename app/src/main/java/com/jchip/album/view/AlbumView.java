package com.jchip.album.view;

import android.content.Context;

import com.jchip.album.data.AlbumData;
import com.jchip.album.data.PhotoData;

import java.util.ArrayList;
import java.util.List;

public class AlbumView {
    private final Context context;
    private final AlbumData album;
    private final int layer;

    private List<PhotoView> photoViews;

    public AlbumView(Context context, int layer) {
        this(context, new AlbumData(), layer);
    }

    public AlbumView(Context context, AlbumData album, int layer) {
        this.context = context;
        this.album = album;
        this.layer = layer;
        this.photoViews = new ArrayList<>();
        for (int index = 0; index < album.getPhotoSize(); index++) {
            this.photoViews.add(new PhotoView(context, album.getPhoto(index), layer));
        }
    }

    public AlbumData getAlbumData() {
        return this.album;
    }

    public boolean isSaved() {
        return this.album.isSaved();
    }

    public boolean isPhotoExisted(PhotoView photoView) {
        return this.album.isPhotoExisted(photoView.getPhotoData());
    }

    public int getAlbumId() {
        return this.album.getAlbumId();
    }

    public String getAlbumName() {
        return this.album.getAlbumName();
    }

    public void setAlbumName(String albumName) {
        this.album.setAlbumName(albumName);
    }

    public List<PhotoView> getPhotoViews() {
        return this.photoViews;
    }

    public int getPhotoSize() {
        return this.photoViews.size();
    }

    public PhotoView getPhotoView(int index) {
        return index >= 0 && index < this.photoViews.size() ? this.photoViews.get(index) :
                new PhotoView(this.context, new PhotoData(), this.layer);
    }

    public void addPhotoView(PhotoView photoView) {
        this.album.addPhoto(photoView.getPhotoData());
        this.photoViews.add(photoView);
    }

    public void removePhotoView(PhotoView photoView) {
        this.album.removePhoto(photoView.getPhotoData());
        this.photoViews.remove(photoView);
    }

    public void setPhotoViews(List<PhotoView> photoViews) {
        List<PhotoData> photos = new ArrayList<>();
        for (PhotoView photoView : photoViews) {
            photos.add(photoView.getPhotoData());
        }
        this.album.setPhotos(photos);
        this.photoViews = photoViews;
    }

}
