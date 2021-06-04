package com.example.mygarden.model;

public class Notification {

    int plantID, notificationID;

    public Notification(int plantID, int notificationID)
    {
        this.plantID = plantID;
        this.notificationID = notificationID;
    }

    public int getPlantID() { return plantID; }

    public int getNotificationID() { return notificationID; }
}
