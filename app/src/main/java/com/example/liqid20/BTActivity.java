package com.example.liqid20;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Set;

public class BTActivity extends AppCompatActivity {
    private ListView listview;
    private ArrayAdapter aAdapter;
    BluetoothAdapter bAdapter;

    // private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter(); - deprecated method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> openMainActivity());

        Button buttonPair = (Button) findViewById(R.id.buttonPair);
        buttonPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
                } else {
                    if (ActivityCompat.checkSelfPermission(BTActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
                    ArrayList list = new ArrayList();
                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            if (ActivityCompat.checkSelfPermission(BTActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions (requests missing permissions)
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            String devicename = device.getName();
                            String macAddress = device.getAddress();
                            list.add("Name: "+devicename+"MAC Address: "+macAddress);
                        }

                        // change variable names
                        listview = (ListView) findViewById(R.id.deviceList);
                        aAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                        listview.setAdapter(aAdapter);
                    }
                }
            }
        });
    }

    public void openMainActivity() {
        Intent intent = new Intent(BTActivity.this, MainActivity.class);
        startActivity(intent);
    }

    // still need BT discover, pair and message to confirm connection
}

