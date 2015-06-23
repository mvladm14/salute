package com.salve.band.sensors.registration;

import android.util.Log;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandIOException;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandGyroscopeEventListener;
import com.microsoft.band.sensors.SampleRate;
import com.salve.band.eventlisteneres.BandAccelerometerEventListenerImpl;
import com.salve.band.eventlisteneres.BandGyroscopeEventListenerImpl;

/**
 * Created by Vlad on 6/17/2015.
 */
public class SensorRegistrationManager {

    private static final String TAG = "SRManager";

    private BandClient bandClient;
    private BandAccelerometerEventListener bandAccelerometerEventListener;
    private BandGyroscopeEventListener bandGyroscopeEventListener;

    public SensorRegistrationManager(BandClient bandClient) {
        this.bandClient = bandClient;
        bandAccelerometerEventListener = new BandAccelerometerEventListenerImpl();
        bandGyroscopeEventListener = new BandGyroscopeEventListenerImpl();
    }

    public void registerAccelerometerListener(BandAccelerometerEventListener bandAccelerometerEventListener) {
        try {
            // register the listener
            boolean registered = bandClient.getSensorManager().registerAccelerometerEventListener(
                    bandAccelerometerEventListener,
                    SampleRate.MS128);
            Log.e(TAG, "Accelerometer registered "  + (registered ? "successfully." : "unsuccessfully."));
        } catch (BandIOException ex) {
            // handle BandException
            ex.printStackTrace();
        }
    }

    public void unregisterAccelerometerListener(BandAccelerometerEventListener bandAccelerometerEventListener) {
        try {
            // register the listener
            bandClient.getSensorManager().unregisterAccelerometerEventListener(bandAccelerometerEventListener);
            Log.e(TAG, "Accelerometer unregistered successfully.");
        } catch (BandIOException ex) {
            // handle BandException
            ex.printStackTrace();
        }
    }

    public void registerAccelerometerListener() {
        try {
            // register the listener
            bandClient.getSensorManager().registerAccelerometerEventListener(
                    this.bandAccelerometerEventListener,
                    SampleRate.MS128);
            Log.e(TAG, "Accelerometer registered successfully.");
        } catch (BandIOException ex) {
            // handle BandException
            ex.printStackTrace();
        }
    }

    public void unregisterAccelerometerListener() {
        try {
            // register the listener
            bandClient.getSensorManager().unregisterAccelerometerEventListener(this.bandAccelerometerEventListener);
            Log.e(TAG, "Accelerometer unregistered successfully.");
        } catch (BandIOException ex) {
            // handle BandException
            ex.printStackTrace();
        }
    }

    public void registerGyroscopeListener() {
        try {
            // register the listener
            bandClient.getSensorManager().registerGyroscopeEventListener(
                    this.bandGyroscopeEventListener,
                    SampleRate.MS128);

        } catch (BandIOException ex) {
            // handle BandException
            ex.printStackTrace();
        }
    }

    public void unregisterGyroscopeListener() {
        try {
            // register the listener
            bandClient.getSensorManager().unregisterGyroscopeEventListener(this.bandGyroscopeEventListener);
        } catch (BandIOException ex) {
            // handle BandException
            ex.printStackTrace();
        }
    }
}
