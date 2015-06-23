package com.salve;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.GestureRecognitionService;
import com.salve.agrf.gestures.IGestureRecognitionListener;
import com.salve.agrf.gestures.IGestureRecognitionService;
import com.salve.agrf.gestures.classifier.Distribution;
import com.salve.band.utils.BandUtil;
import com.salve.band.utils.BandVersionType;
import com.salve.contacts.AccountUtils;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private TextView trainingTV;

    private static final int REQUEST_ENABLE_BT = 1;

    private IBinder gestureListenerStub = new IGestureRecognitionListener.Stub() {

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

    private GestureConnectionService gestureConnectionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trainingTV = (TextView) findViewById(R.id.button99);

        gestureConnectionService = new GestureConnectionService();
        Intent bindIntent = new Intent(this, GestureRecognitionService.class);
        this.bindService(bindIntent, gestureConnectionService, Context.BIND_AUTO_CREATE);

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
        /*
        IGestureRecognitionService recognitionService = gestureConnectionService.getRecognitionService();
        try {
            recognitionService.unregisterListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        recognitionService = null;
        */
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void connect(View view) {

        //bandUtil = new BandUtil(this);
        //bandUtil.connect();

        final String activeTrainingSet = "handshake";
        try {
            gestureConnectionService.getRecognitionService().startClassificationMode(activeTrainingSet);
            //Log.e(TAG,"CACAT");
            gestureConnectionService.getRecognitionService().registerListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
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
