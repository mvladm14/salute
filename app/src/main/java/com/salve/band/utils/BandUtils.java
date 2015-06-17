package com.salve.band.utils;

import android.content.Context;
import android.util.Log;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandIOException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
import com.salve.band.sensors.registration.SensorRegistrationManager;
import com.salve.band.tasks.AsyncResponse;
import com.salve.band.tasks.BandConnectionTask;
import com.salve.band.tasks.BandVersionTask;

import java.util.concurrent.ExecutionException;

/**
 * Created by Vlad on 6/16/2015.
 */
public class BandUtils implements AsyncResponse {

    private BandInfo[] pairedBands;
    private BandClient bandClient;
    private BandPendingResult<ConnectionState> pendingResult;
    private ConnectionState state;
    private String version;
    private SensorRegistrationManager sensorRegistrationManager;

    public BandUtils(Context context) {
        pairedBands = BandClientManager.getInstance().getPairedBands();
        bandClient = BandClientManager.getInstance().create(context, pairedBands[0]);
        sensorRegistrationManager = new SensorRegistrationManager(bandClient);
    }

    public void connect() {
        pendingResult = bandClient.connect();
        BandConnectionTask connectionTask = new BandConnectionTask(this);
        connectionTask.execute(pendingResult);
    }

    public boolean isConnected() {
        return state == ConnectionState.CONNECTED;
    }

    public void retrieveVersion(BandVersionType bandVersion) {
        if (isConnected()) {
            try {
                BandPendingResult<String> pendingResult = null;
                switch (bandVersion) {
                    case FIRMWARE:
                        pendingResult = bandClient.getFirmwareVersion();
                        break;

                    case HARDWARE:
                        pendingResult = bandClient.getHardwareVersion();
                        break;
                }
                new BandVersionTask(this).execute(pendingResult).get();
            } catch (BandIOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public String getVersion() {
        return version;
    }

    @Override
    public void onFinishedConnection(ConnectionState connectionState) {
        this.state = connectionState;
        Log.e("CONNEXION_STATE", state.toString());
    }

    @Override
    public void onFinishedVersion(String version) {
        this.version = version;
        Log.e("VERSION", version.toString());
    }

    public void registerAccelerometerListener() {
            sensorRegistrationManager.registerAccelerometerListener();
        }

    public void registerGyroscopeListener() {
        sensorRegistrationManager.registerGyroscopeListener();
    }

    public void unregisterAccelerometerListener() {
        sensorRegistrationManager.unregisterAccelerometerListener();
    }

    public void unregisterGyroscopeListener() {
        sensorRegistrationManager.unregisterGyroscopeListener();
    }
}
