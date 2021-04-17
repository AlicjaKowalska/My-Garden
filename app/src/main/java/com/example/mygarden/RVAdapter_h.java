package com.example.mygarden;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygarden.database.DBHelper;
import com.example.mygarden.database.Task;

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

        rvViewHolderClass.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(rvViewHolderClass.checkbox.isChecked()) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Wykonać zadanie?");
                alertDialogBuilder.setPositiveButton("TAK",
                        (arg0, arg1) -> {
                            DBHelper DB = new DBHelper(context);
                            DB.deleteTask(String.valueOf(task.getId()));
                            Intent intent = new Intent(context, Harmonogram.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                            ((Activity) context).overridePendingTransition(0, 0);
                        });
                alertDialogBuilder.setNegativeButton("NIE", ((dialog, which) -> {
                    rvViewHolderClass.checkbox.toggle();
                }));
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
