package com.jchip.album.data;

public class PhotoData extends AbstractData {
    public static final String tableName = "photo";
    public static final String fieldPhotoId = "id";
    public static final String fieldAlbumId = "albumId";
    public static final String fieldPhotoPath = "path";

    private int photoId;
    private int albumId;
    private String photoPath;

    private int typeIndex = 0;
    private int flipIndex = 0;
    private int rotationIndex = 0;

    private int fontIndex = 0;
    private int fontSizeIndex = 0;
    private int fontColor = 0;
    private int locationIndex = 0;
    private String comment = "";

    public PhotoData() {
    }

    public PhotoData(int albumId, String photoPath) {
        this.albumId = albumId;
        this.photoPath = photoPath;
    }

    public boolean isSaved() {
        return photoId > 0;
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


    public int getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(int typeIndex) {
        this.typeIndex = typeIndex;
    }

    public int getFlipIndex() {
        return flipIndex;
    }

    public void setFlipIndex(int flipIndex) {
        this.flipIndex = flipIndex;
    }

    public int getRotationIndex() {
        return rotationIndex;
    }

    public void setRotationIndex(int rotationIndex) {
        this.rotationIndex = rotationIndex;
    }

    public int getFontIndex() {
        return fontIndex;
    }

    public void setFontIndex(int fontIndex) {
        this.fontIndex = fontIndex;
    }

    public int getFontSizeIndex() {
        return fontSizeIndex;
    }

    public void setFontSizeIndex(int fontSizeIndex) {
        this.fontSizeIndex = fontSizeIndex;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public int getLocationIndex() {
        return locationIndex;
    }

    public void setLocationIndex(int locationIndex) {
        this.locationIndex = locationIndex;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object photo) {
        if (photo != null) {
            return this.photoPath != null && this.photoPath.equals(((PhotoData) photo).getPhotoPath());
        }
        return false;
    }
}
