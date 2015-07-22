package com.salve.activities.commands;

import android.os.RemoteException;
import android.util.Log;

import com.salve.agrf.gestures.IGestureRecognitionService;
import com.salve.preferences.SalvePreferences;

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
            this.mGestureRecognitionService.stopClassificationMode();
            this.mGestureRecognitionService.startClassificationMode(SalvePreferences.MY_OWN_GESTURE);
            this.mGestureRecognitionService.startLearnMode(SalvePreferences.MY_OWN_GESTURE, SalvePreferences.MY_OWN_GESTURE);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
