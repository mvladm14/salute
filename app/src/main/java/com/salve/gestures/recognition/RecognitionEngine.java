package com.salve.gestures.recognition;

import com.salve.band.sensors.models.Accelerometer;
import com.salve.gestures.factories.GestureFactory;
import com.salve.gestures.models.Gesture;
import com.salve.gestures.types.GestureType;

/**
 * Created by Vlad on 6/17/2015.
 */
public class RecognitionEngine {

    public static Gesture identifyGesture(Accelerometer accelerometer) {
        Gesture gesture = null;
        if (accelerometer.getY() > 0.8f) {
            gesture = GestureFactory.create(GestureType.StationaryHandShakeLeftHand);
        }
        return gesture;
    }
}
