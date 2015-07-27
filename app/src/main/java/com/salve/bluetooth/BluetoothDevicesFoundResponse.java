package com.salve.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * Created by Vlad on 7/27/2015.
 */
public interface BluetoothDevicesFoundResponse {
    void onBluetoothDevicesFound(List<BluetoothDevice> devices);
}
