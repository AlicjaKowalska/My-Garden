package com.example.mygarden.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private final SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

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
        String info = "";
        String spec;
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
        String water = "";
        String spec;
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
        String fertilizer = "";
        String spec;
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
        String repot = "";
        String spec;
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
        String local = "";
        String spec;
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
        String spec;
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

    public void updateWater(String species, int w){
        String spec;
        Cursor cursor = database.rawQuery("SELECT * FROM plants", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            spec=cursor.getString(0);
            if(spec.equals(species)){
                ContentValues objectContentValues=new ContentValues();
                objectContentValues.put("water", w);
                database.update("plants",objectContentValues,"species=?", new String[]{species});
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void updateFertilizer(String species, int f){
        String spec;
        Cursor cursor = database.rawQuery("SELECT * FROM plants", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            spec=cursor.getString(0);
            if(spec.equals(species)){
                ContentValues objectContentValues=new ContentValues();
                objectContentValues.put("fertilizer", f);
                database.update("plants",objectContentValues,"species=?", new String[]{species});
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void updateRepot(String species, int r){
        String spec;
        Cursor cursor = database.rawQuery("SELECT * FROM plants", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            spec=cursor.getString(0);
            if(spec.equals(species)){
                ContentValues objectContentValues=new ContentValues();
                objectContentValues.put("repot", r);
                database.update("plants",objectContentValues,"species=?", new String[]{species});
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

}