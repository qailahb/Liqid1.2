package com.example.liqid20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    ArrayList<String> reading_id;
    ArrayList<Float> reading_speed, reading_travel, reading_wait, reading_force;

   CustomAdapter(Context context,
                 ArrayList<String> reading_id,
                 ArrayList<Float> reading_speed,
                 ArrayList<Float> reading_travel,
                 ArrayList<Float> reading_wait,
                 ArrayList<Float> reading_force) {

       this.context = context;
       this.reading_id = reading_id;
       this.reading_speed = reading_speed;
       this.reading_travel = reading_travel;
       this.reading_wait = reading_wait;
       this.reading_force = reading_force;
   }


    public static class ViewHolder extends RecyclerView.ViewHolder {

       TextView id_txt, speed_txt, travel_txt, wait_txt, force_txt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_txt = itemView.findViewById(R.id.id_txt);
            speed_txt = itemView.findViewById(R.id.speed_txt);
            travel_txt = itemView.findViewById(R.id.travel_txt);
            wait_txt = itemView.findViewById(R.id.wait_txt);
            force_txt = itemView.findViewById(R.id.force_txt);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        holder.id_txt.setText(String.valueOf(reading_id.get(position)));
        holder.speed_txt.setText(String.valueOf(reading_speed.get(position)));
        holder.travel_txt.setText(String.valueOf(reading_travel.get(position)));
        holder.wait_txt.setText(String.valueOf(reading_wait.get(position)));
        holder.force_txt.setText(String.valueOf(reading_force.get(position)));
    }

    @Override
    public int getItemCount() {
       return reading_id.size();
    }
}
