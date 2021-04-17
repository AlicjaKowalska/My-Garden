package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.DatabaseAccess;
import com.example.mygarden.database.Notification;
import com.example.mygarden.database.Plant;
import com.example.mygarden.database.Task;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PlantInfo extends AppCompatActivity {

    private static final String CHANNEL_ID = "notification_id";
    TextView name, localization, species, notes;
    TextView info, water, fertilizer, repot, local;
    String plant_name, plant_localization, plant_species, plant_notes;
    ImageView photo, localPic;
    int id;

    DBHelper DB;
    Plant plant;

    public static ArrayList<Notification> notifications = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_plant_info);

        ScrollView SVparent = findViewById(R.id.scrollView2);
        SVparent.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        });
        ScrollView SVchild = findViewById(R.id.notesSV);
        SVchild.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(shouldRequestDisallowIntercept((ViewGroup) v, event));
            return false;
        });

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

        try {
            id = getIntent().getIntExtra("keyid", 0);
            plant = DB.getPlant(id);

            plant_name = plant.getName();
            plant_localization = plant.getLocalization();
            plant_species = plant.getSpecies();
            plant_notes = plant.getNotes();
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

            @SuppressLint("UseCompatLoadingForDrawables") Drawable penumbra = getDrawable(R.drawable.penumbra);
            @SuppressLint("UseCompatLoadingForDrawables") Drawable direct = getDrawable(R.drawable.direct);
            @SuppressLint("UseCompatLoadingForDrawables") Drawable shadow = getDrawable(R.drawable.shadow);
            if(plant_local.equals("cień")) localPic.setImageDrawable(shadow);
            if(plant_local.equals("półcień")) localPic.setImageDrawable(penumbra);
            if(plant_local.equals("bezpośrednie światło")) localPic.setImageDrawable(direct);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        /////////////////////////////water dialog///////////////////////////////////////////////////
        ImageView watering = findViewById(R.id.water_activity);
        watering.setOnClickListener((View.OnClickListener) v -> {
            final Dialog dialog = new Dialog(PlantInfo.this);
            dialog.setContentView(R.layout.water_dialog);
            Button dialogButton = (Button) dialog.findViewById(R.id.water_button);
            EditText water_number = (EditText) dialog.findViewById(R.id.water_number);
            dialogButton.setOnClickListener(v12 -> {
                int wn = Integer.parseInt(water_number.getText().toString());
                databaseAccess.updateWater(plant_species, wn);
                int water = wn *24* 60*60*1000;

                int id2 = (int) System.currentTimeMillis();
                Notification notif = new Notification(plant.getId(),id2);
                PlantInfo.notifications.add(notif);
                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                plant.getImage().compress(Bitmap.CompressFormat.JPEG, 0 , blob);
                byte[] picture = blob.toByteArray();
                int j=0;
                if(PlantInfo.notifications.size()>0) {
                    while (j < PlantInfo.notifications.size()) {
                        if (PlantInfo.notifications.get(j).getPlantID()==plant.getId()) {
                            int id = PlantInfo.notifications.get(j).getNotificationID();
                            createNotificationChannel();
                            generateNotification(plant.getId(),picture,R.drawable.watercan,plant.getName(), plant.getLocalization(),"Woda", " potrzebuje wody", id, water,true);
                            NotificationManagerCompat.from(PlantInfo.this).cancel(id);
                            Notification notif1 = new Notification(plant.getId(),id);
                            PlantInfo.notifications.remove(notif1);
                            createNotificationChannel();
                            generateNotification(plant.getId(),picture,R.drawable.watercan,plant.getName(), plant.getLocalization(),"Woda", " potrzebuje wody", id2, water,false);
                            }
                        j++;
                    }
                }

                dialog.dismiss();
                recreate();
            });

            ImageButton water_exit = (ImageButton) dialog.findViewById(R.id.water_exit);
            water_exit.setOnClickListener(v13 -> dialog.dismiss());

            dialog.show();
        });

        ///////////////////////////////fertilizer dialog////////////////////////////////////////////
        ImageView fertilizing = findViewById(R.id.fertilizer_activity);
        fertilizing.setOnClickListener((View.OnClickListener) v -> {
            final Dialog dialog = new Dialog(PlantInfo.this);
            dialog.setContentView(R.layout.fertilizer_dialog);
            Button dialogButton = (Button) dialog.findViewById(R.id.fertilizer_button);
            EditText fertilizer_number = (EditText) dialog.findViewById(R.id.fertilizer_number);
            dialogButton.setOnClickListener(v1 -> {
                int fn = Integer.parseInt(fertilizer_number.getText().toString());
                databaseAccess.updateFertilizer(plant_species, fn);
                int fertilizer = fn *24* 60*60*1000;

                int id2 = (int) System.currentTimeMillis();
                Notification notif = new Notification(plant.getId(),id2);
                PlantInfo.notifications.add(notif);
                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                plant.getImage().compress(Bitmap.CompressFormat.JPEG, 0 , blob);
                byte[] picture = blob.toByteArray();
                int j=0;
                if(PlantInfo.notifications.size()>0) {
                    while (j < PlantInfo.notifications.size()) {
                        if (PlantInfo.notifications.get(j).getPlantID()==plant.getId()) {
                            int id = PlantInfo.notifications.get(j).getNotificationID();
                            createNotificationChannel();
                            generateNotification(plant.getId(),picture,R.drawable.fertilizer,plant.getName(), plant.getLocalization(),"Nawóz", " potrzebuje nawozu", id, fertilizer,true);
                            NotificationManagerCompat.from(PlantInfo.this).cancel(id);
                            Notification notif1 = new Notification(plant.getId(),id);
                            PlantInfo.notifications.remove(notif1);
                            createNotificationChannel();
                            generateNotification(plant.getId(),picture,R.drawable.fertilizer,plant.getName(), plant.getLocalization(),"Nawóz", " potrzebuje nawozu", id2, fertilizer,false);
                        }
                        j++;
                    }
                }

                dialog.dismiss();
                recreate();
            });

            ImageButton fertilizer_exit = (ImageButton) dialog.findViewById(R.id.fertilizer_exit);
            fertilizer_exit.setOnClickListener(v15 -> dialog.dismiss());

            dialog.show();
        });

        ////////////////////////////repot dialog////////////////////////////////////////////////////
        ImageView repotting = findViewById(R.id.repot_activity);
        repotting.setOnClickListener((View.OnClickListener) v -> {
            final Dialog dialog = new Dialog(PlantInfo.this);
            dialog.setContentView(R.layout.repot_dialog);
            Button dialogButton = (Button) dialog.findViewById(R.id.repot_button);
            EditText repot_number = (EditText) dialog.findViewById(R.id.repot_number);
            dialogButton.setOnClickListener(v14 -> {
                int rn = Integer.parseInt(repot_number.getText().toString());
                databaseAccess.updateRepot(plant_species, rn);
                int repot = rn *24* 60*60*1000;

                int id2 = (int) System.currentTimeMillis();
                Notification notif = new Notification(plant.getId(),id2);
                PlantInfo.notifications.add(notif);
                ByteArrayOutputStream blob = new ByteArrayOutputStream();
                plant.getImage().compress(Bitmap.CompressFormat.JPEG, 0 , blob);
                byte[] picture = blob.toByteArray();
                int j=0;
                if(PlantInfo.notifications.size()>0) {
                    while (j < PlantInfo.notifications.size()) {
                        if (PlantInfo.notifications.get(j).getPlantID()==plant.getId()) {
                            int id = PlantInfo.notifications.get(j).getNotificationID();
                            createNotificationChannel();
                            generateNotification(plant.getId(),picture,R.drawable.repot,plant.getName(), plant.getLocalization(),"Przesadzanie", " potrzebuje przesadzenia", id, repot,true);
                            NotificationManagerCompat.from(PlantInfo.this).cancel(id);
                            Notification notif1 = new Notification(plant.getId(),id);
                            PlantInfo.notifications.remove(notif1);
                            createNotificationChannel();
                            generateNotification(plant.getId(),picture,R.drawable.repot,plant.getName(), plant.getLocalization(),"Przesadzanie", " potrzebuje przesadzenia", id2, repot,false);
                        }
                        j++;
                    }
                }

                dialog.dismiss();
                recreate();
            });

            ImageButton repot_exit = (ImageButton) dialog.findViewById(R.id.repot_exit);
            repot_exit.setOnClickListener(v16 -> dialog.dismiss());

            dialog.show();
        });

        /////////////////////////localization dialog////////////////////////////////////////////////
        ImageView local = findViewById(R.id.localization_activity);
        local.setOnClickListener((View.OnClickListener) v -> {
            final Dialog dialog = new Dialog(PlantInfo.this);
            dialog.setContentView(R.layout.localization_dialog);
            TextView localization_text = (TextView) dialog.findViewById(R.id.localization_text);
            localization_text.setText(databaseAccess.getLocal(plant.getSpecies()));
            String plant_local = databaseAccess.getLocal(plant.getSpecies());
            ImageView localization_photo = (ImageView) dialog.findViewById(R.id.localization_photo);
            @SuppressLint("UseCompatLoadingForDrawables") Drawable penumbra = getDrawable(R.drawable.penumbra);
            @SuppressLint("UseCompatLoadingForDrawables") Drawable direct = getDrawable(R.drawable.direct);
            @SuppressLint("UseCompatLoadingForDrawables") Drawable shadow = getDrawable(R.drawable.shadow);
            if(plant_local.equals("cień")) localization_photo.setImageDrawable(shadow);
            if(plant_local.equals("półcień")) localization_photo.setImageDrawable(penumbra);
            if(plant_local.equals("bezpośrednie światło")) localization_photo.setImageDrawable(direct);

            ImageButton localization_exit = (ImageButton) dialog.findViewById(R.id.localization_exit);
            localization_exit.setOnClickListener(v17 -> dialog.dismiss());

            dialog.show();
        });


        /////////////////////////////////nav bar////////////////////////////////////////////////////
        Button previous_button = findViewById(R.id.previous_r);
        previous_button.setOnClickListener(v -> {
            Intent i = new Intent(PlantInfo.this, Plants.class);
            startActivity(i);
            overridePendingTransition(0,0 );
        });

        Button edit_button = findViewById(R.id.edit_plant);
        edit_button.setOnClickListener(v -> {
            Intent i = new Intent(PlantInfo.this, Edit.class);
            i.putExtra("editid", id);
            startActivity(i);
            overridePendingTransition(0,0 );
        });
    }

    ///////////////////////////////////////delete data//////////////////////////////////////////////
    public void deleteData(View view){
        try{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Usunąć roślinę?");
            alertDialogBuilder.setPositiveButton("TAK",
                    (arg0, arg1) -> {
                DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
                DB = new DBHelper(this);

                id = getIntent().getIntExtra("keyid", 0);
                plant = DB.getPlant(id);
                int plantid=plant.getId();

                DB.deleteData(String.valueOf(plantid));
                Intent i = new Intent(PlantInfo.this, Plants.class);
                startActivity(i);
                overridePendingTransition(0,0 );

                ///////////////notifications////////////////////////////////////////////////////////
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
                            generateNotification(plant.getId(),null,R.drawable.watercan,name.getText().toString(), localization.getText().toString(),"Woda", " potrzebuje wody", id, water,true);
                            generateNotification(plant.getId(),null,R.drawable.fertilizer,name.getText().toString(), localization.getText().toString(),"Nawóz", " potrzebuje nawozu", id+1, fertilizer, true);
                            generateNotification(plant.getId(),null,R.drawable.repot,name.getText().toString(), localization.getText().toString(),"Przesadzanie", " potrzebuje przesadzenia", id+2, repot, true);
                            NotificationManagerCompat.from(this).cancel(id);
                            NotificationManagerCompat.from(this).cancel(id+1);
                            NotificationManagerCompat.from(this).cancel(id+2);
                            Notification notif = new Notification(plant.getId(),id);
                            PlantInfo.notifications.remove(notif);
                        }
                        j++;
                    }
                }
                    });
            alertDialogBuilder.setNegativeButton("NIE", ((dialog, which) -> {

            }));
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
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
            NotificationChannel channel;
            channel = new NotificationChannel("notificationID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void generateNotification(int plantid, byte[] image,int notificationicon, String name, String localization, String activity, String activitydetails, int id, int time, boolean cancel){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);

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


        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), time, pendingIntent); //AlarmManager.INTERVAL_DAY
        if(cancel==true) {
            alarmManager.cancel(pendingIntent);
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), time, pendingIntent2);
        if(cancel==true){
            alarmManager.cancel(pendingIntent2);
            ArrayList<Task> taskList = DB.getAllTasks();
            int j=0;
            if(taskList.size()>0) {
                while (j < taskList.size()) {
                    if (taskList.get(j).getPlantId()==plantid) {
                        DB.deleteTask(String.valueOf(taskList.get(j).getId()));
                    }
                    j++;
                }
            }
        }
    }

    private void setLocale(String lang) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(lang.toLowerCase()));
        } else {
            config.locale = new Locale(lang.toLowerCase());
        }
        resources.updateConfiguration(config, dm);

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("My_Lang", lang).apply();
    }

    public void loadLocale() {
        String language = PreferenceManager.getDefaultSharedPreferences(this).getString("My_Lang", "");
        setLocale(language);
    }

    protected boolean shouldRequestDisallowIntercept(ViewGroup scrollView, MotionEvent event) {
        boolean disallowIntercept = true;
        float yOffset = getYOffset(event);

        if (scrollView instanceof ListView) {
            ListView listView = (ListView) scrollView;
            if (yOffset < 0 && listView.getFirstVisiblePosition() == 0 && listView.getChildAt(0).getTop() >= 0) {
                disallowIntercept = false;
            }
            else if (yOffset > 0 && listView.getLastVisiblePosition() == listView.getAdapter().getCount() - 1 && listView.getChildAt(listView.getChildCount() - 1).getBottom() <= listView.getHeight()) {
                disallowIntercept = false;
            }
        }
        else {
            float scrollY = scrollView.getScrollY();
            disallowIntercept = !((scrollY == 0 && yOffset < 0) || (scrollView.getHeight() + scrollY == scrollView.getChildAt(0).getHeight() && yOffset >= 0));

        }

        return disallowIntercept;
    }

    protected float getYOffset(MotionEvent ev) {
        final int historySize = ev.getHistorySize();
        final int pointerCount = ev.getPointerCount();

        if (historySize > 0 && pointerCount > 0) {
            float lastYOffset = ev.getHistoricalY(pointerCount - 1, historySize - 1);
            float currentYOffset = ev.getY(pointerCount - 1);

            return lastYOffset - currentYOffset;
        }

        return 0;
    }
}