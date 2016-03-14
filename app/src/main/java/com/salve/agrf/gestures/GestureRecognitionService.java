package com.salve.agrf.gestures;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.salve.activities.commands.SendNotificationCommand;
import com.salve.band.BandConnectionManager;
import com.salve.bluetooth.BluetoothDevicesFoundResponse;
import com.salve.bluetooth.BluetoothUtilityOps;
import com.salve.broadcastReceivers.BluetoothBroadcastReceiver;
import com.salve.broadcastReceivers.StopServiceReceiver;
import com.salve.exceptions.bluetooth.BluetoothNotEnabledException;

import java.util.List;

public class GestureRecognitionService extends Service implements BluetoothDevicesFoundResponse {

    private final String TAG = "GestureRecognitionSvc";

    private BandConnectionManager bandConnectionManager;

    private BluetoothUtilityOps bluetoothOps;

    private BroadcastReceiver bluetoothBroadcastReceiver;

    private BroadcastReceiver stopServiceReceiver;

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate() called");

        try {
            this.bluetoothOps = BluetoothUtilityOps.getInstance(this);
        } catch (BluetoothNotEnabledException e) {
            this.bluetoothOps = null;
            e.printStackTrace();
        }

        bandConnectionManager = new BandConnectionManager(this);

        bluetoothBroadcastReceiver = new BluetoothBroadcastReceiver(this);
        stopServiceReceiver = new StopServiceReceiver(this, bandConnectionManager.getBandClient());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand() called");

        registerReceivers();

        new SendNotificationCommand(this).execute();
        bandConnectionManager.connectToBand();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy() called");

        unregisterReceivers();
        this.bluetoothOps.stopBluetoothService();
        bandConnectionManager.disconnectFromBand();

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bandConnectionManager.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");

        bandConnectionManager.unregisterListener();

        return super.onUnbind(intent);
    }

    @Override
    public void onBluetoothDevicesFound(List<BluetoothDevice> devices) {
        for (BluetoothDevice device : devices) {
            if (device.getName() != null && device.getName().equalsIgnoreCase(bluetoothOps.getDeviceName())) {
                Log.e(TAG, "HAVE THE SAME NAME ==> try to connect");
                bluetoothOps.connectDevice(device.getAddress());
            }
        }
    }

    private void registerReceivers() {
        this.registerReceiver(bluetoothBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        this.registerReceiver(stopServiceReceiver, new IntentFilter(StopServiceReceiver.RECEIVER_FILTER));
    }

    private void unregisterReceivers() {
        unregisterReceiver(bluetoothBroadcastReceiver);
        unregisterReceiver(stopServiceReceiver);
    }
}
