package com.example.mygarden;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
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

    private Uri imageFilePath;
    private Bitmap imageToStore;
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

        ///////////////////////////////button check - insert data/////////////////////////////////////////////////
        //Button check_button = findViewById(R.id.check_add);
        //check_button.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View v) {

                /*String nam = name.getText().toString();
                String local = localization.getText().toString();
                String spec = spinner.getSelectedItem().toString();
                String not = notes.getText().toString();
                Bitmap phot = imageToStore;

                if (nam.equals(""))
                    Toast.makeText(Add.this, "Podaj nazwę", Toast.LENGTH_SHORT).show();
                else if (local.equals(""))
                    Toast.makeText(Add.this, "Podaj lokalizację", Toast.LENGTH_SHORT).show();
                else if (spec.equals(""))
                    Toast.makeText(Add.this, "Podaj gatunek", Toast.LENGTH_SHORT).show();
                else if (phot==null){
                    Toast.makeText(Add.this, "Dodaj zdjęcie", Toast.LENGTH_SHORT).show();
                }
                else {
                    try{
                        boolean checkinsertdata = DB.storeImage(nam, local, spec, not, phot);
                        if (checkinsertdata==true){
                            Toast.makeText(Add.this,"Dodano roślinę", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(Add.this, "Nie udało się dodać rośliny", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch(Exception e){
                        Toast.makeText(Add.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }*/

                    //Intent i = new Intent(Add.this, Plants.class);
                    /*i.putExtra("keyname", nam);
                    i.putExtra("keylocalization", local);
                    i.putExtra("keyspecies", spec);
                    i.putExtra("keynotes", not);
                    i.putExtra("keyphoto", phot);*/
                    //startActivity(i);
                //}
            //}
        //});
        //////////////////////////////////////////////////////////////////////////////////////////////

        ///////////////////////////add image/////////////////////////////////////////////////////////
        /*photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objectIntent=new Intent();
                objectIntent.setType("image/*");

                objectIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(objectIntent, PICK_IMAGE_REQUEST);
            }
        });*/
        /////////////////////////////////////////////////////////////////////////////////////////////

        //DatabaseAccess databaseAccess=DatabaseAccess.getInstance(getApplicationContext());
        //databaseAccess.open();
        //String s = species.getText().toString();
        //String water = databaseAccess.getWater(s);
        //notes.setText(water);
        //databaseAccess.close(); //close database connection


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

    public void storeImage(View view){
        try{
            if(!name.getText().toString().isEmpty() && !localization.getText().toString().isEmpty() && !notes.getText().toString().isEmpty() && photo.getDrawable()!=null && imageToStore!=null){
                DB.storeImage(new Plant(name.getText().toString(),localization.getText().toString(),spinner.getSelectedItem().toString(),notes.getText().toString(),imageToStore));
                Intent i = new Intent(Add.this, Plants.class);
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
    ///////////////////////////////////////////////////////////////////////////////////////////


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