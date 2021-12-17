package com.jchip.album.data;


import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
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
        int id = this.insert(AlbumData.tableName, this.getAlbumContentValues(albumData));
        albumData.setAlbumId(id);
        return albumData;
    }


    // update exist album
    public AlbumData updateAlbum(AlbumData albumData) {
        if (albumData.isSaved()) {
            this.update(AlbumData.tableName, this.getAlbumContentValues(albumData));
        }
        return albumData;
    }

    // save exist album
    public AlbumData saveAlbum(AlbumData albumData) {
        if (albumData.isSaved()) {
            return this.updateAlbum(albumData);
        } else {
            return this.createAlbum(albumData);
        }
    }

    // delete exist album and photos
    public AlbumData deleteAlbum(AlbumData albumData) {
        if (albumData.isSaved()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(AlbumData.fieldAlbumId, albumData.getAlbumId());
            this.delete(AlbumData.tableName, contentValues);

            contentValues = new ContentValues();
            contentValues.put(PhotoData.fieldAlbumId, albumData.getAlbumId());
            this.delete(PhotoData.tableName, contentValues);
        }
        return albumData;
    }

    // Read records related to the album
    public List<AlbumData> queryAlbums() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ").append(AlbumData.tableName);
        sql.append(" ORDER BY ").append(AlbumData.fieldAlbumId).append(" DESC");

        List<AlbumData> albums = new ArrayList<>();
        for (Map<String, Object> rowData : this.query(sql.toString())) {
            albums.add(this.getAlbumData(rowData));
        }
        return albums;
    }

    // create new photo
    public PhotoData createPhoto(PhotoData photoData) {
        photoData.setPhotoId(this.insert(PhotoData.tableName, this.getPhotoContentValues(photoData)));
        return photoData;
    }

    // update exist photo
    public PhotoData updatePhoto(PhotoData photoData) {
        if (photoData.isSaved()) {
            this.update(PhotoData.tableName, this.getPhotoContentValues(photoData));
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
        sql.append(" ORDER BY ").append(PhotoData.fieldPhotoId).append(" ASC");

        List<PhotoData> photos = new ArrayList<>();
        for (Map<String, Object> rowData : this.query(sql.toString())) {
            photos.add(this.getPhotoData(rowData));
        }
        return photos;
    }

    // Read records related to the album and photo
    public List<AlbumData> queryPhotoAlbum() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ").append(AlbumData.tableName);
//        sql.append(" SELECT ").append(AlbumData.tableName).append(".").append(AlbumData.fieldAlbumName).append(", ")
//                .append(PhotoData.tableName).append(".* ");
//        sql.append(" FROM ").append(AlbumData.tableName);
        sql.append(" LEFT JOIN ").append(PhotoData.tableName);
        sql.append(" ON ").append(AlbumData.tableName).append(".").append(AlbumData.fieldAlbumId)
                .append(" = ").append(PhotoData.tableName).append(".").append(PhotoData.fieldAlbumId);
        sql.append(" AND ").append(PhotoData.tableName).append(".").append(PhotoData.fieldPhotoId)
                .append(" = (SELECT MIN( ").append(PhotoData.tableName).append(".").append(PhotoData.fieldPhotoId)
                .append(" ) FROM ").append(PhotoData.tableName).append(")");
        sql.append(" ORDER BY 1 ASC");

        List<AlbumData> albums = new ArrayList<>();
        for (Map<String, Object> rowData : this.query(sql.toString())) {
            AlbumData albumData = this.getAlbumData(rowData);
            PhotoData photoData = this.getPhotoData(rowData);
            albumData.setAlbumId(photoData.getAlbumId());
            albumData.addPhoto(photoData);
            albums.add(albumData);
        }
        return albums;
    }

    // Read records related to the album and photo
    public List<AlbumData> queryAlbumPhotos() {
        StringBuilder sql = new StringBuilder();
//        sql.append(" SELECT ").append(AlbumData.tableName).append(".").append(AlbumData.fieldAlbumName).append(", ")
//                .append(PhotoData.tableName).append(".* ");
        sql.append(" SELECT * FROM ").append(AlbumData.tableName);
        sql.append(" LEFT JOIN ").append(PhotoData.tableName);
        sql.append(" ON ").append(AlbumData.tableName).append(".").append(AlbumData.fieldAlbumId)
                .append(" = ").append(PhotoData.tableName).append(".").append(PhotoData.fieldAlbumId);
        sql.append(" ORDER BY 1 ASC");

        Map<Integer, AlbumData> albumMap = new HashMap<>();
        List<AlbumData> albums = new ArrayList<>();
        for (Map<String, Object> rowData : this.query(sql.toString())) {
            PhotoData photoData = this.getPhotoData(rowData);
            int albumId = photoData.getAlbumId();
            AlbumData albumData = albumMap.get(albumId);
            if (albumData == null) {
                albumData = this.getAlbumData(rowData);
                albumData.setAlbumId(albumId);
                albums.add(albumData);
                albumMap.put(albumId, albumData);
            }
            albumData.addPhoto(photoData);
        }
        return albums;
    }

    // create new widget
    public WidgetData createWidget(WidgetData widgetData) {
        this.insert(WidgetData.tableName, this.getWidgetContentValues(widgetData));
        return widgetData;
    }

    // update exist widget
    public WidgetData updateWidget(WidgetData widgetData) {
        if (widgetData.isSaved()) {
            this.update(AlbumData.tableName, this.getWidgetContentValues(widgetData));
        }
        return widgetData;
    }

    // delete exist widget
    public WidgetData deleteWidget(WidgetData widgetData) {
        if (widgetData.isSaved()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WidgetData.fieldWidgetId, widgetData.getWidgetId());
            this.delete(AlbumData.tableName, contentValues);
        }
        return widgetData;
    }

    // Read record related to a widget
    public WidgetData queryWidget(int widgetId, int photoId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * ");
        if (photoId > 0) {
            sql.append(" ( SELECT GROUP_CONCAT(").append(AlbumData.fieldAlbumId).append(", SEPARATOR ',') FROM ").append(AlbumData.tableName);
            sql.append(" WHERE ").append(AlbumData.tableName).append(".").append(AlbumData.fieldAlbumId);
            sql.append(" = ").append(WidgetData.tableName).append(".").append(WidgetData.fieldAlbumId);
            sql.append(" ) AS ").append(WidgetData.valuePhotoIds);
        }
        sql.append(" FROM ").append(WidgetData.tableName);
        sql.append(" LEFT JOIN ").append(PhotoData.tableName);
        sql.append(" ON ").append(PhotoData.tableName).append(".").append(PhotoData.fieldPhotoId);
        if (photoId > 0) {
            sql.append(" = ").append(photoId);
        } else {
            sql.append(" = ").append(WidgetData.tableName).append(".").append(WidgetData.fieldPhotoId);
        }
        sql.append(" WHERE ").append(WidgetData.fieldWidgetId).append(" = ").append(widgetId);

        for (Map<String, Object> rowData : this.query(sql.toString())) {
            WidgetData widgetData = this.getWidgetData(rowData);
            widgetData.setPhoto(this.getPhotoData(rowData));
            widgetData.setPhotoIds((String) rowData.get(WidgetData.valuePhotoIds));
            return widgetData;
        }
        return null;
    }

    private ContentValues getAlbumContentValues(AlbumData albumData) {
        ContentValues contentValues = new ContentValues();
        if (albumData.getAlbumId() >= 0) {
            contentValues.put(AlbumData.fieldAlbumId, albumData.getAlbumId());
        }
        contentValues.put(AlbumData.fieldAlbumName, albumData.getAlbumName());
        return contentValues;
    }

    private AlbumData getAlbumData(Map<String, Object> rowData) {
        AlbumData albumData = new AlbumData();
        albumData.setAlbumId((Integer) rowData.get(AlbumData.fieldAlbumId));
        Log.d("", "rowData.get(AlbumData.fieldAlbumId) ==" + rowData.get(AlbumData.fieldAlbumId));
        albumData.setAlbumName((String) rowData.get(AlbumData.fieldAlbumName));
        return albumData;
    }

    private ContentValues getPhotoContentValues(PhotoData photoData) {
        ContentValues contentValues = new ContentValues();
        if (photoData.getPhotoId() >= 0) {
            contentValues.put(PhotoData.fieldPhotoId, photoData.getPhotoId());
        }
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
        return contentValues;
    }

    private PhotoData getPhotoData(Map<String, Object> rowData) {
        PhotoData photoData = new PhotoData();
        photoData.setPhotoId((Integer) rowData.get(PhotoData.fieldPhotoId));
        photoData.setAlbumId((Integer) rowData.get(PhotoData.fieldAlbumId));
        photoData.setPhotoPath((String) rowData.get(PhotoData.fieldPhotoPath));

        photoData.setFrameIndex((Integer) rowData.get(PhotoData.fieldFrame));
        photoData.setScaleIndex((Integer) rowData.get(PhotoData.fieldScale));
        photoData.setFlipIndex((Integer) rowData.get(PhotoData.fieldFlip));
        photoData.setRotationIndex((Integer) rowData.get(PhotoData.fieldRotation));

        photoData.setFontType((Integer) rowData.get(PhotoData.fieldFontType));
        photoData.setFontSize((Integer) rowData.get(PhotoData.fieldFontSize));
        photoData.setFontColor((Integer) rowData.get(PhotoData.fieldFontColor));
        photoData.setFontLocation((Integer) rowData.get(PhotoData.fieldFontLocation));
        photoData.setFontText((String) rowData.get(PhotoData.fieldFontText));
        return photoData;
    }

    private ContentValues getWidgetContentValues(WidgetData widgetData) {
        ContentValues contentValues = new ContentValues();
        if (widgetData.getWidgetId() >= 0) {
            contentValues.put(WidgetData.fieldWidgetId, widgetData.getWidgetId());
        }
        contentValues.put(WidgetData.fieldAlbumId, widgetData.getAlbumId());
        contentValues.put(WidgetData.fieldPhotoId, widgetData.getPhotoId());
        return contentValues;
    }

    private WidgetData getWidgetData(Map<String, Object> rowData) {
        WidgetData widgetData = new WidgetData();
        widgetData.setWidgetId((Integer) rowData.get(WidgetData.fieldWidgetId));
        widgetData.setAlbumId((Integer) rowData.get(WidgetData.fieldAlbumId));
        widgetData.setPhotoId((Integer) rowData.get(WidgetData.fieldPhotoId));
        return widgetData;
    }

}