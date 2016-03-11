package com.salve.activities.operations;

import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.salve.R;
import com.salve.bluetooth.BluetoothDevicesFoundResponse;
import com.salve.bluetooth.BluetoothUtilityOps;
import com.salve.contacts.ContactInformation;
import com.salve.exceptions.bluetooth.BluetoothNotEnabledException;
import com.salve.fragments.TestingFragment;

import java.util.List;

public class TestingFragmentOpsImpl implements OnClickListener, BluetoothDevicesFoundResponse {

    private static final String TAG = "TestingFragmentOpsImpl";

    private Fragment mFragment;

    private BluetoothUtilityOps bluetoothOps;

    public TestingFragmentOpsImpl(TestingFragment testingFragment, View rootView) {
        mFragment = testingFragment;

        Button sendContactViaBluetoothButton = (Button) rootView.findViewById(R.id.sendContactViaBluetoothButton);
        sendContactViaBluetoothButton.setOnClickListener(this);

        Button getMyContactButton = (Button) rootView.findViewById(R.id.getMyContactButton);
        getMyContactButton.setOnClickListener(this);

        try {
            bluetoothOps = BluetoothUtilityOps.getInstance(mFragment.getActivity());
        } catch (BluetoothNotEnabledException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendContactViaBluetoothButton:
                sendContactViaBluetooth();
                break;

            case R.id.getMyContactButton:
                getMyContact();
                break;
            default:
                break;
        }
    }

    public void sendContactViaBluetooth() {
        bluetoothOps.sendContactViaBluetooth();
    }

    public void getMyContact() {
        ContactInformation c = ContactInformation.getMyContact(mFragment.getActivity());
        Log.e(TAG, c.toString());
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