package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygarden.database.DBHelper;

import java.io.ByteArrayOutputStream;

public class PlantInfo extends AppCompatActivity {

    TextView name, localization, species, notes;
    ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);

        name = (TextView) findViewById(R.id.plantinfo_name);
        localization = (TextView) findViewById(R.id.plantinfo_localization);
        species = (TextView) findViewById(R.id.plantinfo_species);
        notes = (TextView) findViewById(R.id.plantinfo_notes);
        photo = (ImageView) findViewById(R.id.img_plant);

        try {
            String plant_name = getIntent().getStringExtra("keyname");
            String plant_localization = getIntent().getStringExtra("keylocalization");
            String plant_species = getIntent().getStringExtra("keyspecies");
            String plant_notes = getIntent().getStringExtra("keynotes");
            //Bitmap plant_photo = getIntent().getParcelableExtra("keyphoto");

            name.setText(plant_name);
            localization.setText(plant_localization);
            species.setText(plant_species);
            notes.setText(plant_notes);
            //photo.setImageBitmap(plant_photo);
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
                startActivity(i);
            }
        });
    }
}