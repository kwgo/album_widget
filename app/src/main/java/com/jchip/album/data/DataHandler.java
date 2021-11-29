package com.jchip.album.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataHandler extends SQLiteOpenHelper {

    protected static final int DATABASE_VERSION = 1;
    // database name
    protected static final String DATABASE_NAME = "album";

    // table details
    protected String tableName;
    protected String idFieldName;

    // constructor
    protected DataHandler(Context context, String tableName, String idFieldName) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.tableName = tableName;
        this.idFieldName = idFieldName;
    }


    protected long insert(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(tableName, null, contentValues);
        db.close();
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
        db.close();
        return data;
    }

    protected Map<String, Object> query(ContentValues contentValues) {
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String, Object> rowData = new HashMap<>();
        Cursor cursor = db.query(tableName, null, "id=?", new String[]{contentValues.getAsString(idFieldName)}, null, null, null);
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
        db.close();
        return rowData;
    }

    protected boolean update(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.update(tableName, contentValues, "id=?", new String[]{contentValues.getAsString(idFieldName)});
        db.close();
        return result > 0;
    }

    protected boolean delete(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(tableName, "id=?", new String[]{contentValues.getAsString(idFieldName)});
        db.close();
        return result > 0;
    }

    protected void createTable(SQLiteDatabase db, ContentValues contentValues) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(tableName);
        sql.append("( ");
        int column = 0;
        for (String columnName : contentValues.keySet()) {
            sql.append(columnName).append(contentValues.getAsString(columnName));
            if (column++ < contentValues.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(" )");
        db.execSQL(sql.toString());
        //db.close();
    }

    protected void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        //db.close();
    }

}