package com.example.mygarden;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    //nazwa bazy danych
    public static final String DBNAME="PlantsDB";

    public DBHelper(Context context) {
        super(context, "PlantsDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table my_plants(name TEXT primary key, localization TEXT, species TEXT, notes TEXT)");  //kolumny tabeli my_plants
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists my_plants");
    }

    public Boolean insertData(String name, String localization, String species, String notes){ //Wypełnianie tabeli
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("localization", localization);
        contentValues.put("species", species);
        contentValues.put("notes", notes);
        long result = MyDB.insert("my_plants", null, contentValues); //contentValues wkładamy do tabeli my_plants

        if (result==-1) return false;
        else
            return true;
    }
}
