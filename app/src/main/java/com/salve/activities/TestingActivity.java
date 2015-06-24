package com.salve.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.salve.R;
import com.salve.activityOperations.LoadingScreenOpsImpl;
import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.IGestureRecognitionListener;
import com.salve.agrf.gestures.IGestureRecognitionService;
import com.salve.contacts.AccountUtils;


public class TestingActivity extends Activity {

    private static final String TAG = "TestingActivity";

    private TextView trainingTV;

    private static final int REQUEST_ENABLE_BT = 1;

    private GestureConnectionService gestureConnectionService;
    private IBinder gestureListenerStub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trainingTV = (TextView) findViewById(R.id.button99);

        gestureConnectionService = LoadingScreenOpsImpl.gestureConnectionService;
        gestureListenerStub = LoadingScreenOpsImpl.gestureListenerStub;
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
        super.onDestroy();
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

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
}
