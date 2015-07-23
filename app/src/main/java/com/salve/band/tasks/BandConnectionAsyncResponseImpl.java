package com.salve.band.tasks;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.microsoft.band.BandClient;
import com.microsoft.band.ConnectionState;
import com.salve.activities.receivers.GestureRecognitionServiceReceiver;
import com.salve.activities.receivers.StopServiceReceiver;
import com.salve.agrf.gestures.recorder.GestureRecorder;
import com.salve.agrf.gestures.recorder.GestureRecorderListener;
import com.salve.agrf.gestures.recorder.GestureRecorderListenerImpl;
import com.salve.band.sensors.registration.SensorRegistrationManager;
import com.salve.preferences.SalvePreferences;

/**
 * Created by Vlad on 7/22/2015.
 */
public class BandConnectionAsyncResponseImpl implements IBandConnectionAsyncResponse {

    public static final String BAND_CONNECTION_STATUS = "BAND_CONNECTION_STATUS";

    private static final String TAG = "BandConnAsyncResponse";

    private Service service;

    private BandClient bandClient;

    private GestureRecorderListener gestureRecorderListener;

    private GestureRecorder gestureRecorder;

    private BroadcastReceiver stopServiceReceiver;

    public BandConnectionAsyncResponseImpl() {

    }

    public BandConnectionAsyncResponseImpl(Service service, BandClient bandClient) {
        this.service = service;
        this.bandClient = bandClient;

        SensorRegistrationManager sensorRegistrationManager = new SensorRegistrationManager(bandClient);
        this.gestureRecorder = new GestureRecorder(sensorRegistrationManager);
        this.gestureRecorderListener = new GestureRecorderListenerImpl(service, gestureRecorder);
    }

    @Override
    public void onFinishedConnection(ConnectionState connectionState) {
        Log.e(TAG, connectionState.toString());
        if (gestureRecorder != null) {
            gestureRecorder.registerListener(gestureRecorderListener);

            Context ctx = service.getApplicationContext();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            if (prefs.getBoolean(SalvePreferences.DEFAULT_GESTURE, true)) {
                gestureRecorderListener.startClassification(SalvePreferences.DEFAULT_GESTURE);
            } else if (prefs.getBoolean(SalvePreferences.MY_OWN_GESTURE, true)) {
                gestureRecorderListener.startClassification(SalvePreferences.MY_OWN_GESTURE);
            }
        }

        registerReceiverToStopService();

        sendBroadcastWithBandConnectionState(connectionState);
    }

    @Override
    public void unregisterListener() {
        gestureRecorder.unregisterListener(gestureRecorderListener);
    }

    @Override
    public void unregisterStopReceiver() {
        service.unregisterReceiver(stopServiceReceiver);
    }

    @Override
    public IBinder getBinder() {
        return gestureRecorderListener.getBinder();
    }

    private void registerReceiverToStopService() {
        stopServiceReceiver = new StopServiceReceiver(service, bandClient);
        service.registerReceiver(stopServiceReceiver, new IntentFilter(StopServiceReceiver.RECEIVER_FILTER));
    }

    private void sendBroadcastWithBandConnectionState(ConnectionState connectionState) {
        Intent intent = new Intent()
                .setAction(GestureRecognitionServiceReceiver.PROCESS_RESPONSE)
                .addCategory(Intent.CATEGORY_DEFAULT)
                .putExtra(BAND_CONNECTION_STATUS, connectionState);
        service.sendBroadcast(intent);
    }
}
