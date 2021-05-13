package com.example.mygarden;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.Plant;
import com.example.mygarden.database.Task;

public class TaskBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int plantid = intent.getIntExtra("keyplantid",0);

        DBHelper DB = new DBHelper(context);
        Plant plant = DB.getPlant(plantid);

        String name = plant.getName();
        String activity = intent.getStringExtra("keyactivity");
        String localization = plant.getLocalization();
        Bitmap img = plant.getImage();

        Task task = new Task(plantid, activity, name, localization, img);
        DB.addTask(task);
    }
}
