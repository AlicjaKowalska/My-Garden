package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.mygarden.adapters.GVAdapter;
import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.Plant;

import java.util.ArrayList;
import java.util.Locale;

public class Plants extends AppCompatActivity {

    DBHelper DB;
    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_plants);

        DB = new DBHelper(this);
        ArrayList<Plant> plantList = DB.getAllPlantsData();

        gridview = findViewById(R.id.gv);
        GVAdapter gvAdapter = new GVAdapter(plantList,this);
        gridview.setAdapter(gvAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Plant plant = plantList.get(position);
                Intent intent = new Intent(Plants.this, PlantInfo.class);
                intent.putExtra("keyid", plant.getId());
                startActivity(intent);
                overridePendingTransition(0,0 );
            }
        });
        
        /*try{
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
        }*/

        ///////////////////////////////////menu///////////////////////////////////
        Button settings_button = findViewById(R.id.settings_plants);
        settings_button.setOnClickListener(v -> {
            Intent i = new Intent(Plants.this, Settings.class);
            startActivity(i);
            overridePendingTransition(0,0 );
        });

        Button harmonogram_button = findViewById(R.id.harmonogram);
        harmonogram_button.setOnClickListener(v -> {
            Intent i = new Intent(Plants.this, Harmonogram.class);
            startActivity(i);
            overridePendingTransition(0,0 );
        });

        Button add_button = findViewById(R.id.add);
        add_button.setOnClickListener(v -> {
            Intent i = new Intent(Plants.this, Add.class);
            startActivity(i);
            overridePendingTransition(0,0 );
        });

        Button plants_button = findViewById(R.id.plants);
        plants_button.setOnClickListener(v -> {
            Intent i = new Intent(Plants.this, Plants.class);
            startActivity(i);
            overridePendingTransition(0,0 );
        });
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
}