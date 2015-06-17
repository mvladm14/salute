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
    private long lastUpdate;
    private float last_x;
    private float last_y;
    private float last_z;

    @Override
    public void onBandAccelerometerChanged(BandAccelerometerEvent bandAccelerometerEvent) {

        Accelerometer accelerometer = new Accelerometer.AccelerometerBuilder()
                .withX(bandAccelerometerEvent.getAccelerationX())
                .withY(bandAccelerometerEvent.getAccelerationY())
                .withZ(bandAccelerometerEvent.getAccelerationZ())
                .build();

        Log.e("ACCELEROMETER", accelerometer.toString());

        long curTime = bandAccelerometerEvent.getTimestamp();

        Gesture gesture = RecognitionEngine.identifyGesture(accelerometer);

        if ((curTime - lastUpdate) > 100 && gesture != null && gesture instanceof StationaryHandShakeLeftHand) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float speed = Math.abs(bandAccelerometerEvent.getAccelerationX() +
                    bandAccelerometerEvent.getAccelerationY() +
                    bandAccelerometerEvent.getAccelerationZ() -
                    last_x -
                    last_y -
                    last_z) /
                    diffTime * 10000;

            Log.e("SPEED", speed + " ");
            if (speed > HandShake.SHAKE_THRESHOLD) {
                Log.e("GOOOOOD", "shake");
            }

            last_x = bandAccelerometerEvent.getAccelerationX();
            last_y = bandAccelerometerEvent.getAccelerationY();
            last_z = bandAccelerometerEvent.getAccelerationZ();
        }
    }
}
