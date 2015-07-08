package com.salve.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.salve.R;
import com.salve.activities.operations.ILoadingScreenOps;
import com.salve.activities.operations.LoadingScreenOpsImpl;
import com.salve.bluetooth.BluetoothUtilityOps;

import java.util.List;

public class LoadingScreen extends Activity {

    private static final String TAG = "LoadingScreen";
    private BluetoothUtilityOps bluetoothOps;
    private ILoadingScreenOps screenOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        initializeNonUIFields();
        bluetoothOps = BluetoothUtilityOps.getInstance(this);
    }

    public void deviceFound(List<BluetoothDevice> devices) {
        ensureDiscoverable();
        for (BluetoothDevice device : devices) {
            Log.e(TAG, device.getName() + "\n" + device.getAddress());

            if(device.getName().equals(bluetoothOps.getDeviceName())){
                Log.e(TAG, "HAVE THE SAME NAME");
                bluetoothOps.connectDevice(device.getAddress());

            }
        }

    }
    private void ensureDiscoverable() {
        BluetoothAdapter mBluetoothAdapter = bluetoothOps.getBluetoothAdapter();
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading_screen, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        screenOps.LoadApplication();

    }

    private void initializeNonUIFields() {
        screenOps = new LoadingScreenOpsImpl(this);
    }

    @Override
    protected void onDestroy(){
        bluetoothOps.stopBluetoothService();
    }
}
