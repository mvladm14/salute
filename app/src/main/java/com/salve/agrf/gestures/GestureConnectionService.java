package com.salve.agrf.gestures;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.salve.activities.asyncReplies.IGestureConnectionServiceAsyncReply;

public class GestureConnectionService implements ServiceConnection {

    private static final String TAG = "GestureConnService";

    private IGestureRecognitionService recognitionService;

    private IGestureConnectionServiceAsyncReply mAsyncReply;

    public GestureConnectionService(IGestureConnectionServiceAsyncReply asyncReply) {
        this.mAsyncReply = asyncReply;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        Log.e(TAG, "Service is now connected");
        recognitionService = IGestureRecognitionService.Stub.asInterface(service);
        mAsyncReply.onServiceConnected();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.e(TAG, "Service is now disconnected");
        recognitionService = null;
        mAsyncReply.onServiceDisconnected();
    }

    public IGestureRecognitionService getRecognitionService() {
        return recognitionService;
    }
}
