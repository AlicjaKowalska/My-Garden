package com.example.mygarden;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationicon = intent.getIntExtra("keynotificationicon", 0);
        String name = intent.getStringExtra("keyname");
        String activity = intent.getStringExtra("keyactivity");
        String activitydetails = intent.getStringExtra("keyactivitydetails");
        int id = intent.getIntExtra("keyid",0);

        intent = new Intent(context, Harmonogram.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context,"notificationID")
                .setSmallIcon(notificationicon)
                .setContentTitle(activity)
                .setContentText(name + " " + activitydetails)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(id,notification);
    }
}
