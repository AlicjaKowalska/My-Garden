package com.example.mygarden;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.DatabaseAccess;
import com.example.mygarden.database.Plant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Edit extends AppCompatActivity {

    public Uri imageFilePath;
    public Bitmap imageToStore;
    private static final int PICK_IMAGE_REQUEST=100;

    TextView name, localization, notes;
    Spinner species;
    ImageView photo;
    int edit_id;

    String edit_name, edit_localization, edit_species, edit_notes;
    Bitmap edit_photo;

    DBHelper DB;
    Plant plant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 50 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        ////////////////////////////////spinner///////////////////////////////////////////////
        this.species = (Spinner) findViewById(R.id.edit_spinner);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<String> plant_species = databaseAccess.getSpecies();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, plant_species);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        this.species.setAdapter(adapter);
        //////////////////////////////////////////////////////////////////////////////////////

        name = (TextView) findViewById(R.id.edit_name);
        localization = (TextView) findViewById(R.id.edit_localization);
        notes = (TextView) findViewById(R.id.edit_notes);
        photo = (ImageView) findViewById(R.id.edit_image);

        edit_id = getIntent().getIntExtra("editid", 0);
        DatabaseAccess dbAccess=DatabaseAccess.getInstance(getApplicationContext());
        DB = new DBHelper(this);
        ArrayList<Plant> plantArrayList = DB.getAllPlantsData();
        plant = plantArrayList.get(edit_id);

        edit_name = getIntent().getStringExtra("editname");
        edit_localization = getIntent().getStringExtra("editlocalization");
        edit_species = getIntent().getStringExtra("editspecies");
        edit_notes = getIntent().getStringExtra("editnotes");
        edit_photo = plant.getImage();

        name.setText(edit_name);
        localization.setText(edit_localization);
        //species.setContentDescription(edit_species);
        notes.setText(edit_notes);
        photo.setImageBitmap(edit_photo);

        Button previous_button = findViewById(R.id.previous_edit);
        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    ///////////////////////////////////////update data//////////////////////////////////////////////
    public void updateData(View view){
        try{
            if(!name.getText().toString().isEmpty() && !localization.getText().toString().isEmpty() && !notes.getText().toString().isEmpty() && photo.getDrawable()!=null && imageToStore!=null){
                edit_id = getIntent().getIntExtra("editid", 0);
                DatabaseAccess dbAccess=DatabaseAccess.getInstance(getApplicationContext());
                DB = new DBHelper(this);
                ArrayList<Plant> plantArrayList = DB.getAllPlantsData();
                plant = plantArrayList.get(edit_id);

                plant.setName(name.getText().toString());
                plant.setLocalization(localization.getText().toString());
                plant.setSpecies(species.getSelectedItem().toString());
                plant.setNotes(notes.getText().toString());
                plant.setImage(imageToStore);
                DB.updateData(plant);
                Intent i = new Intent(Edit.this, Plants.class);
                startActivity(i);
            }
            else{
                Toast.makeText(this, "Wypełnij wszystkie pola i dodaj zdjęcie", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /////////////////////////////////////////image//////////////////////////////////////////////
    public void editImage(View view){
        try {
            Intent objectIntent=new Intent();
            objectIntent.setType("image/*");

            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, PICK_IMAGE_REQUEST);
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);

                photo.setImageBitmap(imageToStore);
            }
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
}

