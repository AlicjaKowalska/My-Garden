package com.example.mygarden;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.DatabaseAccess;
import com.example.mygarden.database.Plant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Add extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText name, localization, notes;
    Spinner spinner;
    ImageView photo;
    DBHelper DB;

    public Uri imageFilePath;
    public Bitmap imageToStore;
    private static final int PICK_IMAGE_REQUEST=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 50 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            photo = (ImageView) findViewById(R.id.image);
            name = (EditText) findViewById(R.id.plant_name);
            localization = (EditText) findViewById(R.id.plant_localization);
            notes = (EditText) findViewById(R.id.plant_notes);
            DB = new DBHelper(this);
        }
        catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        ////////////////////////////////spinner///////////////////////////////////////////////
        this.spinner = (Spinner) findViewById(R.id.spinner);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<String> plant_species = databaseAccess.getSpecies();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, plant_species);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        this.spinner.setAdapter(adapter);
        //////////////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////button - previous////////////////////////////////////////
        Button previous_button = findViewById(R.id.previous_add);
        previous_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    /////////////////////////////////////////image//////////////////////////////////////////////
    public void chooseImage(View view){
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
    ////////////////////////////////////store data//////////////////////////////////////////////////
    public void storeData(View view){
        try{
            if(name.getText().toString().isEmpty()){
                Toast.makeText(this, "Podaj nazwę rośliny", Toast.LENGTH_SHORT).show();
            }
            else if(localization.getText().toString().isEmpty()){
                Toast.makeText(this, "Podaj lokalizację rośliny", Toast.LENGTH_SHORT).show();
            }
            else if(!name.getText().toString().isEmpty() && !localization.getText().toString().isEmpty() && photo.getDrawable()!=null && imageToStore!=null){
                Plant plant = new Plant(name.getText().toString(),localization.getText().toString(),spinner.getSelectedItem().toString(),notes.getText().toString(),imageToStore);
                DB.storeData(plant);
                Intent i = new Intent(Add.this, Plants.class);
                startActivity(i);
            }
            else{
                Bitmap plant_img = BitmapFactory.decodeResource(this.getResources(), R.drawable.plant_photo);
                Plant plant = new Plant(name.getText().toString(),localization.getText().toString(),spinner.getSelectedItem().toString(),notes.getText().toString(),plant_img);
                DB.storeData(plant);
                Intent i = new Intent(Add.this, Plants.class);
                startActivity(i);
            }
            
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////spinner///////////////////////////////////////////////////////
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
}