package com.salve.tasks;

import android.os.AsyncTask;

import com.salve.fragments.HandShakeFragment;

public class UpdateHandshakeTask extends AsyncTask<Void,Void,Void> {

    private HandShakeFragment handShakeFragment;

    public UpdateHandshakeTask(HandShakeFragment handShakeFragment) {
        this.handShakeFragment = handShakeFragment;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        handShakeFragment.onGestureLearned();
    }
}
