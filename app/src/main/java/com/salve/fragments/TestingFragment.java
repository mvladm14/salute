package com.salve.fragments;

import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.salve.R;
import com.salve.activities.operations.TestingFragmentOpsImpl;
import com.salve.bluetooth.BluetoothDevicesFoundResponse;
import com.salve.bluetooth.BluetoothUtilityOps;
import com.salve.contacts.ContactInformation;
import com.salve.exceptions.bluetooth.BluetoothNotEnabledException;

import java.util.List;

public class TestingFragment extends Fragment {

    private TestingFragmentOpsImpl ops;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.testing, container, false);

        initializeFields();

        return rootView;
    }

    private void initializeFields() {
        ops = new TestingFragmentOpsImpl(this, rootView);
    }
}