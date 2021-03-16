package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.DatabaseAccess;
import com.example.mygarden.database.Plant;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class PlantInfo extends AppCompatActivity {

    TextView name, localization, species, notes;
    TextView info, water, fertilizer, repot, local;
    String plant_name, plant_localization, plant_species, plant_notes;
    ImageView photo;
    int id;

    DBHelper DB;
    Plant plant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);

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
                //id = getIntent().getIntExtra("keyid", 0)+1;

                DB.deleteData(String.valueOf(plantid));
                Intent i = new Intent(PlantInfo.this, Plants.class);
                startActivity(i);
                Toast.makeText(this, "Usunięto roślinę: "+ plant.getId(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, "Nie udało się usunąć rośliny", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Nie udało się usunąć rośliny", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}