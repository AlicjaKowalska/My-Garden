package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.DatabaseAccess;
import com.example.mygarden.database.Plant;
import com.example.mygarden.database.Task;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class PlantInfo extends AppCompatActivity {

    private static final String CHANNEL_ID = "notification_id";
    TextView name, localization, species, notes;
    TextView info, water, fertilizer, repot, local;
    String plant_name, plant_localization, plant_species, plant_notes;
    ImageView photo, localPic;
    int id;

    DBHelper DB;
    Plant plant;

    public static ArrayList<com.example.mygarden.Notification> notifications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);

        localPic = (ImageView) findViewById(R.id.sun);

        name = (TextView) findViewById(R.id.plantinfo_name);
        localization = (TextView) findViewById(R.id.plantinfo_localization);
        species = (TextView) findViewById(R.id.plantinfo_species);
        notes = (TextView) findViewById(R.id.plantinfo_notes);
        photo = (ImageView) findViewById(R.id.img_plant);

        info = (TextView) findViewById(R.id.info);
        water = (TextView) findViewById(R.id.water);
        fertilizer = (TextView) findViewById(R.id.fertilizer);
        repot = (TextView) findViewById(R.id.repot);
        local = (TextView) findViewById(R.id.localization);

        DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        DB = new DBHelper(this);
        ArrayList<Plant> plantArrayList = DB.getAllPlantsData();

        try {
            id = getIntent().getIntExtra("keyid", 0);
            plant = plantArrayList.get(id);

            plant_name = getIntent().getStringExtra("keyname");
            plant_localization = getIntent().getStringExtra("keylocalization");
            plant_species = getIntent().getStringExtra("keyspecies");
            plant_notes = getIntent().getStringExtra("keynotes");
            Bitmap plant_photo = plant.getImage();

            name.setText(plant_name);
            localization.setText(plant_localization);
            species.setText(plant_species);
            notes.setText(plant_notes);
            photo.setImageBitmap(plant_photo);

            databaseAccess.open();
            String plant_info = databaseAccess.getInfo(plant_species);
            String plant_water = databaseAccess.getWater(plant_species);
            String plant_fertilizer = databaseAccess.getFertilizer(plant_species);
            String plant_repot = databaseAccess.getRepot(plant_species);
            String plant_local = databaseAccess.getLocal(plant_species);

            info.setText(plant_info);
            water.setText(plant_water);
            fertilizer.setText(plant_fertilizer);
            repot.setText(plant_repot);
            local.setText(plant_local);

            Drawable penumbra = getDrawable(R.drawable.penumbra);
            Drawable direct = getDrawable(R.drawable.direct);
            Drawable shadow = getDrawable(R.drawable.shadow);
            if(plant_local.equals("cień")) localPic.setImageDrawable(shadow);
            if(plant_local.equals("półcień")) localPic.setImageDrawable(penumbra);
            if(plant_local.equals("bezpośredni")) localPic.setImageDrawable(direct);

            databaseAccess.close();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        Button previous_button = findViewById(R.id.previous_r);
        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlantInfo.this, Plants.class);
                startActivity(i);
            }
        });

        Button edit_button = findViewById(R.id.edit_plant);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlantInfo.this, Edit.class);
                i.putExtra("editid", id);
                i.putExtra("editname", plant_name);
                i.putExtra("editlocalization", plant_localization);
                i.putExtra("editspecies", plant_species);
                i.putExtra("editnotes", plant_notes);
                startActivity(i);
            }
        });
    }

    ///////////////////////////////////////delete data//////////////////////////////////////////////
    public void deleteData(View view){
        try{
                DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
                DB = new DBHelper(this);
                ArrayList<Plant> plantArrayList = DB.getAllPlantsData();

                id = getIntent().getIntExtra("keyid", 0);
                plant = plantArrayList.get(id);
                int plantid=plant.getId();

                DB.deleteData(String.valueOf(plantid));
                Intent i = new Intent(PlantInfo.this, Plants.class);
                startActivity(i);

                ///////////////notifications////////////////////////////////////////////////////////
                DatabaseAccess databaseAccess2=DatabaseAccess.getInstance(getApplicationContext());

                databaseAccess.open();
                int[] wfr = databaseAccess.getWFR(plant_species);
                int water = wfr[0] *24* 60*60*1000;
                int fertilizer = wfr[1]*24* 60*60*1000;
                int  repot = wfr[2]*24* 60*60*1000;
                databaseAccess.close();


                int j=0;
                if(notifications.size()>0) {
                    while (j < notifications.size()) {
                        if (notifications.get(j).getPlantID()==plantid) {
                            int id = notifications.get(j).getNotificationID();
                            createNotificationChannel();
                            generateNotification(plant.getId(),null,R.drawable.watercan,name.getText().toString(), localization.getText().toString(),"Woda", " potrzebuje wody", id, water);
                            generateNotification(plant.getId(),null,R.drawable.fertilizer,name.getText().toString(), localization.getText().toString(),"Nawóz", " potrzebuje nawozu", id+1, fertilizer);
                            generateNotification(plant.getId(),null,R.drawable.repot,name.getText().toString(), localization.getText().toString(),"Przesadzanie", " potrzebuje przesadzenia", id+2, repot);
                            NotificationManagerCompat.from(this).cancel(id);
                            NotificationManagerCompat.from(this).cancel(id+1);
                            NotificationManagerCompat.from(this).cancel(id+2);
                            com.example.mygarden.Notification notif = new com.example.mygarden.Notification(plant.getId(),id);
                            PlantInfo.notifications.remove(notif);
                        }
                        j++;
                    }
                }

                ////////////////////////////////////////////////////////////////////////////////////

        }
        catch (Exception e){
            Toast.makeText(this, "Nie udało się usunąć rośliny", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Channel";
            String description = "Channel for notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = null;
            channel = new NotificationChannel("notificationID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void generateNotification(int plantid,byte[] image,int notificationicon, String name, String localization, String activity, String activitydetails, int id, int time){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND,00);
        long timeInMillis = time;//86400000=1 dzień

        Intent intent = new Intent(this, ReminderBroadcast.class);
        intent.putExtra("keynotificationicon", notificationicon);
        intent.putExtra("keyname", name);
        intent.putExtra("keyactivity", activity);
        intent.putExtra("keyactivitydetails", activitydetails);
        intent.putExtra("keyid", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);


        Intent intent2 = new Intent(this, TaskBroadcast.class);
        intent2.putExtra("keyname",name);
        intent2.putExtra("keyactivity", activity);
        intent2.putExtra("keylocalization", localization);
        intent2.putExtra("keyphoto", image);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, id, intent2, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), timeInMillis, pendingIntent); //AlarmManager.INTERVAL_DAY
        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), timeInMillis, pendingIntent2);
        alarmManager.cancel(pendingIntent2);
    }
}