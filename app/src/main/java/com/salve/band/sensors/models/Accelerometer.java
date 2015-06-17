package com.salve.band.sensors.models;

/**
 * Created by Vlad on 6/17/2015.
 */
public class Accelerometer {

    private float x;
    private float y;
    private float z;

    public static class AccelerometerBuilder {
        private float x;
        private float y;
        private float z;

        public AccelerometerBuilder withX(float x) {
            this.x = x;
            return this;
        }

        public AccelerometerBuilder withY(float y) {
            this.y = y;
            return this;
        }

        public AccelerometerBuilder withZ(float z) {
            this.z = z;
            return this;
        }

        public Accelerometer build() {
            return new Accelerometer(this);
        }
    }


    private Accelerometer(AccelerometerBuilder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.z = builder.z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "X = " + x + " Y = " + y + " Z = " + z;
    }
}
