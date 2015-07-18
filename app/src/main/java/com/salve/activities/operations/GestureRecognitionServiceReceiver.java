package com.salve.activities.operations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.microsoft.band.ConnectionState;
import com.salve.activities.MainScreen;
import com.salve.agrf.gestures.GestureRecognitionService;

/**
 * Created by Vlad on 7/17/2015.
 */
public class GestureRecognitionServiceReceiver extends BroadcastReceiver {

    private static final String TAG = "GestureRecSvcReceiver";

    public static final String PROCESS_RESPONSE = "com.salve.intent.action.PROCESS_RESPONSE";
    private ConnectionState connectionState;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "received broadcast message");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            connectionState = (ConnectionState) extras.getSerializable(GestureRecognitionService.BAND_CONNECTION_STATUS);
        }
        Log.e(TAG, connectionState + "");

        Intent mainScreenActivity = new Intent(context, MainScreen.class);
        mainScreenActivity.putExtra(GestureRecognitionService.BAND_CONNECTION_STATUS, connectionState);
        context.startActivity(mainScreenActivity);
    }
}
