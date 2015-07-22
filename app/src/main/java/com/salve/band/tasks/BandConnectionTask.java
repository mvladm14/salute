package com.salve.band.tasks;

import android.os.AsyncTask;

import com.microsoft.band.BandException;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;

public class BandConnectionTask extends AsyncTask<BandPendingResult<ConnectionState>, Void, ConnectionState> {

    private IBandConnectionAsyncResponse delegate = null;

    public BandConnectionTask(IBandConnectionAsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected ConnectionState doInBackground(BandPendingResult<ConnectionState>... bandPendingResults) {
        ConnectionState connectionState = null;
        try {
            connectionState = bandPendingResults[0].await();
        } catch (InterruptedException | BandException e) {
            e.printStackTrace();
        }
        return connectionState;
    }

    @Override
    protected void onPostExecute(ConnectionState result) {
        delegate.onFinishedConnection(result);
    }
}
