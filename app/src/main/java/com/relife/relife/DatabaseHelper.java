package com.relife.relife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper  {

    private static final String DB_NAME="GROCERYLIST";
    private static final int DB_VERSION=1;
    public static Cursor cursor;

    public DatabaseHelper(Context context){

        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE SCHEDULE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "name TEXT, "+
                "activity INTEGER, "+
                "hour INTEGER, " +
                "day INTEGER, "+
                "year INTEGER, "+
                "month INTEGER);");
        db.execSQL("CREATE TABLE ACTIVITY (_id INTEGER PRIMARY KEY, name TEXT, path TEXT);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public static void insertSchedule(SQLiteDatabase db, String name, int activity, int hour, int day, int month, int year){
        ContentValues contactValues=new ContentValues();
        contactValues.put("name", name);
        contactValues.put("activity", activity);
        contactValues.put("hour", hour);
        contactValues.put("day", day);
        contactValues.put("month", month);
        contactValues.put("year", year);
        db.insert("SCHEDULE", null, contactValues);
    }
    public static void insertActivity(SQLiteDatabase db, String name, String path){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("path", path);
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
    public static Cursor list_items(SQLiteDatabase db, Context context){
        Cursor cursor = null;
        try{
            cursor = db.query("SCHEDULE", new String[] {"name", "activity", "hour", "day", "month", "year"}, null, null, null, null, "year ASC, month ASC, day ASC, hour ASC");
        }
        catch(SQLiteException e){
            Toast toast = Toast.makeText(context, "Database Unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return cursor;
    }
    public static Cursor listActivities(SQLiteDatabase db, Context context){
        Cursor cursor = null;
        try{
            cursor = db.query("ACTIVITY", new String[] {"name", "path"}, null, null, null, null, null);
        }
        catch(SQLiteException e){
            Toast toast = Toast.makeText(context, "Database Unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return cursor;
    }



}
