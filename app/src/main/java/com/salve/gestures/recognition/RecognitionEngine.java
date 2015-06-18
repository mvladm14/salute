package com.salve.gestures.recognition;

import com.salve.band.sensors.models.Accelerometer;
import com.salve.gestures.factories.GestureFactory;
import com.salve.gestures.models.Gesture;
import com.salve.gestures.models.StationaryHandShakeLeftHand;
import com.salve.gestures.types.GestureType;

/**
 * Created by Vlad on 6/17/2015.
 */
public class RecognitionEngine {

    public static Gesture identifyGesture(Accelerometer accelerometer) {
        Gesture gesture = null;
        if (RecongnitionUtils.isInInterval(accelerometer.getY(), StationaryHandShakeLeftHand.INTERVAL_BEGIN_Y, StationaryHandShakeLeftHand.INTERVAL_END_Y) &&
            RecongnitionUtils.isInInterval(accelerometer.getX(), StationaryHandShakeLeftHand.INTERVAL_BEGIN_X, StationaryHandShakeLeftHand.INTERVAL_END_X) &&
            RecongnitionUtils.isInInterval(accelerometer.getZ(), StationaryHandShakeLeftHand.INTERVAL_BEGIN_Z, StationaryHandShakeLeftHand.INTERVAL_END_Z)) {

            gesture = GestureFactory.create(GestureType.StationaryHandShakeLeftHand);
        }
        return gesture;
    }


}
