package com.salve.bluetooth;

/**
 * Created by Vlad on 7/26/2015.
 */
public enum ConnectionStateEnum {

    // Constants that indicate the current connection state
    STATE_NONE,       // we're doing nothing
    STATE_LISTEN,     // now listening for incoming connections
    STATE_CONNECTING, // now initiating an outgoing connection
    STATE_CONNECTED   // now connected to a remote device
}
