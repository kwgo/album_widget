package com.jchip.album.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlbumDataHandler extends DataHandler {

    public static final String tableName = "album";
    public static final String fieldAlbumId = "id";
    public static final String fieldAlbumName = "name";

    public AlbumDataHandler(Context context) {
        super(context, tableName, fieldAlbumId);
    }

    // creating table
    @Override
    public void onCreate(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(fieldAlbumId, " INTEGER PRIMARY KEY AUTOINCREMENT ");
        contentValues.put(fieldAlbumName, " TEXT ");
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
    public AlbumData insert(AlbumData albumData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(fieldAlbumName, albumData.getAlbumName());
        int id = this.insert(contentValues);
        albumData.setAlbumId(id);
        return albumData;
    }

    // Read records related to the search term
    public List<AlbumData> queryAll() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ").append(tableName);
        sql.append(" ORDER BY ").append(fieldAlbumId).append(" DESC");
        List<Map<String, Object>> data = this.query(sql.toString());

        List<AlbumData> albums = new ArrayList<>();
        for (Map<String, Object> rowData : data) {
            AlbumData albumData = new AlbumData();
            albumData.setAlbumId((Integer) rowData.get(fieldAlbumId));
            albumData.setAlbumName((String) rowData.get(fieldAlbumName));
            albums.add(albumData);
        }
        return albums;
    }

}