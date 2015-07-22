package com.salve.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;


import com.salve.activities.asyncReplies.IGestureConnectionServiceAsyncReply;
import com.salve.activities.models.PreferencesModel;
import com.salve.activities.operations.LoadingScreenOpsImpl;
import com.salve.activities.operations.PreferencesOpsImpl;

import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.GestureRecognitionService;
import com.salve.agrf.gestures.IGestureRecognitionListener;
import com.salve.agrf.gestures.IGestureRecognitionService;

import com.salve.agrf.gestures.classifier.Distribution;

import com.salve.bluetooth.BluetoothAdapterName;
import com.salve.bluetooth.BluetoothUtilityOps;
import com.salve.contacts.ContactInformation;
import com.salve.R;
import java.util.List;


public class TestingActivity extends AppCompatActivity implements IGestureConnectionServiceAsyncReply {

    private static final String TAG = "TestingActivity";

    private TextView trainingTV;

    private IBinder gestureListenerStub;

    private GestureConnectionService gestureConnectionService;


    private BluetoothUtilityOps bluetoothOps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing);
        bluetoothOps = BluetoothUtilityOps.getInstance(this);

        trainingTV = (TextView) findViewById(R.id.button99);

        gestureConnectionService = new GestureConnectionService(this);

        gestureListenerStub = new IGestureRecognitionListener.Stub() {

            @Override
            public void onGestureLearned(String gestureName) throws RemoteException {
                Log.e(TAG, String.format("%s learned", gestureName));
            }

            @Override
            public void onTrainingSetDeleted(String trainingSet) throws RemoteException {
                Log.e(TAG, String.format("Training set %s deleted", trainingSet));
            }

            @Override
            public void onGestureRecognized(final Distribution distribution) throws RemoteException {
                Log.e(TAG, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
            }
        };

        Intent bindIntent = new Intent(this, GestureRecognitionService.class);
        this.bindService(bindIntent, gestureConnectionService, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        this.unbindService(gestureConnectionService);
        super.onDestroy();
    }

    public void training(View v) {

        Log.e(TAG, "Train button pushed");
        IGestureRecognitionService recognitionService = gestureConnectionService.getRecognitionService();
        if (recognitionService != null) {
            try {
                if (!recognitionService.isLearning()) {
                    trainingTV.setText("Stop Training");
                    recognitionService.startLearnMode("handshake", "HANDSHAKE");
                } else {
                    trainingTV.setText("Start Training");
                    recognitionService.stopLearnMode();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "recognitionService is null");
        }
    }

    @Override
    public void onServiceConnected() {
        final String activeTrainingSet = "handshake";
        try {
            gestureConnectionService
                    .getRecognitionService()
                    .startClassificationMode(activeTrainingSet);
            gestureConnectionService
                    .getRecognitionService()
                    .registerListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected() {

    }


    //**********************Bluetooth*****************************

    public void sendContactViaBluetooth(View view) {
        ensureDiscoverable();
        bluetoothOps.changeBluetoothDeviceName(BluetoothAdapterName.CHANGE);
        bluetoothOps.queryDevices();

        List<PreferencesModel> preferencesModels = PreferencesOpsImpl.getPreferencesModels();
        int count = 0;
        for (PreferencesModel model : preferencesModels) {
            if (model.isSelected()) count++;
        }

        Log.e(TAG, "TOTAL PREFERENCES SELECTED = " + count);
    }

    private void ensureDiscoverable() {
        BluetoothAdapter mBluetoothAdapter = bluetoothOps.getBluetoothAdapter();
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            this.startActivity(discoverableIntent);
        }
    }

    public void deviceFound(List<BluetoothDevice> devices) {

        for (BluetoothDevice device : devices) {
            if (device.getName() != null && device.getName().equals(bluetoothOps.getDeviceName())) {
                Log.e(TAG, "HAVE THE SAME NAME ==> try to connect");
                bluetoothOps.connectDevice(device.getAddress());
            }
        }
    }

    public void getMyContact(View view) {
        ContactInformation c = ContactInformation.getMyContact(this);
        Log.e(TAG, c.toString());
    }

}

