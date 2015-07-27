package com.salve.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.salve.contacts.ContactInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothUtilityOps {

    private static final String TAG = "BluetoothUtilityOps";
    //Member fields
    private BluetoothAdapter mBtAdapter;
    //Newly discovered devices
    private List<BluetoothDevice> mNewDevicesArrayList;
    private List<BluetoothDevice> pairedDevicesArrayList;
    private List<BluetoothDevice> allFoundDevicesArrayList;
    private Context context;
    private static final String salveBluetoothName = "SALVE";
    private String bluetoothOldDeviceName;
    private static volatile BluetoothUtilityOps instance;
    private BluetoothService mBluetoothService;
    private boolean sendingContact;

    public static BluetoothUtilityOps getInstance(Context context) {
        synchronized (BluetoothUtilityOps.class) {
            if (instance == null) {
                instance = new BluetoothUtilityOps(context);
            }
        }
        return instance;
    }

    private BluetoothUtilityOps(Context context) {
        this();
        this.context = context;
    }

    private BluetoothUtilityOps() {
        Log.e(TAG, "instance created");
        mNewDevicesArrayList = new ArrayList<>();
        pairedDevicesArrayList = new ArrayList<>();
        allFoundDevicesArrayList = new ArrayList<>();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothOldDeviceName = mBtAdapter.getName();
        sendingContact = false;
        if (mBluetoothService == null) {
            setupBluetooth();
        }
        Log.e(TAG, "Bluetooth service state is: " + mBluetoothService.getState());
        if (mBluetoothService.getState() == ConnectionStateEnum.STATE_NONE) {
            // Start the Bluetooth chat services
            mBluetoothService.start();
        }
    }

    private void setupBluetooth() {
        mBluetoothService = new BluetoothService(this);
    }

    private void queryDevices() {

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver, filter);
        getPairedDevices();
        doDiscovery();
    }


    private List getPairedDevices() {
        pairedDevicesArrayList = new ArrayList<>();
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (!pairedDevicesArrayList.contains(device))
                    pairedDevicesArrayList.add(device);
            }
        } else {
            Log.d(TAG, "NO PAIRED DEVICES");
        }
        return pairedDevicesArrayList;
    }

    //Start device discover with the BluetoothAdapter
    private void doDiscovery() {
        Log.e(TAG, "doDiscovery()");
        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    private void stopDiscovery() {
        Log.e(TAG, "Stopping discovery");
        context.unregisterReceiver(mReceiver);
        mBtAdapter.cancelDiscovery();
    }

    public void stopBluetoothService() {
        mBluetoothService.stop();
    }

    /**
     * The BroadcastReceiver that listens for discovered devices
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (!mNewDevicesArrayList.contains(device))
                        mNewDevicesArrayList.add(device);
                }
                // When discovery is finished, log the result
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (mNewDevicesArrayList.size() == 0) {
                    Log.e(TAG, "No new devices have been discovered");
                }

                BluetoothDevicesFoundResponse act = (BluetoothDevicesFoundResponse) context;
                act.onBluetoothDevicesFound(getAllFoundDevicesArrayList());
                changeBluetoothDeviceName(BluetoothAdapterName.RESTORE);
            }
        }
    };

    public void changeBluetoothDeviceName(BluetoothAdapterName command) {
        if (command.equals(BluetoothAdapterName.CHANGE)) {
            mBtAdapter.setName(salveBluetoothName);
        } else if (command.equals(BluetoothAdapterName.RESTORE)) {
            mBtAdapter.setName(bluetoothOldDeviceName);
        }
    }

    public String getDeviceName() {
        return mBtAdapter.getName();
    }

    public void connectDevice(String address) {
        // Get the BluetoothDevice object
        stopDiscovery();
        Log.e(TAG, "Attempting to connect");

        BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothService.connect(device, false);
    }

    public void sendContactViaBluetooth() {
        this.ensureDiscoverable();
        this.setSendingContact(true);
        this.changeBluetoothDeviceName(BluetoothAdapterName.CHANGE);
        this.queryDevices();
    }

    private void ensureDiscoverable() {
        if (mBtAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            this.context.startActivity(discoverableIntent);
        }
    }


    public void sendMyContact() {
        Log.e(TAG, "Attempting to send.");
        // Check that we're actually connected before trying anything
        if (mBluetoothService.getState() != ConnectionStateEnum.STATE_CONNECTED) {
            Log.e(TAG, "Not connected");
            return;
        }
        ContactInformation myContact = ContactInformation.getMyContact(context);

        mBluetoothService.write(myContact);
    }


    public List<BluetoothDevice> getAllFoundDevicesArrayList() {
        allFoundDevicesArrayList = new ArrayList<>();
        allFoundDevicesArrayList.addAll(mNewDevicesArrayList);
        allFoundDevicesArrayList.addAll(pairedDevicesArrayList);

        Log.e(TAG, "Bluetooth devices found:");
        for (BluetoothDevice device : allFoundDevicesArrayList) {
            Log.e(TAG, device.getName() + " " + device.getAddress());
        }

        return allFoundDevicesArrayList;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBtAdapter;
    }

    public Context getContext() {
        return context;
    }

    public boolean isSendingContact() {
        return sendingContact;
    }

    public void setSendingContact(boolean sendingContact) {
        this.sendingContact = sendingContact;
    }
}
