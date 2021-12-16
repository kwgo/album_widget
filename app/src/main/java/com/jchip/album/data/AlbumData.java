package com.jchip.album.data;

import java.util.ArrayList;
import java.util.List;

public class AlbumData extends AbstractData {
    public static final String tableName = "album";
    public static final String fieldAlbumId = "id";
    public static final String fieldAlbumName = "name";

    private int albumId = -1;
    private String albumName;

    private List<PhotoData> photos;

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

    public List<PhotoData> getPhotos() {
        return photos == null ? new ArrayList<>() : photos;
    }

    public void setPhotos(List<PhotoData> photos) {
        this.photos = photos;
    }

    public void addPhoto(PhotoData photo) {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(photo);
    }

    public void removePhoto(PhotoData photo) {
        if (photos != null) {
            photos.remove(photo);
        }
    }

    public void clearPhotos() {
        if (photos != null) {
            photos.clear();
        }
    }

    public PhotoData getPhoto(int index) {
        if (photos != null && index >= 0 && index < photos.size()) {
            return photos.get(index);
        }
        return new PhotoData();
    }

    public boolean isPhotoEmpty() {
        return photos == null || photos.isEmpty();
    }

    public int getPhotoIndex(PhotoData photo) {
        if (photos != null) {
            return photos.indexOf(photo);
        }
        return -1;
    }

    public int getPhotoSize() {
        return photos == null ? 0 : photos.size();
    }

    public boolean isPhotoPathExisted(PhotoData photo) {
        if (photos != null && photo.getPhotoPath() != null) {
            for (PhotoData photoData : photos) {
                if (photo.getPhotoPath().equals(photoData.getPhotoPath())) {
                    return true;
                }
            }
        }
        return false;
    }

}
