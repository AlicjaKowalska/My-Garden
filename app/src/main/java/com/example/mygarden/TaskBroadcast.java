package com.example.mygarden;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.SharedPreferences;

import com.example.mygarden.database.Task;

public class TaskBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("keyname");
        String activity = intent.getStringExtra("keyactivity");
        String localization = intent.getStringExtra("keylocalization");
        byte[] photo = intent.getByteArrayExtra("keyphoto");

        Bitmap plant_img = BitmapFactory.decodeByteArray(photo, 0 , photo.length);
        //Bitmap plant_img = BitmapFactory.decodeResource(context.getResources(), R.drawable.plant_photo);

        Task task = new Task(activity, name, localization, plant_img);
        Harmonogram.taskArrayList.add(task);
    }
}
