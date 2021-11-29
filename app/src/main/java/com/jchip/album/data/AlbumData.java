package com.jchip.album.data;

public class AlbumData {

    private int albumId;
    private String albumName;

    public AlbumData() {
    }

    public AlbumData(String albumName) {
        this.albumName = albumName;
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

}
