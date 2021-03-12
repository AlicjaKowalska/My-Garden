package com.example.mygarden.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mygarden.Add;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    Context context;

    private static String DATABASE_NAME="MyPlantsDB.db";//nazwa bazy danych
    private static int DATABASE_VERSION=1;
    private static String createTableQuery = "create table my_plants (/*_id integer primary key autoincrement," + " */name TEXT" + ",localization TEXT" + ",species TEXT" + ",notes TEXT" + ", image BLOB)";

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        try {
            MyDB.execSQL(createTableQuery);  //kolumny tabeli my_plants
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists my_plants");
    }

    public void storeImage(Plant plant)
    {
        try{
                SQLiteDatabase objectSqLiteDatabase=this.getWritableDatabase();
                Bitmap imageToStoreBitmap = plant.getImage();

                objectByteArrayOutputStream=new ByteArrayOutputStream();
                imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);

                imageInBytes=objectByteArrayOutputStream.toByteArray();
                ContentValues objectContentValues=new ContentValues();

                objectContentValues.put("name", plant.getName());
                objectContentValues.put("localization", plant.getLocalization());
                objectContentValues.put("species", plant.getSpecies());
                objectContentValues.put("notes", plant.getNotes());
                objectContentValues.put("image", imageInBytes);

                long checkIfQueryRuns=objectSqLiteDatabase.insert("my_plants",null, objectContentValues);
                if(checkIfQueryRuns!=-1){
                    Toast.makeText(context,"Dodano roślinę",Toast.LENGTH_SHORT).show();
                    objectSqLiteDatabase.close();
                }
                else{
                    Toast.makeText(context,"Nie udało się dodać rośliny",Toast.LENGTH_SHORT).show();
                }
        }
        catch(Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Plant> getAllPlantsData(){
            SQLiteDatabase objectSqLiteDatabase=this.getReadableDatabase();
            ArrayList<Plant> plantArrayList=new ArrayList<>();

            Cursor objectCursor=objectSqLiteDatabase.rawQuery("select * from my_plants", null);
            if(objectCursor.getCount()!= -1){
                while(objectCursor.moveToNext()){
                    String nameOfImage=objectCursor.getString(0);
                    String localization=objectCursor.getString(1);
                    String species=objectCursor.getString(2);
                    String notes=objectCursor.getString(3);
                    byte [] imageBytes = objectCursor.getBlob(4);

                    Bitmap objectBitmap= BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                    plantArrayList.add(new Plant(nameOfImage,localization, species, notes, objectBitmap));
                }

                return plantArrayList;
            }
            else{
                Toast.makeText(context, "Nie ma roślin", Toast.LENGTH_SHORT).show();
                return null;
            }
    }

    /*public Boolean insertData(String name, String localization, String species, String notes, Bitmap image){ //Wypełnianie tabeli
            SQLiteDatabase MyDB = this.getWritableDatabase();

            Bitmap imageToStoreBitmap = image;
            objectByteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
            imageInBytes = objectByteArrayOutputStream.toByteArray();

            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("localization", localization);
            contentValues.put("species", species);
            contentValues.put("notes", notes);
            contentValues.put("image", imageInBytes);

            long result = MyDB.insert("my_plants", null, contentValues); //contentValues wkładamy do tabeli my_plants

            if (result != 0) return true;
            else
                return false;
    }

    public Boolean updateData(String name, String localization, String species, String notes){ //edytowanie tabeli
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("localization", localization);
        contentValues.put("species", species);
        contentValues.put("notes", notes);
        Cursor cursor = MyDB.rawQuery("Select * from my_plants where name=?", new String[] {name});
        if (cursor.getCount()>0) {

            long result = MyDB.update("my_plants", contentValues, "name=?", new String[]{name}); //contentValues wkładamy do tabeli my_plants

            if (result == -1) return false;
            else
                return true;
        }
        else
        {
            return false;
        }
    }


    public Boolean deleteData(String name){ //usuwanie z tabeli
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from my_plants where name=?", new String[] {name});
        if (cursor.getCount()>0) {

            long result = MyDB.delete("my_plants", "name=?", new String[]{name});

            if (result == -1) return false;
            else
                return true;
        }
        else
        {
            return false;
        }
    }


    public Cursor getData() { //wyświetlanie tabeli
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from my_plants", null);
        return cursor;
    }*/

}
