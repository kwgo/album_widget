package com.jchip.album.data;

public class PhotoData {
    public static final String tableName = "photo";
    public static final String fieldPhotoId = "id";
    public static final String fieldAlbumId = "albumId";
    public static final String fieldPhotoPath = "path";

    private int photoId;
    private int albumId;
    private String photoPath;


    public PhotoData() {
    }

    public PhotoData(int albumId, String photoPath) {
        this.albumId = albumId;
        this.photoPath = photoPath;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    @Override
    public boolean equals(Object photo) {
        if (photo != null) {
            return this.photoPath != null && this.photoPath.equals(((PhotoData) photo).getPhotoPath());
        }
        return false;
    }
}
