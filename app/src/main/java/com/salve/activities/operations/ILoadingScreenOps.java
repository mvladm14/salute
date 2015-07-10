package com.salve.activities.operations;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * Created by Vlad on 6/24/2015.
 */
public interface ILoadingScreenOps extends IActivityOperations {
    void LoadApplication();
    public void deviceFound(List<BluetoothDevice> allFoundDevicesArrayList);

}
