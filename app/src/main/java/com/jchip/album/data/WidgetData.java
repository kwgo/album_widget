package com.jchip.album.data;

public class WidgetData extends AbstractData {
    public static final String tableName = "widget";
    public static final String fieldWidgetId = "id";
    public static final String fieldAlbumId = "albumId";
    public static final String fieldPhotoId = "photoId";
    public static final String valuePhotoIds = "photoIds";
    public static final String valueTotal = "total";

    private int widgetId = -1;
    private int albumId = -1;
    private int photoId = -1;

    private String photoIds;
    private PhotoData photo;

    public boolean isSaved() {
        return widgetId >= 0;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(String photoIds) {
        this.photoIds = photoIds;
    }

    public PhotoData getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoData photo) {
        this.photo = photo;
    }
}
