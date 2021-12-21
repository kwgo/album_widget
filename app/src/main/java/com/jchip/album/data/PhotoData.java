package com.jchip.album.data;

public class PhotoData extends AbstractData {
    public static final String tableName = "photo";
    public static final String fieldPhotoId = "id";
    public static final String fieldAlbumId = "albumId";
    public static final String fieldPhotoPath = "path";

    public static final String fieldFrame = "frame";
    public static final String fieldLook = "look";
    public static final String fieldScale = "scale";
    public static final String fieldFlip = "flip";
    public static final String fieldRotation = "rotation";

    public static final String fieldFontType = "fontType";
    public static final String fieldFontSize = "fontSize";
    public static final String fieldFontColor = "fontColor";
    public static final String fieldFontLocation = "fontLocation";
    public static final String fieldFontText = "fontText";

    private int photoId = -1;
    private int albumId = -1;
    private String photoPath;

    private int frameIndex = 0;
    private int frameLook = 0;
    private int scaleIndex = 0;
    private int flipIndex = 0;
    private int rotationIndex = 0;

    private int fontType = 0;
    private int fontSize = 80;
    private int fontColor = 0xFFFFFFFF;
    private int fontLocation = 4;
    private String fontText = "";

    public PhotoData() {
    }

    public PhotoData(int albumId, String photoPath) {
        this.albumId = albumId;
        this.photoPath = photoPath;
    }

    public boolean isSaved() {
        return photoId >= 0;
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

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    public int getFrameLook() {
        return frameLook;
    }

    public void setFrameLook(int frameLook) {
        this.frameLook = frameLook;
    }

    public int getScaleIndex() {
        return scaleIndex;
    }

    public void setScaleIndex(int scaleIndex) {
        this.scaleIndex = scaleIndex;
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

    public int getFontType() {
        return fontType;
    }

    public void setFontType(int fontType) {
        this.fontType = fontType;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public int getFontLocation() {
        return fontLocation;
    }

    public void setFontLocation(int fontLocation) {
        this.fontLocation = fontLocation;
    }

    public String getFontText() {
        return fontText;
    }

    public void setFontText(String fontText) {
        this.fontText = fontText;
    }
}
