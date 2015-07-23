package com.salve.agrf.gestures.recorder;

import android.os.IBinder;

import java.util.List;

/**
 * Created by Vlad on 6/19/2015.
 */
public interface GestureRecorderListener {

    void onGestureRecorded(List<float[]> values);

    IBinder getBinder();

    void startClassification(String defaultGesture);
}