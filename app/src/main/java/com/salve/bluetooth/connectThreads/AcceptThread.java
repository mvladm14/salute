package com.salve.bluetooth.connectThreads;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.salve.bluetooth.BluetoothService;
import com.salve.bluetooth.ConnectionState;
import com.salve.bluetooth.ConnectionStateEnum;

import java.io.IOException;

/**
 * Created by Vlad on 7/26/2015.
 */
public class AcceptThread extends Thread {

    private static final String TAG = "AcceptThread";

    // The local server socket
    private final BluetoothServerSocket mmServerSocket;
    private String mSocketType;

    private BluetoothService service;
    private ConnectionState connectionState;

    public AcceptThread(boolean secure, BluetoothService service) {
        this.service = service;
        this.connectionState = service.getConnectionState();


        BluetoothServerSocket tmp = null;
        mSocketType = secure ? "Secure" : "Insecure";

        // Create a new listening server socket
        try {
            tmp = service.getAdapter().listenUsingInsecureRfcommWithServiceRecord(
                    BluetoothService.NAME_INSECURE, BluetoothService.MY_UUID_INSECURE);
        } catch (IOException e) {
            Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        Log.e(TAG, "Socket Type: " + mSocketType +
                "BEGIN mAcceptThread" + this);
        setName("AcceptThread" + mSocketType);

        BluetoothSocket socket;

        // Listen to the server socket if we're not connected
        while (connectionState.getStateEnum() != ConnectionStateEnum.STATE_CONNECTED) {
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + " accept() failed", e);
                break;
            }

            // If a connection was accepted
            if (socket != null) {
                Log.e(TAG, "A connection was accepted");
                synchronized (AcceptThread.this) {
                    switch (connectionState.getStateEnum()) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            service.connected(socket, mSocketType);
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                    }
                }
            }
        }
        Log.e(TAG, "END mAcceptThread, socket Type: " + mSocketType);
    }

    public void cancel() {
        try {
            Log.e(TAG, "Closing socket");
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
        }
    }
}
