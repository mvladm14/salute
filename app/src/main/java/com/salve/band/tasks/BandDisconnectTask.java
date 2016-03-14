package com.salve.band.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.band.BandException;
import com.microsoft.band.BandPendingResult;

public class BandDisconnectTask extends AsyncTask<BandPendingResult<Void>, Void, Void> {

    private static final String TAG = "BandDisconnectTask";

    private IBandDisconnectionAsyncResponse delegate = null;

    public BandDisconnectTask(IBandDisconnectionAsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Void doInBackground(BandPendingResult<Void>... bandPendingResults) {
        try {
            bandPendingResults[0].await();
        } catch (InterruptedException | BandException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.e(TAG, "band was disconnected.");
        delegate.onFinishedConnection(result);
    }
}