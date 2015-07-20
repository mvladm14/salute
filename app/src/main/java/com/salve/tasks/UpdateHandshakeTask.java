package com.salve.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.salve.activities.HandShake;

/**
 * Created by Vlad on 7/20/2015.
 */
public class UpdateHandshakeTask extends AsyncTask<Void,Void,Void> {

    private HandShake handShake;

    public UpdateHandshakeTask(HandShake handShake) {
        this.handShake = handShake;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        handShake.onGestureLearned();
    }
}
