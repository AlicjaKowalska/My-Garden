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
    private static String createTableQuery = "create table my_plants (_id integer primary key autoincrement," + "name TEXT" + ",localization TEXT" + ",species TEXT" + ",notes TEXT" + ", image BLOB)";

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

    public void storeData(Plant plant)
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

    public void updateData(Plant plant)
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

            long checkIfQueryRuns=objectSqLiteDatabase.update("my_plants",objectContentValues,"_id=?", new String[]{String.valueOf(plant.getId())});
            if(checkIfQueryRuns!=-1){
                Toast.makeText(context,"Edytowano roślinę",Toast.LENGTH_SHORT).show();
                objectSqLiteDatabase.close();
            }
            else{
                Toast.makeText(context,"Nie udało się edytować rośliny",Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteData(String id)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        /*Cursor cursor = MyDB.rawQuery("Select * from my_plants where _id=?", new String[]{String.valueOf(plant.getId())});
        if (cursor.getCount()>0) {
            long result = MyDB.delete("my_plants", "_id=?", new String[]{String.valueOf(plant.getId())});
        }*/
        MyDB.delete("my_plants", "_id=?", new String[] { id });
        MyDB.close();
    }

    public ArrayList<Plant> getAllPlantsData(){
            SQLiteDatabase objectSqLiteDatabase=this.getReadableDatabase();
            ArrayList<Plant> plantArrayList=new ArrayList<>();

            Cursor objectCursor=objectSqLiteDatabase.rawQuery("select * from my_plants", null);
            if(objectCursor.getCount()!= -1){
                while(objectCursor.moveToNext()){
                    Plant plant = new Plant();
                    plant.setId(Integer.parseInt(objectCursor.getString(0)));
                    plant.setName(objectCursor.getString(1));
                    plant.setLocalization(objectCursor.getString(2));
                    plant.setSpecies(objectCursor.getString(3));
                    plant.setNotes(objectCursor.getString(4));
                    byte [] imageBytes = objectCursor.getBlob(5);

                    Bitmap objectBitmap= BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                    plant.setImage(objectBitmap);
                    plantArrayList.add(plant);
                }

                return plantArrayList;
            }
            else{
                Toast.makeText(context, "Nie ma roślin", Toast.LENGTH_SHORT).show();
                return null;
            }
    }

}
