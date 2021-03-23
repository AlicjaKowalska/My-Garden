package com.example.mygarden.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    Context context;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getSpecies() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT species FROM plants", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String getInfo(String species) {
        String info = new String();
        String spec = new String();
        Cursor cursor = database.rawQuery("SELECT * FROM plants", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            spec=cursor.getString(0);
            if(spec.equals(species)){
                info=cursor.getString(5);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return info;
    }

    public String getWater(String species) {
        String water = new String();
        String spec = new String();
        Cursor cursor = database.rawQuery("SELECT * FROM plants", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            spec=cursor.getString(0);
            if(spec.equals(species)){
                water=cursor.getString(1);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return water;
    }

    public String getFertilizer(String species) {
        String fertilizer = new String();
        String spec = new String();
        Cursor cursor = database.rawQuery("SELECT * FROM plants", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            spec=cursor.getString(0);
            if(spec.equals(species)){
                fertilizer=cursor.getString(2);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return fertilizer;
    }

    public String getRepot(String species) {
        String repot = new String();
        String spec = new String();
        Cursor cursor = database.rawQuery("SELECT * FROM plants", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            spec=cursor.getString(0);
            if(spec.equals(species)){
                repot=cursor.getString(3);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return repot;
    }

    public String getLocal(String species) {
        String local = new String();
        String spec = new String();
        Cursor cursor = database.rawQuery("SELECT * FROM plants", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            spec=cursor.getString(0);
            if(spec.equals(species)){
                local=cursor.getString(4);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return local;
    }

    public int[] getWFR(String species) {
        int[] wfr = new int[3];
        String spec = new String();
        Cursor cursor = database.rawQuery("SELECT * FROM plants", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            spec=cursor.getString(0);
            if(spec.equals(species)){
                wfr[0]=cursor.getInt(1); //water
                wfr[1]=cursor.getInt(2); //fertilizer
                wfr[2]=cursor.getInt(3); //repot
            }
            cursor.moveToNext();
        }
        cursor.close();
        return wfr;
    }
}