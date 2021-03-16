package com.example.mygarden;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.Plant;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolderClass>{

    ArrayList<Plant> plantList;
    Context context;

    public RVAdapter(ArrayList<Plant> plantList, Context context) {
        this.plantList = plantList;
        this.context = context;
    }

    @NonNull
    @Override
    public RVViewHolderClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RVViewHolderClass(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_row,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolderClass rvViewHolderClass, int i) {
        Plant plant = plantList.get(i);
        rvViewHolderClass.imageNameTV.setText(plant.getName());
        rvViewHolderClass.localizationTV.setText(plant.getLocalization());
        rvViewHolderClass.speciesTV.setText(plant.getSpecies());
        rvViewHolderClass.notesTV.setText(plant.getNotes());
        rvViewHolderClass.objectImageView.setImageBitmap(plant.getImage());

        rvViewHolderClass.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name = (TextView) v.findViewById(android.R.id.text1);

                Intent intent = new Intent(context, PlantInfo.class);
                intent.putExtra("keyname", plant.getName());
                intent.putExtra("keylocalization", plant.getLocalization());
                intent.putExtra("keyspecies", plant.getSpecies());
                intent.putExtra("keynotes", plant.getNotes());
                intent.putExtra("keyid", i);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public static class RVViewHolderClass extends RecyclerView.ViewHolder
    {

        TextView imageNameTV, localizationTV, speciesTV, notesTV;
        ImageView objectImageView;
        RelativeLayout relative;
        public RVViewHolderClass(View itemView){
            super(itemView);
            imageNameTV=itemView.findViewById(R.id.sr_imageDetailsTV);
            localizationTV=itemView.findViewById(R.id.sr_localization);
            speciesTV=itemView.findViewById(R.id.sr_species);
            notesTV=itemView.findViewById(R.id.sr_notes);
            objectImageView=itemView.findViewById(R.id.sr_imageIV);

            relative=itemView.findViewById(R.id.relative);
        }
    }

}
