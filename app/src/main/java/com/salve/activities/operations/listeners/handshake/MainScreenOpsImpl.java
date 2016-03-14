package com.salve.activities.operations.listeners.handshake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.salve.R;
import com.salve.fragments.HandShakeFragment;
import com.salve.activities.asyncReplies.IGestureConnectionServiceAsyncReply;
import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.GestureRecognitionService;
import com.salve.agrf.gestures.IGestureRecognitionListener;
import com.salve.agrf.gestures.classifier.Distribution;
import com.salve.contacts.ContactInformation;
import com.salve.fragments.HomeFragment;
import com.salve.listeners.fragment.OnHandShakeFragmentListener;
import com.salve.preferences.SalvePreferences;
import com.salve.tasks.UpdateHandshakeTask;

import java.util.List;

public class MainScreenOpsImpl implements IGestureConnectionServiceAsyncReply, OnHandShakeFragmentListener {

    private static final String TAG = "MainScreenOpsImpl";

    private Activity mActivity;

    private List<ContactInformation> contacts;

    private GestureConnectionService gestureConnectionService;

    private IBinder gestureListenerStub;

    public MainScreenOpsImpl(Activity activity) {
        this.mActivity = activity;
        gestureConnectionService = new GestureConnectionService(this);

        Intent bindIntent = new Intent(mActivity, GestureRecognitionService.class);
        mActivity.bindService(bindIntent, gestureConnectionService, Context.BIND_AUTO_CREATE);
        gestureListenerStub = new IGestureRecognitionListener.Stub() {

            @Override
            public void onGestureLearned(String gestureName) throws RemoteException {
                Log.e(TAG, String.format("%s learned", gestureName));

                HandShakeFragment hsf = (HandShakeFragment) mActivity.getFragmentManager().findFragmentById(R.id.frame_container);
                new UpdateHandshakeTask(hsf).execute();

                Context ctx = mActivity.getApplicationContext();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                if (prefs.getBoolean(SalvePreferences.DEFAULT_GESTURE, true)) {
                    restartClassification(SalvePreferences.DEFAULT_GESTURE);
                } else if (prefs.getBoolean(SalvePreferences.MY_OWN_GESTURE, true)) {
                    restartClassification(prefs.getString(SalvePreferences.MY_OWN_GESTURE, SalvePreferences.MY_OWN_GESTURE));
                }
            }

            @Override
            public void onTrainingSetDeleted(String trainingSet) throws RemoteException {
                Log.e(TAG, String.format("Training set %s deleted", trainingSet));

                Context ctx = mActivity.getApplicationContext();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(SalvePreferences.OWN_GESTURE_DEFINE_DATE, "");
                editor.apply();
            }

            @Override
            public void onGestureRecognized(final Distribution distribution) throws RemoteException {
                Log.e(TAG, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
            }
        };
    }

    @Override
    public void onServiceConnected() {
        Log.e(TAG, "Connected to the background service");
        try {
            gestureConnectionService
                    .getRecognitionService()
                    .registerListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));

            contacts = gestureConnectionService
                    .getRecognitionService()
                    .getContacts();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

//        for (int i = 0; i < 5; i++) {
//            ContactInformation cf = new ContactInformation();
//            cf.setName("Lodewijck " + i);
//            contacts.add(cf);
//        }
        HomeFragment homeFragment = (HomeFragment) mActivity.getFragmentManager().findFragmentById(R.id.frame_container);
        homeFragment.updateContacts(contacts);
    }

    @Override
    public void onServiceDisconnected() {
        Log.e(TAG, "Disconnected to the background service");
    }

    @Override
    public void restartClassification(String activeTrainingSet) {
        Log.e(TAG, "Restarting classification with new training set: " + activeTrainingSet);
        try {
            gestureConnectionService
                    .getRecognitionService()
                    .stopClassificationMode();
            gestureConnectionService
                    .getRecognitionService()
                    .startClassificationMode(activeTrainingSet);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteGestureData() {
        try {
            gestureConnectionService
                    .getRecognitionService()
                    .deleteTrainingSet(SalvePreferences.MY_OWN_GESTURE);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public GestureConnectionService getGestureConnectionService() {
        return gestureConnectionService;
    }

    public void unbindService() {
        mActivity.unbindService(gestureConnectionService);
    }
}
