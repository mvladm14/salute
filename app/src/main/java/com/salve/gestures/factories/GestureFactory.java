package com.salve.gestures.factories;

import com.salve.gestures.models.Gesture;
import com.salve.gestures.models.StationaryHandShakeLeftHand;
import com.salve.gestures.types.GestureType;

/**
 * Created by Vlad on 6/17/2015.
 */
public class GestureFactory {

    public static Gesture create(GestureType gestureType) {
        switch (gestureType) {
            case StationaryHandShakeLeftHand:
                return new StationaryHandShakeLeftHand();
            default:
                return null;
        }
    }
}
