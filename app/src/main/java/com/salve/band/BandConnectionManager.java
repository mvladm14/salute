package com.salve.band;

import android.app.Service;
import android.os.IBinder;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
import com.salve.band.tasks.BandConnectionAsyncResponseImpl;
import com.salve.band.tasks.BandConnectionTask;
import com.salve.band.tasks.IBandConnectionAsyncResponse;

/**
 * Created by Vlad on 7/22/2015.
 */
public class BandConnectionManager {

    private Service service;

    private BandClient bandClient;

    private IBandConnectionAsyncResponse bandConnectionAsyncResponse;

    public BandConnectionManager(Service service) {
        this.service = service;
    }

    public void connectToBand() {

        BandInfo[] pairedBands = BandClientManager.getInstance().getPairedBands();

        if (pairedBands.length > 0) {

            bandClient = BandClientManager.getInstance().create(service, pairedBands[0]);

            bandConnectionAsyncResponse = new BandConnectionAsyncResponseImpl(service, bandClient);

            connect();

        } else {

            bandConnectionAsyncResponse = new BandConnectionAsyncResponseImpl();

            bandConnectionAsyncResponse.onFinishedConnection(ConnectionState.UNBOUND);

        }
    }

    private void connect() {
        BandPendingResult<ConnectionState> pendingResult = bandClient.connect();
        BandConnectionTask connectionTask = new BandConnectionTask(bandConnectionAsyncResponse);
        connectionTask.execute(pendingResult);
    }

    public void unregisterListnere() {
        if (bandConnectionAsyncResponse != null) {
            bandConnectionAsyncResponse.unregisterListener();
        }
    }

    public void unregisterStopReceiver() {
        if (bandConnectionAsyncResponse != null) {
            bandConnectionAsyncResponse.unregisterStopReceiver();
        }
    }

    public IBinder getBinder() {
        return bandConnectionAsyncResponse.getBinder();
    }
}
