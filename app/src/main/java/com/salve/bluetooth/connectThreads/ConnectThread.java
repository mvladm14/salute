package com.salve.bluetooth.connectThreads;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.salve.bluetooth.BluetoothService;

import java.io.IOException;

public class ConnectThread extends Thread {

    private static final String TAG = "ConnectThread";

    private final BluetoothSocket mmSocket;
    private String mSocketType;
    private BluetoothService service;

    public ConnectThread(BluetoothDevice device, boolean secure, BluetoothService service) {
        this.service = service;

        BluetoothSocket tmp = null;

        mSocketType = secure ? "Secure" : "Insecure";

        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            tmp = device.createInsecureRfcommSocketToServiceRecord(
                    BluetoothService.MY_UUID_INSECURE);
        } catch (IOException e) {
            Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        Log.e(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
        setName("ConnectThread" + mSocketType);

        // Always cancel discovery because it will slow down a connection
        service.getAdapter().cancelDiscovery();

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mmSocket.connect();
        } catch (IOException e) {
            // Close the socket
            try {
                Log.e(TAG, "Unable to connect. Socket will now close");
                mmSocket.close();
            } catch (IOException e2) {
                Log.e(TAG, "unable to close() " + mSocketType +
                        " socket during connection failure", e2);
            }
            service.connectionFailed(e);
            return;
        }

        // Start the connected thread
        service.connected(mmSocket, mSocketType);
    }

    public void cancel() {
        try {
            Log.e(TAG, "Closing socket");
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
        }
    }
}

