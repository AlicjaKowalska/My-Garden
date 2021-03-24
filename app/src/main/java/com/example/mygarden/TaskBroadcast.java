package com.example.mygarden;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.SharedPreferences;

import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.Task;
import com.google.gson.Gson;

public class TaskBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int plantid = intent.getIntExtra("keyplantid",0);
        String name = intent.getStringExtra("keyname");
        String activity = intent.getStringExtra("keyactivity");
        String localization = intent.getStringExtra("keylocalization");
        byte[] photo = intent.getByteArrayExtra("keyphoto");

        Bitmap plant_img = BitmapFactory.decodeByteArray(photo, 0 , photo.length);

        Task task = new Task(plantid, activity, name, localization, plant_img);
        DBHelper DB = new DBHelper(context);
        DB.addTask(task);
    }
}
