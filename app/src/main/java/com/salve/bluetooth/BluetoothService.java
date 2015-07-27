package com.salve.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.salve.bluetooth.connectThreads.AcceptThread;
import com.salve.bluetooth.connectThreads.ConnectThread;
import com.salve.bluetooth.connectThreads.ConnectedThread;
import com.salve.contacts.ContactInformation;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class BluetoothService {

    // Debugging
    private static final String TAG = "BluetoothService";

    // Name for the SDP record when creating server socket
    public static final String NAME_INSECURE = "BluetoothInsecure";
    public static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    // Member fields
    private final BluetoothAdapter mAdapter;
    //private final Handler mHandler;
    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private ConnectionState connectionState;
    private BluetoothUtilityOps callback;

    /**
     * Constructor.
     *
     * @param callback A Handler to send messages back to the UI Activity
     */
    public BluetoothService(BluetoothUtilityOps callback) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        connectionState = new ConnectionState();
        connectionState.setStateEnum(ConnectionStateEnum.STATE_NONE);
        this.callback = callback;

    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(ConnectionStateEnum state) {
        Log.e(TAG, "setState() " + connectionState.getStateEnum() + " -> " + state);
        connectionState.setStateEnum(state);
    }

    /**
     * Return the current connection state.
     */
    public synchronized ConnectionStateEnum getState() {
        return connectionState.getStateEnum();
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.e(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(ConnectionStateEnum.STATE_LISTEN);

        // Start the thread to listen on a BluetoothServerSocket
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread(false, this);
            mInsecureAcceptThread.start();
        }
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        Log.e(TAG, "connect to: " + device);

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device, secure, this);
        mConnectThread.start();
        setState(ConnectionStateEnum.STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     */
    public synchronized void connected(BluetoothSocket socket, final String socketType) {
        Log.e(TAG, "connected, Socket Type:" + socketType);

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket, socketType, this);
        mConnectedThread.start();
        setState(ConnectionStateEnum.STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        Log.e(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mInsecureAcceptThread != null) {
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }
        setState(ConnectionStateEnum.STATE_NONE);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param userProfile The bytes to write
     */
    public void write(ContactInformation userProfile) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (connectionState.getStateEnum() != ConnectionStateEnum.STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        Log.e(TAG, "SENDING: " + userProfile.toString());
        // Perform the write unsynchronized
        try {
            ObjectOutputStream oos = new ObjectOutputStream(r.getMmOutStream());
            oos.writeObject(userProfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     *
     * @param e
     */
    public void connectionFailed(IOException e) {
        // Start the service over to restart listening mode
        Log.e(TAG, "connectionFailed", e);
        BluetoothService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    public void connectionLost() {
        Log.e(TAG, "Connection was lost");
        // Start the service over to restart listening mode
        BluetoothService.this.start();
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public BluetoothAdapter getAdapter() {
        return mAdapter;
    }

    public BluetoothUtilityOps getCallback() {
        return callback;
    }
}

