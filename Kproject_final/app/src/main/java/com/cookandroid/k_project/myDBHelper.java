package com.cookandroid.k_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myDBHelper extends SQLiteOpenHelper {
    public myDBHelper(Context context) {
        super(context, "weloDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE loginTBL ( ID CHAR(20) PRIMARY KEY,PW CHAR(20),NAME CHAR(20),NUMBER CHAR(20),EMAIL CHAR(50),HOMEADDRESS CHAR(200),JOBADDRESS CHAR(200),BIRTH CHAR(20));");
        db.execSQL("CREATE TABLE locationTBL(gName CHAR(30) PRIMARY KEY, locationX NUMERIC, locationY NUMERIC);");
        db.execSQL("CREATE TABLE savelocationTBL(gName CHAR(30) PRIMARY KEY, latitude, longitude);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS loginTBL");
        db.execSQL("DROP TABLE IF EXISTS locationTBL");
        db.execSQL("DROP TABLE IF EXISTS savelocationTBL");
        onCreate(db);

    }
}

