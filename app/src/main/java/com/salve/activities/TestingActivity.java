package com.salve.activities;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.salve.R;
import com.salve.bluetooth.BluetoothDevicesFoundResponse;
import com.salve.bluetooth.BluetoothUtilityOps;
import com.salve.contacts.ContactInformation;

import java.util.List;

public class TestingActivity extends AppCompatActivity implements BluetoothDevicesFoundResponse {

    private static final String TAG = "TestingActivity";

    private BluetoothUtilityOps bluetoothOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing);
        bluetoothOps = BluetoothUtilityOps.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //**********************Bluetooth*****************************

    public void sendContactViaBluetooth(View view) {
        bluetoothOps.sendContactViaBluetooth();
    }

    public void getMyContact(View view) {
        ContactInformation c = ContactInformation.getMyContact(this);
        Log.e(TAG, c.toString());
    }

    @Override
    public void onBluetoothDevicesFound(List<BluetoothDevice> devices) {
        for (BluetoothDevice device : devices) {
            if (device.getName() != null && device.getName().equals(bluetoothOps.getDeviceName())) {
                Log.e(TAG, "HAVE THE SAME NAME ==> try to connect");
                bluetoothOps.connectDevice(device.getAddress());
            }
        }
    }
}

