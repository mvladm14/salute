package com.salve.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.salve.R;
import com.salve.activities.LoadingScreen;
import com.salve.activities.operations.ILoadingScreenOps;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by rroxa_000 on 7/4/2015.
 */
public class BluetoothUtilityOps {

    private static final String TAG = "BluetoothUtilityOps";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    //Member fields
    private BluetoothAdapter mBtAdapter;
    //Newly discovered devices
    private List<BluetoothDevice> mNewDevicesArrayList;
    private List<BluetoothDevice> pairedDevicesArrayList;
    private List<BluetoothDevice> allFoundDevicesArrayList;
    private LoadingScreen activity;
    private ILoadingScreenOps loadingScreenOps;
    private static final String salveBluetoothName = "SALVE";
    private String bluetothOldDeviceName;
    private static volatile BluetoothUtilityOps instance;
    private BluetoothService mBluetoothService = null;

    public static BluetoothUtilityOps getInstance(LoadingScreen activity){
        if(instance == null){
            synchronized (BluetoothUtilityOps.class) {
                if (instance == null) {
                    instance = new BluetoothUtilityOps(activity);
                }
            }
        }
        return instance;
    }

    private BluetoothUtilityOps(LoadingScreen activity) {
        this.activity = activity;
        this.loadingScreenOps = loadingScreenOps;
        mNewDevicesArrayList = new ArrayList<>();
        pairedDevicesArrayList = new ArrayList<>();
        allFoundDevicesArrayList = new ArrayList<>();
        this.mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothService == null){
            setupBluetooth();
        }
    }

    private void setupBluetooth(){
        mBluetoothService = new BluetoothService(activity, mHandler);
    }
    public void queryDevices() {

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        activity.registerReceiver(mReceiver, filter);
        getPairedDevices();
        doDiscovery();
    }


    private List getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayList.add(device);
            }
        } else {
            Log.d(TAG, "NO PAIRED DEVICES");
        }
        return pairedDevicesArrayList;
    }

    //Start device discover with the BluetoothAdapter
    private void doDiscovery() {
        Log.d(TAG, "doDiscovery()");
        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
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
                    mNewDevicesArrayList.add(device);


                }
                // When discovery is finished, log the result
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (mNewDevicesArrayList.size() == 0) {
                    Log.d(TAG, "No new devices have been discovered");

                }
                activity.deviceFound(getAllFoundDevicesArrayList());
                changeBluetoothDeviceName(BluetoothAdapterName.RESTORE);
            }
        }
    };

    public List<BluetoothDevice> getPairedDevicesArrayList() {
        return pairedDevicesArrayList;
    }


    public void changeBluetoothDeviceName(BluetoothAdapterName command){
        if (command.equals(BluetoothAdapterName.CHANGE)) {
            bluetothOldDeviceName = mBtAdapter.getName();
            mBtAdapter.setName(salveBluetoothName);
        }
        else if(command.equals(BluetoothAdapterName.RESTORE)){
            mBtAdapter.setName(bluetothOldDeviceName);
        }
    }
    public String getDeviceName(){
        return mBtAdapter.getName();
    }

    public void connectDevice(String address) {
        // Get the BluetoothDevice object
        BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mBluetoothService.connect(device, false);
    }

    public List<BluetoothDevice> getmNewDevicesArrayList() {
        return mNewDevicesArrayList;
    }

    public List<BluetoothDevice> getAllFoundDevicesArrayList() {
        allFoundDevicesArrayList.addAll(mNewDevicesArrayList);
        allFoundDevicesArrayList.addAll(pairedDevicesArrayList);
        return allFoundDevicesArrayList;
    }


    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
           // FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Log.d(TAG, "CONNECTED");
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d(TAG, "CONNECTING");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d(TAG,"NOT CONNECTED");
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    String connectedDevice = msg.getData().getString(Constants.DEVICE_NAME);

                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Log.d(TAG,msg.getData().getString(Constants.TOAST));
                    }
                    break;
            }
        }
    };


}
