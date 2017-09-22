package com.greenshadow.studylocker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sl_db";
    private static final String KEY_ID = "id";
    private static final String TABLE_LOCKEDAPPS = "locked_apps";
    private static final String PACKAGE_NAME = "package_name";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_LOCKEDAPPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + PACKAGE_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCKEDAPPS);
        onCreate(db);
    }

    void addApp(String packageName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PACKAGE_NAME, packageName);

        // Inserting Row
        db.insert(TABLE_LOCKEDAPPS, null, values);
        Log.d("DBHelper",packageName + " inserted in DB");
        db.close();
    }

    // Getting All Contacts
    public List<String> getAllApps() {
        List<String> appList = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOCKEDAPPS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                appList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return appList;
    }


    public boolean isAppPresent(String appName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  * FROM " + TABLE_LOCKEDAPPS + " WHERE " + PACKAGE_NAME + " = '" + appName + "'";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            Log.d("DBHelper",appName + " not in DB");
            return false;
        }
        cursor.close();
        Log.d("DBHelper",appName + " in DB");
        return true;
    }

    public int getAppsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOCKEDAPPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteApp(String appName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCKEDAPPS, KEY_ID + " = ?",
                new String[] {appName});
        Log.d("DBHelper",appName + " deleted from DB");
        db.close();
    }

}