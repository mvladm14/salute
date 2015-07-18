package com.salve.activities.commands;

import android.os.RemoteException;
import android.util.Log;

import com.salve.agrf.gestures.IGestureRecognitionService;

/**
 * Created by Vlad on 7/18/2015.
 */
public class StartTrainingCommand extends AbstractGestureRecognitionSvcCommand {

    private static final String TAG = "StartTrainingCommand";

    public StartTrainingCommand(IGestureRecognitionService gestureRecognitionService) {
        super(gestureRecognitionService);
    }

    @Override
    public void execute() {
        Log.e(TAG, "executing...");
        try {
            this.mGestureRecognitionService.startLearnMode("handshake", "HANDSHAKE");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
