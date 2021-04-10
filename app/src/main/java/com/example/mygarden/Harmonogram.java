package com.example.mygarden;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygarden.database.DBHelper;

import java.util.Locale;

public class Harmonogram extends AppCompatActivity {

    DBHelper DB;
    RecyclerView objectRecyclerView;
    RVAdapter_h objectRvAdapter;

    TextView notasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_harmonogram);

        try{
            objectRecyclerView=findViewById(R.id.tasksRV);
            DB = new DBHelper(this);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try{
            if(!DB.getAllTasks().isEmpty()) {
                objectRvAdapter = new RVAdapter_h(DB.getAllTasks(), this);
                objectRecyclerView.setHasFixedSize(true);

                objectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                objectRecyclerView.setAdapter(objectRvAdapter);
            }
            else{
                notasks = (TextView) findViewById(R.id.notasks);
                notasks.setText("Brak zadań na dziś");
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        ///////////////////////////////settings/////////////////////////////////////////////////////
        Button settings_button = findViewById(R.id.settings_harmonogram);
        settings_button.setOnClickListener(v -> {
            Intent i = new Intent(Harmonogram.this, Settings.class);
            startActivity(i);
            overridePendingTransition(0,0 );
        });

        ///////////////////////////////menu/////////////////////////////////////////////////////////
        Button harmonogram_button = findViewById(R.id.harmonogram);
        harmonogram_button.setOnClickListener(v -> {
            Intent i = new Intent(Harmonogram.this, Harmonogram.class);
            startActivity(i);
            overridePendingTransition(0,0 );
        });

        Button add_button = findViewById(R.id.add);
        add_button.setOnClickListener(v -> {
            Intent i = new Intent(Harmonogram.this, Add.class);
            startActivity(i);
            overridePendingTransition(0,0 );
        });

        Button plants_button = findViewById(R.id.plants);
        plants_button.setOnClickListener(v -> {
            Intent i = new Intent(Harmonogram.this, Plants.class);
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