package com.salve.band.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.band.BandException;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;

public class BandConnectionTask extends AsyncTask<BandPendingResult<ConnectionState>, Void, ConnectionState> {

    private static final String TAG = "BandConnectionTask";
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
        Log.e(TAG, "Connection state is " + connectionState.toString());
        return connectionState;
    }

    @Override
    protected void onPostExecute(ConnectionState result) {
        delegate.onFinishedConnection(result);
    }
}
