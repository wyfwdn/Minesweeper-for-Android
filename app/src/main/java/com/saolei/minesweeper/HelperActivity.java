package com.saolei.minesweeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Administrator on 2017/4/29.
 */

public class HelperActivity extends SQLiteOpenHelper{
    private static final String DB_NAME = "record.db";
    public static final String TBL_NAME = "RECORDTbl";
    private static final String CREATE_TBL = " create table " + " RECORDTbl(_id integer primary key autoincrement,result text,mode text, time integer) ";
    private SQLiteDatabase db;
    HelperActivity(Context context) {
        super(context, DB_NAME, null, 2);
    }
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_TBL);
    }
    public void insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TBL_NAME, null, values);
        db.close();
    }
    public Cursor query_battle() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"time"}, "mode like ?", new String[]{"%Battle%"}, null, null, null);
        return cursor;
    }
    public Cursor query_flags_wins() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"count(_id)"}, "result like ? and mode like ?" , new String[]{"%Win%","%Flags%"}, null, null, null);
        return cursor;
    }
    public Cursor query_flags_total() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"count(_id)"},  "mode like ?", new String[]{"%Flags%"}, null, null, null);
        return cursor;
    }
    public Cursor query_flags_best() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"min(time)"},  "result like ? and mode like ?", new String[]{"%Win%","%Flags%"}, null, null, null);
        return cursor;
    }
    public Cursor query_normal_wins() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"count(_id)"}, "result like ? and mode like ?" , new String[]{"%Win%","%Normal%"}, null, null, null);
        return cursor;
    }
    public Cursor query_normal_total() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"count(_id)"},  "mode like ?", new String[]{"%Normal%"}, null, null, null);
        return cursor;
    }
    public Cursor query_normal_best() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"min(time)"},  "result like ? and mode like ?", new String[]{"%Win%","%Normal%"}, null, null, null);
        return cursor;
    }
    public Cursor query_pvp_wins() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"count(_id)"}, "result like ? and mode like ?" , new String[]{"%Win%","%PVP%"}, null, null, null);
        return cursor;
    }
    public Cursor query_pvp_total() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"count(_id)"},  "mode like ?", new String[]{"%PVP%"}, null, null, null);
        return cursor;
    }
    public Cursor query_pvp_best() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"min(time)"},  "result like ? and mode like ?", new String[]{"%Win%","%PVP%"}, null, null, null);
        return cursor;
    }
    public Cursor query_adv_wins() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"count(_id)"}, "result like ? and mode like ?" , new String[]{"%Win%","%Adventure%"}, null, null, null);
        return cursor;
    }
    public Cursor query_adv_total() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"count(_id)"},  "mode like ?", new String[]{"%Adventure%"}, null, null, null);
        return cursor;
    }
    public Cursor query_adv_best() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"min(time)"},  "result like ? and mode like ?", new String[]{"%Win%","%Adventure%"}, null, null, null);
        return cursor;
    }
    public Cursor query_hunt_wins() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"count(_id)"}, "result like ? and mode like ?" , new String[]{"%Win%","%Hunt%"}, null, null, null);
        return cursor;
    }
    public Cursor query_hunt_total() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"count(_id)"},  "mode like ?", new String[]{"%Hunt%"}, null, null, null);
        return cursor;
    }
    public Cursor query_hunt_best() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, new String[]{"min(time)"},  "result like ? and mode like ?", new String[]{"%Win%","%Hunt%"}, null, null, null);
        return cursor;
    }

    public void delete(int id) {
        if (db == null)
            db = getWritableDatabase();
        db.delete(TBL_NAME, "_id=?", new String[] { String.valueOf(id) });
    }
    public void close() {
        if (db != null)
            db.close();
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
