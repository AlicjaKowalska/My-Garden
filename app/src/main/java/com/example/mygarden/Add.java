package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Add extends AppCompatActivity {

    EditText name, localization, species, notes;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = (EditText) findViewById(R.id.plant_name);
        localization = (EditText) findViewById(R.id.plant_localization);
        species = (EditText) findViewById(R.id.plant_species);
        notes = (EditText) findViewById(R.id.plant_notes);
        DB = new DBHelper(this);

        Button check_button = findViewById(R.id.check_add);
        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam = name.getText().toString();
                String local = localization.getText().toString();
                String spec = species.getText().toString();
                String not = notes.getText().toString();

                if (nam.equals(""))
                    Toast.makeText(Add.this, "Podaj nazwę", Toast.LENGTH_SHORT).show();
                else if (local.equals(""))
                    Toast.makeText(Add.this, "Podaj lokalizację", Toast.LENGTH_SHORT).show();
                else if (spec.equals(""))
                    Toast.makeText(Add.this, "Podaj gatunek", Toast.LENGTH_SHORT).show();
                else {
                    Intent i = new Intent(Add.this, PlantInfo.class);
                    startActivity(i);
                }
            }
        });

        Button previous_button = findViewById(R.id.previous_add);
        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button edit_photo_button = findViewById(R.id.edit_plant);
        edit_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //zrobienie zdjęcia/ wybranie z galerii
            }
        });

    }
}