package com.example.liqid20;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    MyDatabaseHelper myDB;
    ArrayList<String> reading_id;
    ArrayList<Float> reading_speed, reading_travel, reading_wait, reading_force;
    CustomAdapter customAdapter;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Check and request WRITE_EXTERNAL_STORAGE permission if needed
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE
            );
        }

        Button exportButton = findViewById(R.id.buttonExport);
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDataToCsv();
            }
        });
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

        storeData(selectedList);

        customAdapter = new CustomAdapter(DashboardActivity.this, reading_id, reading_speed, reading_travel, reading_wait, reading_force);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
    }

    void storeData(String selectedList) {
        Cursor cursor = myDB.readData(selectedList);
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            // added as a fix
            //int startPosition = reading_id.size();

            while (cursor.moveToNext()) {
                reading_id.add(cursor.getString(0));
                reading_speed.add(Float.parseFloat(cursor.getString(1))); // Update to Float
                reading_travel.add(Float.parseFloat(cursor.getString(2))); // Update to Float
                reading_wait.add(Float.parseFloat(cursor.getString(3)));   // Update to Float
                reading_force.add(Float.parseFloat(cursor.getString(4)));
            }

            Log.d("DashboardActivity", "reading_id: " + reading_id.toString());
            Log.d("DashboardActivity", "reading_speed: " + reading_speed.toString());
            Log.d("DashboardActivity", "reading_travel: " + reading_travel.toString());
            Log.d("DashboardActivity", "reading_wait: " + reading_wait.toString());
            Log.d("DashboardActivity", "reading_force: " + reading_force.toString());

            //customAdapter.notifyItemInserted(startPosition);
            //customAdapter.notifyItemRangeInserted(startPosition, reading_id.size() - startPosition);
        }
    }

    private void exportDataToCsv() {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        Cursor cursor = dbHelper.getDataFromTable();

        if (cursor != null) {
            try {
                // Directory in the Documents folder
                File directory = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), "LIQID");

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // File in the directory
                File file = new File(directory, "exported_data.csv");

                // Writes data to CSV file
                writeDataToCsv(cursor, file);

                Toast.makeText(this, "Data exported to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } finally {
                cursor.close();
            }
        }
    }


    private void writeDataToCsv(Cursor cursor, File file) {
        try {
            FileWriter fw = new FileWriter(file);
            CSVWriter csvWriter = new CSVWriter(fw);

            // Writes column names to CSV file
            csvWriter.writeNext(cursor.getColumnNames());

            // Writes data rows to CSV file
            while (cursor.moveToNext()) {
                String[] row = new String[cursor.getColumnCount()];
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    row[i] = cursor.getString(i);
                }
                csvWriter.writeNext(row);
            }

            csvWriter.close();
            fw.close();
        } catch (IOException e) {
            Log.e("CSV Export", "Error writing CSV", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportDataToCsv();
            } else {
                Toast.makeText(this, "Permission denied. Cannot export data.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}