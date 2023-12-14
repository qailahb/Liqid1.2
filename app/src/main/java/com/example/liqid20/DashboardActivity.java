package com.example.liqid20;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    MyDatabaseHelper myDB;
    ArrayList<String> reading_id, reading_speed, reading_travel, reading_wait, reading_force;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Retrieve the selected item from the Intent extra
        String selectedList = getIntent().getStringExtra("SELECTED_LIST");

        // Use the selected item as a label (you can set it to a TextView or any other UI element)
        TextView listLabel = findViewById(R.id.listLabel);
        listLabel.setText(selectedList);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        myDB = new MyDatabaseHelper(DashboardActivity.this);

        reading_id = new ArrayList<>();
        reading_speed = new ArrayList<>();
        reading_travel = new ArrayList<>();
        reading_wait = new ArrayList<>();
        reading_force = new ArrayList<>();

        storeData();

        customAdapter = new CustomAdapter(DashboardActivity.this, reading_id, reading_speed, reading_travel, reading_wait, reading_force);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
    }

    void storeData() {
        Cursor cursor = myDB.readData();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                reading_id.add(cursor.getString(0));
                reading_speed.add(cursor.getString(1));
                reading_travel.add(cursor.getString(2));
                reading_wait.add(cursor.getString(3));
                reading_force.add(cursor.getString(4));
            }

            Log.d("DashboardActivity", "reading_id: " + reading_id.toString());
            Log.d("DashboardActivity", "reading_speed: " + reading_speed.toString());
            Log.d("DashboardActivity", "reading_travel: " + reading_travel.toString());
            Log.d("DashboardActivity", "reading_wait: " + reading_wait.toString());
            Log.d("DashboardActivity", "reading_force: " + reading_force.toString());
        }
    }

}