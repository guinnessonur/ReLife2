package com.relife.relife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper  {

    private static final String DB_NAME="GROCERYLIST";
    private static final int DB_VERSION=1;
    public static Cursor cursor;

    public DatabaseHelper(Context context){

        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE SCHEDULE (_id INTEGER PRIMARY KEY AUTOINCREMENT "+
                "name TEXT, "+
                "activity INTEGER, "+
                "day INTEGER, "+
                "year INTEGER, "+
                "month INTEGER);");
        db.execSQL("CREATE TABLE ACTIVITY (id INTEGER PRIMARY KEY "+
                "name TEXT, "+
                "activity INTEGER);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public static void insertSchedule(SQLiteDatabase db, String name, int activity, int day, int month, int year){
        ContentValues contactValues=new ContentValues();
        contactValues.put("name", name);
        contactValues.put("activity", activity);
        contactValues.put("day", day);
        contactValues.put("month", month);
        contactValues.put("year", year);
        db.insert("SCHEDULE", null, contactValues);
    }
    public static void insertActivity(SQLiteDatabase db, String name, int activity){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("activity", activity);
        db.insert("ACTIVITY", null, contentValues);
    }
    public static Cursor showItems(SQLiteDatabase db,Context context ){

        cursor=db.query("GROCERYLIST",new String[]{"element","state"},null,null,null,null,null);
        return cursor;
    }
    public void update(SQLiteDatabase db, String data,String element,String state,int phoneNumber){
        db.execSQL("Update "+data.toUpperCase()+" SET element='"+element+"', state = '"+state+"' WHERE phoneNumber="+phoneNumber+" ;");
    }
    public static void delete(SQLiteDatabase db, String data, String name){
        db.execSQL("DELETE FROM " + data.toUpperCase() + " WHERE NAME = " + name);
    }



}
