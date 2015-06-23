package com.salve.band.sensors.models;

/**
 * Created by Vlad on 6/19/2015.
 */
public enum SensorAxes {

    DATA_X(0),
    DATA_Y(1),
    DATA_Z(2);

    private final int value;

    private SensorAxes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
