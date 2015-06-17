package com.salve.band.sensors.registration;

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

    private BandClient bandClient;
    private BandAccelerometerEventListener bandAccelerometerEventListener;
    private BandGyroscopeEventListener bandGyroscopeEventListener;

    public SensorRegistrationManager(BandClient bandClient) {
        this.bandClient = bandClient;
        bandAccelerometerEventListener = new BandAccelerometerEventListenerImpl();
        bandGyroscopeEventListener = new BandGyroscopeEventListenerImpl();
    }

    public void registerAccelerometerListener() {
        try {
            // register the listener
            bandClient.getSensorManager().registerAccelerometerEventListener(
                    this.bandAccelerometerEventListener,
                    SampleRate.MS16);
        } catch (BandIOException ex) {
            // handle BandException
            ex.printStackTrace();
        }
    }

    public void unregisterAccelerometerListener() {
        try {
            // register the listener
            bandClient.getSensorManager().unregisterAccelerometerEventListener(this.bandAccelerometerEventListener);
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
                    SampleRate.MS32);

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
