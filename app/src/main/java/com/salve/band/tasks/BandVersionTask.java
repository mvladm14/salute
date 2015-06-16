package com.salve.band.tasks;

import android.os.AsyncTask;

import com.microsoft.band.BandException;
import com.microsoft.band.BandPendingResult;

/**
 * Created by Vlad on 6/16/2015.
 */
public class BandVersionTask extends AsyncTask<BandPendingResult<String>, Void, String> {

    private AsyncResponse delegate = null;

    public BandVersionTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(BandPendingResult<String>... bandPendingResults) {
        String result = null;
        try {
            result = bandPendingResults[0].await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BandException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.onFinishedVersion(result);
    }

}
