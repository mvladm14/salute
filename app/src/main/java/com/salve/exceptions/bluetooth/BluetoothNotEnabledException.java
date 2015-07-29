package com.salve.exceptions.bluetooth;

public class BluetoothNotEnabledException extends Exception {

    private static final String MESSAGE = "Bluetooth is not enable. Please turn it on";

    public BluetoothNotEnabledException() {
        super(MESSAGE);
    }
}
