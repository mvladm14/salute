package com.salve.activities.operations;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.salve.activities.LoadingScreen;
import com.salve.activities.Preferences;
import com.salve.activities.models.PreferencesModel;
import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.GestureRecognitionService;
import com.salve.agrf.gestures.IGestureRecognitionListener;
import com.salve.agrf.gestures.classifier.Distribution;
import com.salve.bluetooth.BluetoothAdapterName;
import com.salve.bluetooth.BluetoothUtilityOps;

import java.util.List;

/**
 * Created by Vlad on 6/24/2015.
 */
public class LoadingScreenOpsImpl implements ILoadingScreenOps {

    private static final String TAG = "LoadingScreenOpsImpl";
    public static IBinder gestureListenerStub;
    public static GestureConnectionService gestureConnectionService;
    private LoadingScreen mActivity;
    private BluetoothUtilityOps bluetoothOps;

    public LoadingScreenOpsImpl(final LoadingScreen mActivity) {
        this.mActivity = mActivity;
        bluetoothOps = BluetoothUtilityOps.getInstance(mActivity);
    }

    @Override
    public void LoadApplication() {
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
        };

        gestureConnectionService = new GestureConnectionService();
        Intent bindIntent = new Intent(mActivity, GestureRecognitionService.class);
        mActivity.bindService(bindIntent, gestureConnectionService, Context.BIND_AUTO_CREATE);


    }


    private void ensureDiscoverable() {
        BluetoothAdapter mBluetoothAdapter = bluetoothOps.getBluetoothAdapter();
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            mActivity.startActivity(discoverableIntent);
        }
    }



    public void deviceFound(List<BluetoothDevice> devices) {

        for (BluetoothDevice device : devices) {
            Log.e(TAG, device.getName() + "\n" + device.getAddress());

            if(device.getName().equals(bluetoothOps.getDeviceName())){
                Log.e(TAG, "HAVE THE SAME NAME ==> try to connect");
                bluetoothOps.connectDevice(device.getAddress());
            }
        }
    }


    @Override
    public void goToActivity(Activity currentActivity, Class<? extends Activity> nextActivityClass) {
        Intent intent = new Intent(currentActivity, nextActivityClass);
        currentActivity.startActivity(intent);
    }
}
