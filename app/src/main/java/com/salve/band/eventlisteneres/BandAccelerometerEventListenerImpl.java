package com.salve.band.eventlisteneres;

import android.util.Log;

import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;

/**
 * Created by Vlad on 6/17/2015.
 */
public class BandAccelerometerEventListenerImpl implements BandAccelerometerEventListener {
    @Override
    public void onBandAccelerometerChanged(BandAccelerometerEvent bandAccelerometerEvent) {
        Log.e("ACCELEROMETER", bandAccelerometerEvent.getAccelerationX() + "");
    }
}
