package com.salve.gestures.recognition;

/**
 * Created by Vlad on 6/18/2015.
 */
public class RecongnitionUtils {

    public static boolean isInInterval(float value, float begin, float end) {
        return value >= begin && value <= end;
    }
}
