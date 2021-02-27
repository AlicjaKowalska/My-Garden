package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PlantInfo extends AppCompatActivity {

    TextView name, localization, species, notes;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);

        name = (TextView) findViewById(R.id.textView7);
        localization = (TextView) findViewById(R.id.textView14);
        species = (TextView) findViewById(R.id.textView13);
        notes = (TextView) findViewById(R.id.textView15);
        DB = new DBHelper(this);

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