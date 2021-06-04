package com.example.mygarden.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.mygarden.model.Plant;
import com.example.mygarden.model.Task;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    Context context;

    private static final String DATABASE_NAME="MyPlantsDB.db";//nazwa bazy danych
    private static final int DATABASE_VERSION=1;
    private static final String createTableQuery = "create table my_plants(_id integer primary key autoincrement," + "name TEXT" + ",localization TEXT" + ",species TEXT" + ",notes TEXT" + ", image BLOB)";
    private static final String createTableQuery2 = "create table my_tasks(_idTask integer primary key autoincrement," +"plantID integer"+ ",activity TEXT" + ",name TEXT" + ",localization TEXT" + ", image BLOB)";

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
            MyDB.execSQL(createTableQuery2);  //kolumny tabeli my_tasks
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists my_plants");
        MyDB.execSQL("drop Table if exists my_tasks");
    }

    public void storeData(Plant plant)
    {
        try{
                SQLiteDatabase objectSqLiteDatabase=this.getWritableDatabase();
                Bitmap imageToStoreBitmap = plant.getImage();

                objectByteArrayOutputStream=new ByteArrayOutputStream();
                imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,50,objectByteArrayOutputStream);

                imageInBytes=objectByteArrayOutputStream.toByteArray();
                ContentValues objectContentValues=new ContentValues();

                objectContentValues.put("name", plant.getName());
                objectContentValues.put("localization", plant.getLocalization());
                objectContentValues.put("species", plant.getSpecies());
                objectContentValues.put("notes", plant.getNotes());
                objectContentValues.put("image", imageInBytes);

                long checkIfQueryRuns=objectSqLiteDatabase.insert("my_plants",null, objectContentValues);
                if(checkIfQueryRuns!=-1){
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
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,50,objectByteArrayOutputStream);

            imageInBytes=objectByteArrayOutputStream.toByteArray();
            ContentValues objectContentValues=new ContentValues();

            objectContentValues.put("name", plant.getName());
            objectContentValues.put("localization", plant.getLocalization());
            objectContentValues.put("species", plant.getSpecies());
            objectContentValues.put("notes", plant.getNotes());
            objectContentValues.put("image", imageInBytes);

            long checkIfQueryRuns=objectSqLiteDatabase.update("my_plants",objectContentValues,"_id=?", new String[]{String.valueOf(plant.getId())});
            if(checkIfQueryRuns!=-1){
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
        MyDB.delete("my_plants", "_id=?", new String[] { id });
        MyDB.close();
    }

    public ArrayList<Plant> getAllPlantsData(){
            SQLiteDatabase objectSqLiteDatabase=this.getReadableDatabase();
            ArrayList<Plant> plantArrayList=new ArrayList<>();

            @SuppressLint("Recycle") Cursor objectCursor=objectSqLiteDatabase.rawQuery("select * from my_plants", null);
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
                return null;
            }
    }

    public Plant getPlant(int id){
        SQLiteDatabase DB = this.getWritableDatabase();
        Plant plant = new Plant();
        @SuppressLint("Recycle") Cursor objectCursor = DB.rawQuery("select * from my_plants where _id = ?", new String[]{String.valueOf(id)});
        if(objectCursor.getCount()!=-1){
            while (objectCursor.moveToNext()){
                plant.setId(Integer.parseInt(objectCursor.getString(0)));
                plant.setName(objectCursor.getString(1));
                plant.setLocalization(objectCursor.getString(2));
                plant.setSpecies(objectCursor.getString(3));
                plant.setNotes(objectCursor.getString(4));
                byte [] imageBytes = objectCursor.getBlob(5);
                Bitmap objectBitmap= BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                plant.setImage(objectBitmap);
            }
            return plant;
        }
        else{
            return null;
        }
    }

    ///////////////////////////////////////////tasks////////////////////////////////////////////////
    public void addTask(Task task)
    {
        try{
            SQLiteDatabase objectSqLiteDatabase=this.getWritableDatabase();
            Bitmap imageToStoreBitmap = task.getImage();

            objectByteArrayOutputStream=new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,50,objectByteArrayOutputStream);

            imageInBytes=objectByteArrayOutputStream.toByteArray();
            ContentValues objectContentValues=new ContentValues();

            objectContentValues.put("plantID", task.getPlantId());
            objectContentValues.put("activity", task.getActivity());
            objectContentValues.put("name", task.getName());
            objectContentValues.put("localization", task.getLocalization());
            objectContentValues.put("image", imageInBytes);

            long checkIfQueryRuns=objectSqLiteDatabase.insert("my_tasks",null, objectContentValues);
            if(checkIfQueryRuns!=-1){
                objectSqLiteDatabase.close();
            }
            else{
                Toast.makeText(context,"Nie udało się dodać zadania",Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteTask(String id)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.delete("my_tasks", "_idTask=?", new String[] { id });
        MyDB.close();
    }

    public ArrayList<Task> getAllTasks(){
        SQLiteDatabase objectSqLiteDatabase=this.getReadableDatabase();
        ArrayList<Task> taskArrayList=new ArrayList<>();

        @SuppressLint("Recycle") Cursor objectCursor=objectSqLiteDatabase.rawQuery("select * from my_tasks", null);
        if(objectCursor.getCount()!= -1){
            while(objectCursor.moveToNext()){
                Task task = new Task();
                task.setId(Integer.parseInt(objectCursor.getString(0)));
                task.setPlantId(objectCursor.getInt(1));
                task.setActivity(objectCursor.getString(2));
                task.setName(objectCursor.getString(3));
                task.setLocalization(objectCursor.getString(4));
                byte [] imageBytes = objectCursor.getBlob(5);

                Bitmap objectBitmap= BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
                task.setImage(objectBitmap);
                taskArrayList.add(task);
            }

            return taskArrayList;
        }
        else{
            return null;
        }
    }
}
