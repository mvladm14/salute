package com.salve.band.tasks;

import android.os.IBinder;

import com.microsoft.band.ConnectionState;

/**
 * Created by Vlad on 6/16/2015.
 */
public interface IBandConnectionAsyncResponse {

    void onFinishedConnection(ConnectionState connectionState);

    void unregisterListener();

    IBinder getBinder();
}
