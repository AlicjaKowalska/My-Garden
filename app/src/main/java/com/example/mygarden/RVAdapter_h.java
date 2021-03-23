package com.example.mygarden;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygarden.database.DatabaseAccess;
import com.example.mygarden.database.Plant;
import com.example.mygarden.database.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RVAdapter_h extends RecyclerView.Adapter<RVAdapter_h.RVViewHolderClassH>{
    ArrayList<Task> taskList;
    Context context;

    public RVAdapter_h(ArrayList<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public RVAdapter_h.RVViewHolderClassH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RVAdapter_h.RVViewHolderClassH(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_row_h,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter_h.RVViewHolderClassH rvViewHolderClass, int i) {
        Task task = taskList.get(i);

        rvViewHolderClass.activity.setText(task.getActivity());
        rvViewHolderClass.name.setText(task.getName());
        rvViewHolderClass.localization.setText(task.getLocalization());
        rvViewHolderClass.image.setImageBitmap(task.getImage());

        rvViewHolderClass.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                taskList.remove(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class RVViewHolderClassH extends RecyclerView.ViewHolder
    {
        CheckBox checkbox;
        TextView activity;
        TextView name;
        TextView localization;
        ImageView image;
        RelativeLayout layout;
        public RVViewHolderClassH(View itemView){
            super(itemView);

            checkbox = itemView.findViewById(R.id.checkBox);
            activity = itemView.findViewById(R.id.sr_h_imageDetailsTV);
            name =itemView.findViewById(R.id.sr_h_name);
            localization =itemView.findViewById(R.id.sr_h_localization);
            image =itemView.findViewById(R.id.sr_h_imageIV);

            layout=itemView.findViewById(R.id.sr_layout);
        }
    }

}
