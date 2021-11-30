package com.jchip.album.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataHandler extends SQLiteOpenHelper {

    protected static final int DATABASE_VERSION = 4;
    // database name
    protected static final String DATABASE_NAME = "album";

    // table details
    //protected String tableName;
    protected String FIELD_ID = "id";

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    protected DataHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    // @Override
    // public void onConfigure(SQLiteDatabase db) {
    //     super.onConfigure(db);
    //    db.setForeignKeyConstraintsEnabled(true);
    //}

    // creating table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("", "onCreate +++++++++++++++++++++++++++");
        this.createAlbumTable(db);
        this.createPhotoTable(db);
    }

    // When upgrading the database, it will drop the current table and recreate.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("", "onUpgrade +++++++++++++++++++++++++++");
        this.dropAlbumTable(db);
        this.dropPhotoTable(db);
        this.onCreate(db);
    }


    protected int insert(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = (int) db.insert(tableName, null, contentValues);
        // db.close();
        return id;
    }

    protected List<Map<String, Object>> query(String sql) {
        List<Map<String, Object>> data = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Map<String, Object> rowData = new HashMap<>();
                for (int column = 0; column < cursor.getColumnCount(); column++) {
                    switch (cursor.getType(column)) {
                        case Cursor.FIELD_TYPE_INTEGER:
                            rowData.put(cursor.getColumnName(column), cursor.getInt(column));
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            rowData.put(cursor.getColumnName(column), cursor.getString(column));
                            break;
                    }
                }
                data.add(rowData);
            }
        }
        cursor.close();
        // db.close();
        return data;
    }

    protected Map<String, Object> query(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String, Object> rowData = new HashMap<>();
        Cursor cursor = db.query(tableName, null, "id=?", new String[]{contentValues.getAsString(FIELD_ID)}, null, null, null);
        if (cursor.moveToFirst()) {
            for (int column = 0; column < cursor.getColumnCount(); column++) {
                switch (cursor.getType(column)) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        rowData.put(cursor.getColumnName(column), cursor.getInt(column));
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        rowData.put(cursor.getColumnName(column), cursor.getString(column));
                        break;
                }
            }
        }
        cursor.close();
        // db.close();
        return rowData;
    }

    protected boolean update(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.update(tableName, contentValues, "id=?", new String[]{contentValues.getAsString(FIELD_ID)});
        // db.close();
        return result > 0;
    }

    protected boolean delete(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(tableName, "id=?", new String[]{contentValues.getAsString(FIELD_ID)});
        // db.close();
        return result > 0;
    }

    protected void createTable(SQLiteDatabase db, String tableName, ContentValues contentValues) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(tableName);
        sql.append(" (");
        int column = 0;
        for (String columnName : contentValues.keySet()) {
            sql.append(columnName).append(" ").append(contentValues.getAsString(columnName));
            if (column++ < contentValues.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
        Log.d("", "creating table SQL: " + sql.toString());
        db.execSQL(sql.toString());
        //// db.close();
    }

    public void createAlbumTable(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(AlbumData.fieldAlbumId, "INTEGER PRIMARY KEY AUTOINCREMENT");
        contentValues.put(AlbumData.fieldAlbumName, "TEXT");
        this.createTable(db, AlbumData.tableName, contentValues);
    }

    public void createPhotoTable(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotoData.fieldPhotoId, "INTEGER PRIMARY KEY AUTOINCREMENT");
        contentValues.put(PhotoData.fieldAlbumId, "INT");
        contentValues.put(PhotoData.fieldPhotoPath, "TEXT");
        this.createTable(db, PhotoData.tableName, contentValues);
    }

    protected void dropAlbumTable(SQLiteDatabase db) {
        this.dropTable(db, AlbumData.tableName);
    }

    protected void dropPhotoTable(SQLiteDatabase db) {
        this.dropTable(db, PhotoData.tableName);
    }

    protected void dropTable(SQLiteDatabase db, String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("DROP TABLE IF EXISTS ").append(tableName);
        Log.d("", "drop table SQL: " + sql.toString());
        db.execSQL(sql.toString());
        //// db.close();
    }

}