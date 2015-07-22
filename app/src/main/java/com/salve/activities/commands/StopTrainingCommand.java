package com.salve.activities.commands;

import android.os.RemoteException;
import android.util.Log;

import com.salve.agrf.gestures.IGestureRecognitionService;
import com.salve.preferences.SalvePreferences;

/**
 * Created by Vlad on 7/18/2015.
 */
public class StopTrainingCommand extends AbstractGestureRecognitionSvcCommand {

    private final static String TAG = "StopTrainingCommand";

    public StopTrainingCommand(IGestureRecognitionService gestureRecognitionService) {
        super(gestureRecognitionService);
    }

    @Override
    public void execute() {
        Log.e(TAG, "executing...");
        try {
            this.mGestureRecognitionService.stopLearnMode();
            this.mGestureRecognitionService.stopClassificationMode();
            this.mGestureRecognitionService.startClassificationMode(SalvePreferences.DEFAULT_GESTURE);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
