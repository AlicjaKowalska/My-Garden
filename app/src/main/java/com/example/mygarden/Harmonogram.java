package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.Task;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;

public class Harmonogram extends AppCompatActivity {

    DBHelper DB;
    RecyclerView objectRecyclerView;
    RVAdapter_h objectRvAdapter;
    public static ArrayList<Task> taskArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harmonogram);

        try{
            objectRecyclerView=findViewById(R.id.tasksRV);
            DB = new DBHelper(this);

            //SaveIntoSharedPrefs("TaskList", objectRecyclerView);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try{
            if(!taskArrayList.isEmpty()) {
                objectRvAdapter = new RVAdapter_h(taskArrayList, this);
                objectRecyclerView.setHasFixedSize(true);

                objectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                objectRecyclerView.setAdapter(objectRvAdapter);
            }
            else{
                Toast.makeText(this, "Brak zadań na dziś", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        ///////////////////////////////settings/////////////////////////////////////////////////////
        Button settings_button = findViewById(R.id.settings_harmonogram);
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Harmonogram.this, Settings.class);
                startActivity(i);
            }
        });

        ///////////////////////////////menu/////////////////////////////////////////////////////////
        Button harmonogram_button = findViewById(R.id.harmonogram);
        harmonogram_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Harmonogram.this, Harmonogram.class);
                startActivity(i);
            }
        });

        Button add_button = findViewById(R.id.add);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Harmonogram.this, Add.class);
                startActivity(i);
            }
        });

        Button plants_button = findViewById(R.id.plants);
        plants_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Harmonogram.this, Plants.class);
                startActivity(i);
            }
        });

    }

    /*public void SaveIntoSharedPrefs(String key, RecyclerView value){
        SharedPreferences sp = this.getSharedPreferences("SP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public boolean Update(String key){
        SharedPreferences sp = this.getSharedPreferences("SP",Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }*/
}