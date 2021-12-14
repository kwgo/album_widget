package com.jchip.album.data;


import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataHelper extends DataHandler {
    private static DataHelper instance;

    private DataHelper(Context context) {
        super(context);
    }

    public static synchronized DataHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new DataHelper(context.getApplicationContext());
        }
        return instance;
    }


    // create new record
    public AlbumData createAlbum(AlbumData albumData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AlbumData.fieldAlbumName, albumData.getAlbumName());
        int id = this.insert(AlbumData.tableName, contentValues);
        albumData.setAlbumId(id);
        return albumData;
    }


    // update exist album
    public AlbumData updateAlbum(AlbumData albumData) {
        if (albumData.isSaved()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(AlbumData.fieldAlbumId, albumData.getAlbumId());
            contentValues.put(AlbumData.fieldAlbumName, albumData.getAlbumName());
            this.update(AlbumData.tableName, contentValues);
        }
        return albumData;
    }

    // save new record
    public AlbumData saveAlbum(AlbumData albumData) {
        if (albumData.isSaved()) {
            return this.updateAlbum(albumData);
        } else {
            return this.createAlbum(albumData);
        }
    }

    // delete new record
    public AlbumData deleteAlbum(AlbumData albumData) {
        if (albumData.isSaved()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PhotoData.fieldAlbumId, albumData.getAlbumId());
            this.delete(AlbumData.tableName, contentValues);
        }
        return albumData;
    }

    // Read records related to the album
    public List<AlbumData> queryAlbums() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ").append(AlbumData.tableName);
        sql.append(" ORDER BY ").append(AlbumData.fieldAlbumId).append(" DESC");
        List<Map<String, Object>> data = this.query(sql.toString());

        List<AlbumData> albums = new ArrayList<>();
        for (Map<String, Object> rowData : data) {
            AlbumData albumData = new AlbumData();
            albumData.setAlbumId((Integer) rowData.get(AlbumData.fieldAlbumId));
            albumData.setAlbumName((String) rowData.get(AlbumData.fieldAlbumName));
            albums.add(albumData);
        }
        return albums;
    }

    // create new photo
    public PhotoData createPhoto(PhotoData photoData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotoData.fieldAlbumId, photoData.getAlbumId());
        contentValues.put(PhotoData.fieldPhotoPath, photoData.getPhotoPath());

        contentValues.put(PhotoData.fieldFrame, photoData.getFrameIndex());
        contentValues.put(PhotoData.fieldScale, photoData.getScaleIndex());
        contentValues.put(PhotoData.fieldFlip, photoData.getFlipIndex());
        contentValues.put(PhotoData.fieldRotation, photoData.getRotationIndex());

        contentValues.put(PhotoData.fieldFontType, photoData.getFontType());
        contentValues.put(PhotoData.fieldFontSize, photoData.getFontSize());
        contentValues.put(PhotoData.fieldFontColor, photoData.getFontColor());
        contentValues.put(PhotoData.fieldFontLocation, photoData.getFontLocation());
        contentValues.put(PhotoData.fieldFontText, photoData.getFontText());

        photoData.setPhotoId(this.insert(PhotoData.tableName, contentValues));
        return photoData;
    }

    // update exist photo
    public PhotoData updatePhoto(PhotoData photoData) {
        if (photoData.isSaved()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PhotoData.fieldPhotoId, photoData.getPhotoId());
            contentValues.put(PhotoData.fieldAlbumId, photoData.getAlbumId());
            contentValues.put(PhotoData.fieldPhotoPath, photoData.getPhotoPath());

            contentValues.put(PhotoData.fieldFrame, photoData.getFrameIndex());
            contentValues.put(PhotoData.fieldScale, photoData.getScaleIndex());
            contentValues.put(PhotoData.fieldFlip, photoData.getFlipIndex());
            contentValues.put(PhotoData.fieldRotation, photoData.getRotationIndex());

            contentValues.put(PhotoData.fieldFontType, photoData.getFontType());
            contentValues.put(PhotoData.fieldFontSize, photoData.getFontSize());
            contentValues.put(PhotoData.fieldFontColor, photoData.getFontColor());
            contentValues.put(PhotoData.fieldFontLocation, photoData.getFontLocation());
            contentValues.put(PhotoData.fieldFontText, photoData.getFontText());

            this.update(PhotoData.tableName, contentValues);
        }
        return photoData;
    }

    // delete exist photo
    public PhotoData deletePhoto(PhotoData photoData) {
        if (photoData.isSaved()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PhotoData.fieldPhotoId, photoData.getPhotoId());
            this.delete(PhotoData.tableName, contentValues);
        }
        return photoData;
    }

    // Read records related to the photo
    public List<PhotoData> queryPhotos(int albumId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ").append(PhotoData.tableName);
        sql.append(" WHERE ").append(PhotoData.fieldAlbumId).append(" = ").append(albumId);
        sql.append(" ORDER BY ").append(PhotoData.fieldPhotoId).append(" DESC");
        List<Map<String, Object>> data = this.query(sql.toString());

        List<PhotoData> photos = new ArrayList<>();
        for (Map<String, Object> rowData : data) {
            PhotoData photo = new PhotoData();
            photo.setPhotoId((Integer) rowData.get(PhotoData.fieldPhotoId));
            photo.setAlbumId((Integer) rowData.get(PhotoData.fieldAlbumId));
            photo.setPhotoPath((String) rowData.get(PhotoData.fieldPhotoPath));

            photo.setFrameIndex((Integer) rowData.get(PhotoData.fieldFrame));
            photo.setScaleIndex((Integer) rowData.get(PhotoData.fieldScale));
            photo.setFlipIndex((Integer) rowData.get(PhotoData.fieldFlip));
            photo.setRotationIndex((Integer) rowData.get(PhotoData.fieldRotation));

            photo.setFontType((Integer) rowData.get(PhotoData.fieldFontType));
            photo.setFontSize((Integer) rowData.get(PhotoData.fieldFontSize));
            photo.setFontColor((Integer) rowData.get(PhotoData.fieldFontColor));
            photo.setFontLocation((Integer) rowData.get(PhotoData.fieldFontLocation));
            photo.setFontText((String) rowData.get(PhotoData.fieldFontText));

            photos.add(photo);
        }
        return photos;
    }

}