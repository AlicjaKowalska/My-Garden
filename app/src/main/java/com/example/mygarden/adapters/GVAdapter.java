package com.example.mygarden.adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mygarden.R;
import com.example.mygarden.database.Plant;

import java.util.ArrayList;

public class GVAdapter extends BaseAdapter {

    ArrayList<Plant> plantList;
    Context context;

    public GVAdapter(ArrayList<Plant> plantList, Context context) {
        this.plantList = plantList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return plantList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_gv,parent,false);

        TextView name = view1.findViewById(R.id.nameGV);
        ImageView image = view1.findViewById(R.id.imageGV);

        Plant plant = plantList.get(position);

        name.setText(plant.getName());
        Glide.with(image.getContext()).load(plant.getImage()).into(image);

        return view1;
    }
}
