package com.salve.activities.operations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.GestureRecognitionService;
import com.salve.agrf.gestures.IGestureRecognitionListener;
import com.salve.agrf.gestures.classifier.Distribution;

/**
 * Created by Vlad on 6/24/2015.
 */
public class LoadingScreenOpsImpl implements ILoadingScreenOps {

    private static final String TAG = "LoadingScreenOpsImpl";
    public static IBinder gestureListenerStub;
    public static GestureConnectionService gestureConnectionService;

    private Activity mActivity;

    public LoadingScreenOpsImpl(Activity activity) {
       this.mActivity = activity;
    }

    @Override
    public void LoadApplication() {
        gestureListenerStub = new IGestureRecognitionListener.Stub() {

            @Override
            public void onGestureLearned(String gestureName) throws RemoteException {
                Log.e(TAG, String.format("%s learned", gestureName));
            }

            @Override
            public void onTrainingSetDeleted(String trainingSet) throws RemoteException {
                Log.e(TAG, String.format("Training set %s deleted", trainingSet));
            }

            @Override
            public void onGestureRecognized(final Distribution distribution) throws RemoteException {
                Log.e(TAG, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
            }
        };

        gestureConnectionService = new GestureConnectionService();
        Intent bindIntent = new Intent(mActivity, GestureRecognitionService.class);
        mActivity.bindService(bindIntent, gestureConnectionService, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void goToActivity(Activity currentActivity, Class<? extends Activity> nextActivityClass) {
        Intent intent = new Intent(currentActivity, nextActivityClass);
        currentActivity.startActivity(intent);
    }
}
