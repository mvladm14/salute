package com.salve.band.eventlisteneres;

import android.util.Log;

import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.salve.band.sensors.models.Accelerometer;
import com.salve.gestures.models.Gesture;
import com.salve.gestures.models.HandShake;
import com.salve.gestures.models.StationaryHandShakeLeftHand;
import com.salve.gestures.recognition.RecognitionEngine;

/**
 * Created by Vlad on 6/17/2015.
 */
public class BandAccelerometerEventListenerImpl implements BandAccelerometerEventListener {

    public BandAccelerometerEventListenerImpl() {
        Log.e("CACAT", "CACAT");
    }

    private Gesture lastGesture;

    private long lastUpdate;
    private float last_y;

    @Override
    public void onBandAccelerometerChanged(BandAccelerometerEvent bandAccelerometerEvent) {
        Accelerometer accelerometer = new Accelerometer.AccelerometerBuilder()
                .withX(bandAccelerometerEvent.getAccelerationX())
                .withY(bandAccelerometerEvent.getAccelerationY())
                .withZ(bandAccelerometerEvent.getAccelerationZ())
                .build();

        Log.e("ACCELEROMETER", accelerometer.toString());

        long curTime = bandAccelerometerEvent.getTimestamp();

        Gesture currentGesture = RecognitionEngine.identifyGesture(accelerometer);

        if ((curTime - lastUpdate) > 100 && lastGesture != null && lastGesture instanceof StationaryHandShakeLeftHand) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float speed = Math.abs(bandAccelerometerEvent.getAccelerationY() - last_y) / diffTime * 10000;

            Log.e("SPEED", speed + " ");
            if (speed > HandShake.SHAKE_THRESHOLD && !Float.isInfinite(speed)) {
                Log.e("GOOOOOD", "shake");
            }

            last_y = bandAccelerometerEvent.getAccelerationY();
        }
        lastGesture = currentGesture;
        Log.e("GESTURE", lastGesture != null ? lastGesture.toString() : "NULL" + accelerometer.toString());
    }
}
