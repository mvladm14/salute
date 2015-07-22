package com.salve.agrf.gestures.recorder;

import java.util.List;

/**
 * Created by Vlad on 6/19/2015.
 */
public interface GestureRecorderListener {

    void onGestureRecorded(List<float[]> values);

}