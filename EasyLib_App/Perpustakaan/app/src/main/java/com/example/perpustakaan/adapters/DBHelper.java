package com.example.perpustakaan.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String database_name = "db_perpus";
    public static final String tabel_name = "tabel_perpus";

    public static final String row_id = "_id";
    public static final String row_nama = "Nama";
    public static final String row_judul = "Judul";
    public static final String row_pinjam = "TglPinjam";
    public static final String row_kembali = "TglKembali";
    public static final String row_status = "Status";

    private SQLiteDatabase db;


    public DBHelper(Context context) {
        super(context, database_name, null, 2);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + tabel_name + "(" + row_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_nama + " TEXT," + row_judul + " TEXT," + row_pinjam + " TEXT," + row_kembali + " TEXT," + row_status + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tabel_name);
    }



    public Cursor allData(){
        Cursor cur = db.rawQuery("SELECT * FROM " + tabel_name + " ORDER BY " + row_id + " DESC ", null);
        return cur;
    }


    public Cursor dataPinjam(){
        Cursor cur = db.rawQuery("SELECT * FROM " + tabel_name + " WHERE " + row_status + "=" + "'Dipinjam'", null);
        return cur;
    }


    public Cursor dataDikembalikan(){
        Cursor cur = db.rawQuery("SELECT * FROM " + tabel_name + " WHERE " + row_status + "=" + "'Dikembalikan'", null);
        return cur;
    }



    public Cursor oneData(long id){
        Cursor cur = db.rawQuery("SELECT * FROM " + tabel_name + " WHERE " + row_id + "=" + id, null);
        return cur;
    }


    public void insertData(ContentValues values){
        db.insert(tabel_name, null, values);
    }


    public void updateData(ContentValues values, long id){
        db.update(tabel_name, values, row_id + "=" + id, null);
    }


    // Sort by Borrower Name
    public Cursor sortDataByName() {
        return db.rawQuery("SELECT * FROM " + tabel_name + " ORDER BY " + row_nama + " ASC", null);
    }


    // Sort by Date Borrowed
    public Cursor sortDataByDateBorrowed() {
        return db.rawQuery("SELECT * FROM " + tabel_name + " ORDER BY " + row_pinjam + " DESC", null);
    }

    // Sort by Date Returned
    public Cursor sortDataByDateReturned() {
        return db.rawQuery("SELECT * FROM " + tabel_name + " ORDER BY " + row_kembali + " ASC", null);
    }

    // Sort by Book Name (Judul)
    public Cursor sortDataByBookName() {
        return db.rawQuery("SELECT * FROM " + tabel_name + " ORDER BY " + row_judul + " ASC", null);
    }
}


