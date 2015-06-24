package com.salve.agrf.gestures.recorder;

import android.content.Context;
import android.util.Log;

import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.salve.band.sensors.models.SensorAxes;
import com.salve.band.sensors.registration.SensorRegistrationManager;

import java.util.ArrayList;

/**
 * Created by Vlad on 6/19/2015.
 */
public class GestureRecorder implements BandAccelerometerEventListener {

    private final String TAG = "GestureRecorder";

    private SensorRegistrationManager sensorRegistrationManager;

    public enum RecordMode {
        MOTION_DETECTION, PUSH_TO_GESTURE
    }

    final int MIN_GESTURE_SIZE = 8;
    float THRESHOLD = 0.4f;
    boolean isRecording;

    int stepsSinceNoMovement;
    ArrayList<float[]> gestureValues;
    Context context;
    GestureRecorderListener listener;
    boolean isRunning;
    RecordMode recordMode = RecordMode.MOTION_DETECTION;

    public GestureRecorder(SensorRegistrationManager sensorRegistrationManager, Context context) {
        this.context = context;
        this.sensorRegistrationManager = sensorRegistrationManager;
    }

    private float calcVectorNorm(float[] values) {
        float norm = (float) Math.sqrt(
                values[SensorAxes.DATA_X.getValue()] * values[SensorAxes.DATA_X.getValue()] +
                        values[SensorAxes.DATA_Y.getValue()] * values[SensorAxes.DATA_Y.getValue()] +
                        values[SensorAxes.DATA_Z.getValue()] * values[SensorAxes.DATA_Z.getValue()]) - 1f;
        return norm;
    }

    public RecordMode getRecordMode() {
        return recordMode;
    }

    public void setThreshold(float threshold) {
        THRESHOLD = threshold;
        Log.e(TAG, "New Threshold " + threshold);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void onPushToGesture(boolean pushed) {
        Log.e(TAG, "onPushToGesture() called");
        if (recordMode == RecordMode.PUSH_TO_GESTURE) {
            isRecording = pushed;
            if (isRecording) {
                gestureValues = new ArrayList<float[]>();
            } else {
                if (gestureValues.size() > MIN_GESTURE_SIZE) {
                    listener.onGestureRecorded(gestureValues);
                }
                gestureValues = null;
            }
        }
    }


    @Override
    public void onBandAccelerometerChanged(BandAccelerometerEvent bandAccelerometerEvent) {

        float[] value = {
                bandAccelerometerEvent.getAccelerationX(),
                bandAccelerometerEvent.getAccelerationY(),
                bandAccelerometerEvent.getAccelerationZ()
        };

        //Log.e(TAG, String.format("x = %f y = %f z = %f", value[0], value[1], value[2]));

        switch (recordMode) {
            case MOTION_DETECTION:
                Log.d(TAG, calcVectorNorm(value) + " " + THRESHOLD);
                if (isRecording) {
                    gestureValues.add(value);
                    if (calcVectorNorm(value) < THRESHOLD) {
                        stepsSinceNoMovement++;
                    } else {
                        stepsSinceNoMovement = 0;
                    }
                } else if (calcVectorNorm(value) >= THRESHOLD) {
                    isRecording = true;
                    stepsSinceNoMovement = 0;
                    gestureValues = new ArrayList<float[]>();
                    gestureValues.add(value);
                }
                if (stepsSinceNoMovement == 10) {

                    Log.e(TAG, "Length is: " + String.valueOf(gestureValues.size() - 10));
                    if (gestureValues.size() - 10 > MIN_GESTURE_SIZE) {
                        listener.onGestureRecorded(gestureValues.subList(0, gestureValues.size() - 10));
                    }
                    gestureValues = null;
                    stepsSinceNoMovement = 0;
                    isRecording = false;
                }
                break;
            case PUSH_TO_GESTURE:
                if (isRecording) {
                    gestureValues.add(value);
                }
                break;
        }
    }

    public void registerListener(GestureRecorderListener listener) {
        this.listener = listener;
        start();
    }

    public void setRecordMode(RecordMode recordMode) {
        this.recordMode = recordMode;
    }

    public void start() {
        sensorRegistrationManager.registerAccelerometerListener(this);
        isRunning = true;
    }

    public void stop() {
        sensorRegistrationManager.unregisterAccelerometerListener(this);
        isRunning = false;
    }

    public void unregisterListener(GestureRecorderListener listener) {
        this.listener = null;
        stop();
    }

    public void pause(boolean b) {
        if (b) {
            sensorRegistrationManager.unregisterAccelerometerListener(this);
        } else {
            sensorRegistrationManager.registerAccelerometerListener(this);
        }
    }

}
