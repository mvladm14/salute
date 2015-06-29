package com.salve.band.sensors.registration;

import android.util.Log;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandIOException;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

/**
 * Created by Vlad on 6/17/2015.
 */
public class SensorRegistrationManager {

    private static final String TAG = "SRManager";

    private BandClient bandClient;

    public SensorRegistrationManager(BandClient bandClient) {
        this.bandClient = bandClient;
    }

    public void registerAccelerometerListener(BandAccelerometerEventListener bandAccelerometerEventListener) {
        try {
            // register the listener
            boolean registered = bandClient.getSensorManager().registerAccelerometerEventListener(
                    bandAccelerometerEventListener,
                    SampleRate.MS128);
            Log.e(TAG, "Accelerometer registered " + (registered ? "successfully." : "unsuccessfully."));
        } catch (BandIOException ex) {
            // handle BandException
            ex.printStackTrace();
        }
    }

    public void unregisterAccelerometerListener(BandAccelerometerEventListener bandAccelerometerEventListener) {
        try {
            // register the listener
            bandClient.getSensorManager().unregisterAccelerometerEventListener(bandAccelerometerEventListener);
            Log.e(TAG, "BAND Accelerometer unregistered successfully.");
        } catch (BandIOException ex) {
            // handle BandException
            ex.printStackTrace();
        }
    }
}
