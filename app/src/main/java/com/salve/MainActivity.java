package com.salve;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.salve.band.utils.BandUtils;
import com.salve.band.utils.BandVersionType;
import com.salve.contacts.AccountUtils;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    private BandUtils bandUtils;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void connect(View view) {

        bandUtils = new BandUtils(this);
        bandUtils.connect();

    }

    public void getVersion(View view) {
        bandUtils.retrieveVersion(BandVersionType.FIRMWARE);
    }

    public void getContacts(View view) {
        AccountUtils.UserProfile userProfile = AccountUtils.getUserProfile(this);

    }

    public void registerAccelerometerListener(View view) {
        bandUtils.registerAccelerometerListener();
    }

    public void setupBluetooth(View view) {

        if (mBluetoothAdapter == null) {
            Log.e("BLUETOOTH", ": Device does not support Bluetooth");
        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Log.e("BLUETOOTH", "I AM CONNECTED");
            } else if (resultCode == RESULT_CANCELED) {
                Log.e("BLUETOOTH", "I AM NOOOOOT CONNECTED");
            }
        }
    }
    public void queryPairedDevices(View view) {
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
    public void discoverDevices(View view){
        if(mBluetoothAdapter.startDiscovery()){
            // Register the BroadcastReceiver
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        bandUtils.unregisterAccelerometerListener();
        //bandUtils.unregisterGyroscopeListener();
        bandUtils.disconnect();
        unregisterReceiver(mReceiver);
        mBluetoothAdapter.cancelDiscovery();

    }


}
