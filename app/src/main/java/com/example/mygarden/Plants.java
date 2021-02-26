package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Plants extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);

        Button settings_button = findViewById(R.id.settings_plants);
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Plants.this, Settings.class);
                startActivity(i);
            }
        });

        Button harmonogram_button = findViewById(R.id.harmonogram);
        harmonogram_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Plants.this, Harmonogram.class);
                startActivity(i);
            }
        });

        Button add_button = findViewById(R.id.add);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Plants.this, Add.class);
                startActivity(i);
            }
        });

        Button plants_button = findViewById(R.id.plants);
        plants_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Plants.this, Plants.class);
                startActivity(i);
            }
        });
    }


}