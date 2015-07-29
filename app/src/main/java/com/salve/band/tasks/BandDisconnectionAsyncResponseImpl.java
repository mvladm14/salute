package com.salve.band.tasks;

import android.app.Service;

import com.microsoft.band.BandClient;

public class BandDisconnectionAsyncResponseImpl implements IBandDisconnectionAsyncResponse {

    private static final String TAG = "BandDisconnectionAsyncResponseImpl";

    private final Service service;
    private final BandClient bandClient;

    public BandDisconnectionAsyncResponseImpl(Service service, BandClient bandClient) {
        this.service = service;
        this.bandClient = bandClient;
    }


    @Override
    public void onFinishedConnection(Void result) {

    }
}
