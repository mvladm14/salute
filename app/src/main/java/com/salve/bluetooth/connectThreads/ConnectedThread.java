package com.salve.bluetooth.connectThreads;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.salve.bluetooth.BluetoothAdapterName;
import com.salve.bluetooth.BluetoothService;
import com.salve.contacts.ContactInformation;
import com.salve.contacts.ContactsFileManager;
import com.salve.contacts.ImportContact;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

    private static final String TAG = "ConnectedThread";

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    private BluetoothService service;

    public ConnectedThread(BluetoothSocket socket, String socketType, BluetoothService service) {
        Log.e(TAG, "create ConnectedThread: " + socketType);
        this.service = service;
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the BluetoothSocket input and output streams
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "temp sockets not created", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        Log.e(TAG, "BEGIN mConnectedThread");

        if (service.getCallback().isSendingContact()) {
            service.getCallback().sendMyContact();
        }

        // Keep listening to the InputStream while connected
        while (true) {
            try {
                // Read from the InputStream
                ObjectInputStream o = new ObjectInputStream(mmInStream);
                ContactInformation receivedContact;
                try {
                    receivedContact = (ContactInformation) o.readObject();
                    Log.e(TAG, "RECEIVED CONTACT: " + receivedContact.toString());

                    new ContactsFileManager(service.getCallback().getContext()).writeContactToFile(receivedContact);

                    ImportContact op = new ImportContact();
                    op.updateContact(service.getCallback().getContext(), receivedContact);
                    service.getCallback().changeBluetoothDeviceName(BluetoothAdapterName.RESTORE);
                    service.connectionLost();
                } catch (ClassNotFoundException e) {
                    Log.e(TAG, e.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
                service.connectionLost();
                break;
            }
        }
    }

    public void cancel() {
        try {
            Log.e(TAG, "Closing socket");
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }

    public OutputStream getMmOutStream() {
        return mmOutStream;
    }
}
