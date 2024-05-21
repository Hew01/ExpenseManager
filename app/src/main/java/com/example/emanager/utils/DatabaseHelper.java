package com.example.emanager.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "exampleTableCHITIEU.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_CHITIEUS = "ChiTieus";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CHITIEUS_TABLE = "CREATE TABLE " + TABLE_CHITIEUS + "(MaChiTieu TEXT primary key, MaNguoiDung TEXT, Tien INTEGER, ThoiGian TEXT, Loai TEXT, GhiChu TEXT)";
        db.execSQL(CREATE_CHITIEUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHITIEUS);
        onCreate(db);
    }

    public Cursor getAllChiTieus() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CHITIEUS, null);
    }

    public long insertUser(String MaChiTieu, String MaNguoiDung, Integer Tien, String ThoiGian, String Loai, String GhiChu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaChiTieu", MaChiTieu);
        values.put("MaNguoiDung", MaNguoiDung);
        values.put("Tien", Tien);
        values.put("ThoiGian", ThoiGian);
        values.put("Loai", Loai);
        values.put("GhiChu", GhiChu);

        long result = db.insert(TABLE_CHITIEUS, null, values);
        db.close();
        return result;
    }
}

