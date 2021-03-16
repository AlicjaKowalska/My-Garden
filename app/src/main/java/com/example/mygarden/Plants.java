package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.Plant;

public class Plants extends AppCompatActivity {

    DBHelper DB;
    RecyclerView objectRecyclerView;
    RVAdapter objectRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);
        
        try{
            objectRecyclerView=findViewById(R.id.imagesRV);
            DB = new DBHelper(this);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try{
            objectRvAdapter=new RVAdapter(DB.getAllPlantsData(),this);
            objectRecyclerView.setHasFixedSize(true);

            objectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            objectRecyclerView.setAdapter(objectRvAdapter);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        ///////////////////////////////////menu///////////////////////////////////
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