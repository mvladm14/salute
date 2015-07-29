package com.salve.broadcastReceivers;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "BluetoothBroadcastReceiver";

    private Service mService;

    public BluetoothBroadcastReceiver(Service service) {
        this.mService = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR);
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    Log.e(TAG, "Bluetooth off");
                    //TODO stop pairing with the band...unregister listeners for the band
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.e(TAG, "Turning Bluetooth off...");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.e(TAG, "Bluetooth on");
                    //TODO start pairing with the band...register listeners for the band
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.e(TAG, "Turning Bluetooth on...");
                    break;
            }
        }
    }
}
