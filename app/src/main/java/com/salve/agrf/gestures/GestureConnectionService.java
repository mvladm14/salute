package com.salve.agrf.gestures;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class GestureConnectionService implements ServiceConnection {

    private static final String TAG = "GestureConnService";

    private IGestureRecognitionService recognitionService;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        Log.e(TAG, "Service is now connected");
        recognitionService = IGestureRecognitionService.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.e(TAG, "Service is now disconnected");
        recognitionService = null;
    }

    public IGestureRecognitionService getRecognitionService() {
        return recognitionService;
    }
}
