package com.salve;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.salve.band.utils.BandUtils;
import com.salve.band.utils.BandVersionType;
import com.salve.contacts.AccountUtils;


public class MainActivity extends ActionBarActivity {

    private BandUtils bandUtils;
    private static final int REQUEST_ENABLE_BT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    @Override
    public void onDestroy() {
        bandUtils.unregisterAccelerometerListener();
        //bandUtils.unregisterGyroscopeListener();
        bandUtils.disconnect();
        super.onDestroy();
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

    public void setupBluetooth(View view){

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.e("BLUETOOTH",": Device does not support Bluetooth");
        }
        else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


    }
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK){
                Log.e("BLUETOOTH", "I AM CONNECTED");
            }
            else if (resultCode == RESULT_CANCELED){
                Log.e("BLUETOOTH", "I AM NOOOOOT CONNECTED");
            }
        }

    }
}
