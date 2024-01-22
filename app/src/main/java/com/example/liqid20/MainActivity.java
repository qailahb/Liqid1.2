package com.example.liqid20;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity  {

    private static final String[] lists = new String[]{ "QFLOW-VI-LOT", "QFLOW-VI-LOT1", "QFLOW-VI-LOT2", "QFLOW-VI-LOT3", "QFLOW-VI-LOT4", "Add New List" };

    // initialisation of hashmap
    // private Map<String, List<DataModel>> optionToListMap = new HashMap<>();
    EditText etSpeed, etTravel, etWait, etForce;
    ImageButton buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatAutoCompleteTextView saveSelect = findViewById(R.id.listSaveSelect);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, lists);
        saveSelect.setThreshold(1);
        saveSelect.setAdapter(adapter);

        // Set OnItemClickListener for the AutoCompleteTextView
        saveSelect.setOnItemClickListener((parent, view, position, id) -> {
            String selectedList = (String) parent.getItemAtPosition(position);

            if (selectedList.equals("Add New List")) {
                // Handle the click for "Add New List"
                showPopup(view);
            } else {
                // Handle other list selections if needed
            }
        });

        // Initialises SeekBars and EditTexts
        SeekBar sbSpeed = findViewById(R.id.seekBarSpeed);
        SeekBar sbTravel = findViewById(R.id.seekBarTravel);
        SeekBar sbWait = findViewById(R.id.seekBarWait);

        // Initialises button
        Button buttonRun = findViewById(R.id.buttonRun);

        etSpeed = findViewById(R.id.Speed);
        etTravel = findViewById(R.id.Travel);
        etWait = findViewById(R.id.Wait);
        etForce = findViewById(R.id.valueForce);

        // Implement TextWatcher to ensure proper input format
        DecimalTextWatcher decimalInputTextWatcher = new DecimalTextWatcher(etSpeed);
        etSpeed.addTextChangedListener(decimalInputTextWatcher);

        decimalInputTextWatcher = new DecimalTextWatcher(etTravel);
        etTravel.addTextChangedListener(decimalInputTextWatcher);

        decimalInputTextWatcher = new DecimalTextWatcher(etWait);
        etWait.addTextChangedListener(decimalInputTextWatcher);

        decimalInputTextWatcher = new DecimalTextWatcher(etForce);
        etForce.addTextChangedListener(decimalInputTextWatcher);

        TextInputLayout textInputLayout2 = findViewById(R.id.textInputLayout2);
        TextInputLayout textInputLayout3 = findViewById(R.id.textInputLayout3);
        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout);

        etSpeed.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && etSpeed.getText().length() == 0) {
                textInputLayout2.setHint("Speed [mm/s]");
            } else if (!hasFocus && etSpeed.getText().length() == 0) {
                textInputLayout2.setHint("Speed");
            }
        });

        etTravel.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && etTravel.getText().length() == 0) {
                textInputLayout3.setHint("Travel [mm]");
            } else if (!hasFocus && etTravel.getText().length() == 0) {
                textInputLayout3.setHint("Travel");
            }
        });

        etWait.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && etWait.getText().length() == 0) {
                textInputLayout.setHint("Wait [mm]");
            } else if (!hasFocus && etWait.getText().length() == 0) {
                textInputLayout.setHint("Wait");
            }
        });

        etSpeed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                float speedValue = Float.parseFloat(editable.toString());
                float speed_max = 15;

                if (editable.length() > 0) {
                    // User has started typing, update hint
                    textInputLayout2.setHint("Speed [mm/s]");

                    if (speedValue > speed_max) {
                        textInputLayout2.setError("Value too high (max: " + speed_max + " + mm");
                    } else {
                        textInputLayout2.setError(null);
                    }

                } else {
                    // No input, reset to default hint
                    textInputLayout2.setHint("Speed");
                }
            }
        });

        etTravel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    // User has started typing, update hint
                    textInputLayout3.setHint("Travel [mm]");
                } else {
                    // No input, reset to default hint
                    textInputLayout3.setHint("Travel");
                }

                try {
                    float travelValue = Float.parseFloat(editable.toString());
                    // Max allowed value
                    float travel_max = 15;

                    if (travelValue > travel_max) {
                        textInputLayout3.setError("Value too high (max: " + travel_max + " + mm");
                    } else {
                        textInputLayout3.setError(null);
                    }
                } catch (NumberFormatException e) {
                    // Handle the case when text can't be converted to a float
                    textInputLayout3.setError("Invalid input");
                }
            }
        });

        etWait.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    // User has started typing, update hint
                    textInputLayout.setHint("Wait [mm]");
                } else {
                    // No input, reset to default hint
                    textInputLayout.setHint("Wait");
                }
                try {
                    float waitValue = Float.parseFloat(editable.toString());
                    // Max allowed value
                    float wait_max = 15;

                    if (waitValue > wait_max) {
                        textInputLayout.setError("Value too high (max: " + wait_max + " + mm");
                    } else {
                        textInputLayout.setError(null);
                    }
                } catch (NumberFormatException e) {
                    // Handle the case when text can't be converted to a float
                    textInputLayout.setError("Invalid input");
                }
            }
        });

        etTravel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    // User has started typing, update hint
                    textInputLayout3.setHint("Travel [mm]");
                } else {
                    // No input, reset to default hint
                    textInputLayout3.setHint("Travel");
                }

                try {
                    float travelValue = Float.parseFloat(editable.toString());
                    // Max allowed value
                    float travel_max = 15;

                    if (travelValue > travel_max) {
                        textInputLayout3.setError("Value too high (max: " + travel_max + " + mm");
                    } else {
                        textInputLayout3.setError(null);
                    }
                } catch (NumberFormatException e) {
                    // Handle the case when text can't be converted to a float
                    textInputLayout3.setError("Invalid input");
                }
            }
        });

        // Links SeekBar and EditText pairs
        linkSeekBarAndEditText(sbSpeed, etSpeed);
        linkSeekBarAndEditText(sbTravel, etTravel);
        linkSeekBarAndEditText(sbWait, etWait);

        buttonRun.setOnClickListener(view -> {
            // Code to set ESP32 output pin high/low to send data and start force calculation

            // etForce.setText(getString(R.string.mockForce)); [for testing purposes]
            // Toast.makeText(MainActivity.this, "Run started", Toast.LENGTH_SHORT).show();
            // etForce.setEnabled(true); [Force input enabled after test]

        });

        Button buttonDashboard = findViewById(R.id.buttonDashboard);
        buttonDashboard.setOnClickListener(v -> openDashboard(saveSelect.getText().toString().trim()));

        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checks if force value has been entered by user
                if (etForce.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter force value", Toast.LENGTH_SHORT).show();
                }
                else {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);

                    // Retrieve selected item from listSaveSelect
                    String selectedList = saveSelect.getText().toString().trim();

                    myDB.addList(selectedList,
                            Float.parseFloat(etSpeed.getText().toString().trim()),
                            Float.parseFloat(etTravel.getText().toString().trim()),
                            Float.parseFloat(etWait.getText().toString().trim()),
                            Float.parseFloat(etForce.getText().toString().trim()));

                    //Pass selected item to DashboardActivity
                    // openDash(selectedList);
                }
            }
        });
    }

    private void showPopup(View anchorView) {
        // Create a PopupWindow with a custom layout
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup, null);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // Enable background dimming
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000"))); // Dark gray background color with 50% opacity

        // Set soft input mode to adjustResize for automatic keyboard pop-up
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Set up your popup content and functionality
        EditText editTextNewListName = popupView.findViewById(R.id.textInputNewList);
        Button buttonSaveNewList = popupView.findViewById(R.id.buttonSaveNewList);

        buttonSaveNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the save button click
                String newListName = editTextNewListName.getText().toString().trim();

                if (!newListName.isEmpty()) {
                    // Add the new list name to the 'lists' array
                    lists[lists.length - 2] = newListName;

                    // Notify the AutoCompleteTextView adapter about the data change
                    AppCompatAutoCompleteTextView saveSelect = findViewById(R.id.listSaveSelect);

                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) saveSelect.getAdapter();
                    adapter.notifyDataSetChanged();

                    // Dismiss the popup
                    popupWindow.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a name for the new list", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Get the root view of the activity
        View rootView = getWindow().getDecorView().getRootView();

        // Calculate the center of the screen
        int[] location = new int[2];
        rootView.getLocationOnScreen(location);
        int centerX = location[0] + rootView.getWidth() / 2;
        int centerY = location[1] + rootView.getHeight() / 2;

        // Show the popup at the center of the screen
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
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
