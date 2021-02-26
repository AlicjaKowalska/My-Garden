package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlantInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);

        Button previous_button = findViewById(R.id.previous_r); //poprawić przycisk cofania
        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlantInfo.this, Plants.class);
                startActivity(i);
            }
        });

        Button settings_button = findViewById(R.id.edit_plant);
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlantInfo.this, Edit.class);
                startActivity(i);
            }
        });
    }
}