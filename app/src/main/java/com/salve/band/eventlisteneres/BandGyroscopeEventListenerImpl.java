package com.salve.band.eventlisteneres;

import android.util.Log;

import com.microsoft.band.sensors.BandGyroscopeEvent;
import com.microsoft.band.sensors.BandGyroscopeEventListener;

/**
 * Created by Vlad on 6/17/2015.
 */
public class BandGyroscopeEventListenerImpl implements BandGyroscopeEventListener {
    @Override
    public void onBandGyroscopeChanged(BandGyroscopeEvent bandGyroscopeEvent) {
        Log.e("GYROSCOPE", bandGyroscopeEvent.getAngularVelocityX() + "");
    }
}
