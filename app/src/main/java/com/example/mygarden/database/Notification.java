package com.example.mygarden.database;

import android.graphics.Bitmap;

public class Notification {

    int plantID, notificationID;

    public Notification(){};

    public Notification(int plantID, int notificationID)
    {
        this.plantID = plantID;
        this.notificationID = notificationID;
    }

    public int getPlantID() { return plantID; }
    public void setPlantID(int plantID) { this.plantID = plantID; }

    public int getNotificationID() { return notificationID; }
    public void setNotificationID(int notificationID) { this.notificationID = notificationID; }
}
