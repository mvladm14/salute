package com.salve.agrf.gestures;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.salve.activities.commands.SendNotificationCommand;
import com.salve.band.BandConnectionManager;
import com.salve.bluetooth.BluetoothDevicesFoundResponse;
import com.salve.bluetooth.BluetoothUtilityOps;

import java.util.List;

public class GestureRecognitionService extends Service implements BluetoothDevicesFoundResponse {

    private final String TAG = "GestureRecognitionSvc";

    private BandConnectionManager bandConnectionManager;

    private BluetoothUtilityOps bluetoothOps;

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate() called");

        this.bluetoothOps = BluetoothUtilityOps.getInstance(this);
        bandConnectionManager = new BandConnectionManager(this);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand() called");

        new SendNotificationCommand(this).execute();
        bandConnectionManager.connectToBand();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy() called");
        bandConnectionManager.unregisterStopReceiver();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bandConnectionManager.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");

        bandConnectionManager.unregisterListnere();
        return super.onUnbind(intent);
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
