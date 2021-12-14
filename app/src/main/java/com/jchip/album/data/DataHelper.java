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
        ContentValues contentValues = new ContentValues();
        contentValues.put(AlbumData.fieldAlbumId, albumData.getAlbumId());
        contentValues.put(AlbumData.fieldAlbumName, albumData.getAlbumName());
        this.update(AlbumData.tableName, contentValues);
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
        photoData.setPhotoId(this.insert(PhotoData.tableName, contentValues));
        return photoData;
    }

    // update exist photo
    public PhotoData updatePhoto(PhotoData photoData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotoData.fieldAlbumId, photoData.getAlbumId());
        contentValues.put(PhotoData.fieldPhotoPath, photoData.getPhotoPath());
        this.update(PhotoData.tableName, contentValues);
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
            photos.add(photo);
        }
        return photos;
    }

}