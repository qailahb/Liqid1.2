package com.example.liqid20;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

public class MainActivity extends AppCompatActivity  {

    private static final String[] lists = new String[]{ "QFLOW-VI-LOT", "QFLOW-VI-LOT1", "QFLOW-VI-LOT2", "QFLOW-VI-LOT3", "QFLOW-VI-LOT4", "Add New List" };

    // initialisation of hashmap
    //private Map<String, List<DataModel>> optionToListMap = new HashMap<>();
    EditText etSpeed, etTravel, etWait;
    ImageButton buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatAutoCompleteTextView saveSelect = findViewById(R.id.listSaveSelect);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, lists);
        saveSelect.setThreshold(1);
        saveSelect.setAdapter(adapter);

        // Initialises SeekBars and EditTexts
        SeekBar sbSpeed = findViewById(R.id.seekBarSpeed);
        SeekBar sbTravel = findViewById(R.id.seekBarTravel);
        SeekBar sbWait = findViewById(R.id.seekBarWait);

        EditText etForce = findViewById(R.id.valueForce);

        // Initialises button
        Button buttonRun = findViewById(R.id.buttonRun);

        etSpeed = findViewById(R.id.Speed);
        etTravel = findViewById(R.id.Travel);
        etWait = findViewById(R.id.Wait);


        // Links SeekBar and EditText pairs
        linkSeekBarAndEditText(sbSpeed, etSpeed);
        linkSeekBarAndEditText(sbTravel, etTravel);
        linkSeekBarAndEditText(sbWait, etWait);

        buttonRun.setOnClickListener(view -> {
            // Code to set ESP32 output pin high/low to send data and start force calculation
            // etForce.setText(getString(R.string.mockForce)); [for testing purposes]

            Toast.makeText(MainActivity.this, "Run started", Toast.LENGTH_SHORT).show();

            // Force input enabled after test
            etForce.setEnabled(true);

        });

        Button buttonDashboard = findViewById(R.id.buttonDashboard);
        buttonDashboard.setOnClickListener(v -> openDashboard(saveSelect.getText().toString().trim()));

        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if the force value has been entered by the user
                if (etForce.isEnabled()) {
                    // Your code to save data or perform actions with the entered force value
                    String userForceInput = etForce.getText().toString();

                    // Check if the user input is not empty
                    if (!userForceInput.isEmpty()) {
                        // Now you can use the user input (userForceInput) as needed
                        // For example, you can display a Toast with the input
                        Toast.makeText(MainActivity.this, "User input: " + userForceInput, Toast.LENGTH_SHORT).show();
                    } else {
                        // If the input is empty, you can provide a message to the user
                        Toast.makeText(MainActivity.this, "Please enter force value", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Notify the user to press "Run" first
                    Toast.makeText(MainActivity.this, "Press 'Run' first", Toast.LENGTH_SHORT).show();
                }

                MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
                myDB.addList(Integer.parseInt(etSpeed.getText().toString().trim()),
                            Integer.parseInt(etTravel.getText().toString().trim()),
                            Integer.parseInt(etWait.getText().toString().trim()),
                            Integer.parseInt(etForce.getText().toString().trim()));

                // Retrieve selected item from listSaveSelect
                String selectedList = saveSelect.getText().toString().trim();

                //Pass selected item to DashboardActivity
                openDash(selectedList);
            }
        });
    }

    public void openDash(String selectedList) {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        intent.putExtra("SELECTED_LIST", selectedList);
        //startActivity(intent);
    }

    public void openDashboard(String selectedList) {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        intent.putExtra("SELECTED_LIST", selectedList);
        startActivity(intent);
    }

    // SeekBarChangeListener
    private void linkSeekBarAndEditText(SeekBar sb, EditText et) {
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                et.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Action for when SeekBar tracking starts
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Action for when SeekBar tracking stops
            }
        });

        // TextWatcher for EditText to update based on scrollbar values
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int j, int k) {
                // Do something before text changes
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int j, int k) {
                // Do something when text changes
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int progress = Integer.parseInt(editable.toString());
                    // Sets up the progress of the SeekBar
                    sb.setProgress(progress);
                }
                catch (NumberFormatException e) {
                    // Case for when text can't be converted to an integer
                }
            }
        });
    }
}
