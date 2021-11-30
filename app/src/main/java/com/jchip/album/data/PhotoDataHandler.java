package com.jchip.album.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhotoDataHandler extends DataHandler {

    public static final String tableName = "photo";
    public static final String fieldPhotoId = "id";
    public static final String fieldAlbumId = "albumId";
    public static final String fieldPhotoPath = "path";

    public PhotoDataHandler(Context context) {
        super(context, tableName, fieldPhotoId);
    }

    // creating table
    @Override
    public void onCreate(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(fieldPhotoId, " INTEGER PRIMARY KEY AUTOINCREMENT ");
        contentValues.put(fieldAlbumId, " INT ");
        contentValues.put(fieldPhotoPath, " TEXT ");
        this.createTable(db, contentValues);
    }

    // When upgrading the database, it will drop the current table and recreate.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.dropTable(db);
        this.onCreate(db);
    }

    // create new record
    // @param albumData contains details to be added as single row.
    public PhotoData insert(PhotoData photoData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(fieldAlbumId, photoData.getAlbumId());
        contentValues.put(fieldPhotoPath, photoData.getPhotoPath());
        photoData.setPhotoId(this.insert(contentValues));
        return photoData;
    }

    // Read records related to the search term
    public List<PhotoData> queryAll(int albumId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ").append(tableName);
        sql.append(" WHERE ").append(fieldAlbumId).append(" = ").append(albumId);
        sql.append(" ORDER BY ").append(fieldPhotoId).append(" DESC");
        List<Map<String, Object>> data = this.query(sql.toString());

        List<PhotoData> photos = new ArrayList<>();
        for (Map<String, Object> rowData : data) {
            PhotoData photo = new PhotoData();
            photo.setPhotoId((Integer) rowData.get(fieldPhotoId));
            photo.setAlbumId((Integer) rowData.get(fieldAlbumId));
            photo.setPhotoPath((String) rowData.get(fieldPhotoPath));
            photos.add(photo);
        }
        return photos;
    }
}