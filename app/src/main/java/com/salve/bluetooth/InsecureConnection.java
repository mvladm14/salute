package com.salve.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by rroxa_000 on 6/18/2015.
 */
public class InsecureConnection {
     private static final String TAG = "BLUETOOTH";
     private BluetoothAdapter bluetoothAdapter;
     private BluetoothDevice bluetoothDevice;
     private BluetoothSocket socket;
    private void setUpAdapter()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) return;

        bluetoothDevice = bluetoothAdapter.getRemoteDevice("1C:62:B8:C3:C2:08");
        if(bluetoothDevice==null) {
            Log.e(this.toString(), "Bluetooth not avaialable!");
        }
    }


    public boolean btConnect(String MAC) {
        // Set up the Bluetooth Connection
        // MAC needs to be uppercase
        // MAC has the format of XX:XX:XX ... (it NEEDS the colons)
        setUpAdapter();
        MAC = MAC.toUpperCase();

        bluetoothDevice = bluetoothAdapter.getRemoteDevice(MAC);
        // UUID for Serial Port Protocol
        UUID mUUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
        // Connect based on API Version
        // Open the socket
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentApiVersion >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            // This is an API 10 command (Gingerbread); it will obviously fail on
            // an API 8 (Froyo) device.
            try {
                socket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(mUUID);

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        } else {
            // This is an an API 5 call, so Froyo should handle this!
            try {
                socket = bluetoothDevice.createRfcommSocketToServiceRecord(mUUID);
            } catch (IOException e1) {
                e1.printStackTrace();
                return false;
            }
        }

        // Lets connect the socket
        try {
            socket.connect();
             socket.getInputStream();
             socket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
            // If we didn't connect to the socket, the user is going to have a bad time.
            return false;
        }
        // If we made it this far, everything must have worked. Huzzah!
        return true;
    }


}

