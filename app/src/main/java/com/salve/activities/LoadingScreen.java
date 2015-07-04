package com.salve.activities;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
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

    private ILoadingScreenOps screenOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        initializeFields();

    }

    private void initializeFields() {
        initializeNonUIFields();
    }

    private void initializeNonUIFields() {
        screenOps = new LoadingScreenOpsImpl(this);
    }

    public void deviceFound(List<BluetoothDevice> devices){
    for (BluetoothDevice device:devices){
        Log.e(TAG, device.getName() + "\n" + device.getAddress());
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
}
