package com.salve.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.salve.R;
import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.IGestureRecognitionListener;
import com.salve.agrf.gestures.IGestureRecognitionService;
import com.salve.bluetooth.BluetoothFragment;
import com.salve.contacts.AccountUtils;

import java.util.Set;


public class TestingActivity extends AppCompatActivity {

    private static final String TAG = "TestingActivity";

    private TextView trainingTV;

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private GestureConnectionService gestureConnectionService;
    private IBinder gestureListenerStub;
    private boolean mLogShown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trainingTV = (TextView) findViewById(R.id.button99);

        //gestureConnectionService = LoadingScreenOpsImpl.gestureConnectionService;
       // gestureListenerStub = LoadingScreenOpsImpl.gestureListenerStub;
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothFragment fragment = new BluetoothFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logToggle = menu.findItem(R.id.action_settings);
        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        logToggle.setTitle(mLogShown ? "true" : "false");

        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(item.getItemId()) {
            case R.id.action_settings:
                mLogShown = !mLogShown;
                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) {
                    output.setDisplayedChild(1);
                } else {
                    output.setDisplayedChild(0);
                }
                supportInvalidateOptionsMenu();
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void connect(View view) {
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

    public void getVersion(View view) {
        //bandUtil.retrieveVersion(BandVersionType.FIRMWARE);
    }

    public void getContacts(View view) {

        AccountUtils.UserProfile userProfile = AccountUtils.getUserProfile(this);

    }

    public void registerAccelerometerListener(View view) {
        //bandUtil.registerAccelerometerListener();
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

    public void setupBluetooth(View view) {
         mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.e("BLUETOOTH", ": Device does not support Bluetooth");
        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

/*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Log.e("BLUETOOTH", "I AM CONNECTED");
            } else if (resultCode == RESULT_CANCELED) {
                Log.e("BLUETOOTH", "I AM NOOOOOT CONNECTED");
            }
        }

    }
*/
    public void queryPairedDevices(View view) {
        setupBluetooth(view);
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        String[] devices = new String[5];
        ArrayAdapter mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, devices);
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                // mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                Log.e("BLUETOOTH", device.getName() + "\n" + device.getAddress());
            }

        }

    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                Log.e("BLUETOOTH", device.getName() + "\n" + device.getAddress());

            }
        }
    };

    public void discoverDevices(View view) {
        setupBluetooth(view);
        if (mBluetoothAdapter.startDiscovery()) {
            // Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mBluetoothAdapter.cancelDiscovery();
    }
}
