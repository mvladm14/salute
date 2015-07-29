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
import com.salve.band.tasks.BandDisconnectTask;
import com.salve.band.tasks.BandDisconnectionAsyncResponseImpl;
import com.salve.band.tasks.IBandConnectionAsyncResponse;
import com.salve.band.tasks.IBandDisconnectionAsyncResponse;

public class BandConnectionManager {

    private Service service;

    private BandClient bandClient;

    private IBandConnectionAsyncResponse bandConnectionAsyncResponse;
    private IBandDisconnectionAsyncResponse bandDisconnectionAsyncResponse;

    public BandConnectionManager(Service service) {
        this.service = service;
    }

    public void connectToBand() {

        BandInfo[] pairedBands = BandClientManager.getInstance().getPairedBands();

        if (pairedBands.length > 0) {

            bandClient = BandClientManager.getInstance().create(service, pairedBands[0]);

            bandConnectionAsyncResponse = new BandConnectionAsyncResponseImpl(service, bandClient);
            bandDisconnectionAsyncResponse = new BandDisconnectionAsyncResponseImpl(service, bandClient);

            connect();

        } else {

            bandConnectionAsyncResponse = new BandConnectionAsyncResponseImpl(service);

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

    public IBinder getBinder() {
        return bandConnectionAsyncResponse.getBinder();
    }

    public BandClient getBandClient() {
        return bandClient;
    }

    public void disconnectFromBand() {
        BandPendingResult<Void> pendingResult = bandClient.disconnect();
        BandDisconnectTask disconnectionTask = new BandDisconnectTask(bandDisconnectionAsyncResponse);
        disconnectionTask.execute(pendingResult);
    }
}
