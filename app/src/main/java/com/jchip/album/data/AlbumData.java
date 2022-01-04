package com.jchip.album.data;

import java.util.ArrayList;
import java.util.List;

public class AlbumData extends AbstractData {
    public static final String tableName = "album";
    public static final String fieldAlbumId = "id";
    public static final String fieldAlbumName = "name";

    private int albumId = -1;
    private String albumName = "";

    private List<PhotoData> photos = new ArrayList<>();

    public AlbumData() {
    }

    public AlbumData(String albumName) {
        this.albumName = albumName;
    }

    public boolean isSaved() {
        return albumId >= 0;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setPhotos(List<PhotoData> photos) {
        this.photos = photos;
    }

    public void addPhoto(PhotoData photo) {
        photos.add(photo);
    }

    public void removePhoto(PhotoData photo) {
        photos.remove(photo);
    }

    public PhotoData getPhoto(int index) {
        return index >= 0 && index < photos.size() ? photos.get(index) : new PhotoData();
    }

    public int getPhotoSize() {
        return photos == null ? 0 : photos.size();
    }

    public boolean isPhotoExisted(PhotoData photoData) {
        if (photoData.getPhotoPath() != null) {
            for (PhotoData photo : photos) {
                if (photo.getPhotoPath().equals(photoData.getPhotoPath())) {
                    return true;
                }
            }
        }
        return false;
    }
}
