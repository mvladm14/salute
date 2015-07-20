package com.salve.activities.operations;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.salve.R;
import com.salve.activities.HandShake;
import com.salve.activities.asyncReplies.IGestureConnectionServiceAsyncReply;
import com.salve.activities.commands.Command;
import com.salve.activities.commands.CommandFactory;
import com.salve.activities.commands.CommandType;
import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.GestureRecognitionService;
import com.salve.agrf.gestures.IGestureRecognitionListener;
import com.salve.agrf.gestures.IGestureRecognitionService;
import com.salve.agrf.gestures.classifier.Distribution;
import com.salve.tasks.UpdateHandshakeTask;

/**
 * Created by Vlad on 7/18/2015.
 */
public class HandShakeOpsImpl implements IGestureConnectionServiceAsyncReply {

    private static final String TAG = "HandShakeOpsImpl";
    private final String activeTrainingSet = "handshake";

    private IBinder gestureListenerStub;
    private GestureConnectionService gestureConnectionService;
    private Activity mActivity;

    public HandShakeOpsImpl(Activity activity) {

        this.mActivity = activity;

        gestureConnectionService = new GestureConnectionService(this);

        gestureListenerStub = new IGestureRecognitionListener.Stub() {

            @Override
            public void onGestureLearned(String gestureName) throws RemoteException {
                Log.e(TAG, String.format("%s learned", gestureName));
                new UpdateHandshakeTask((HandShake)mActivity).execute();
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

        Intent bindIntent = new Intent(mActivity, GestureRecognitionService.class);
        mActivity.bindService(bindIntent, gestureConnectionService, Context.BIND_AUTO_CREATE);
    }

    public void defineHandShake() {

        final AlertDialog d = createAlertDialog(mActivity);

        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                final Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        IGestureRecognitionService recognitionService = gestureConnectionService.getRecognitionService();
                        Command command;

                        if (b.getText() == mActivity.getResources().getString(R.string.handshake_alertDialog_start)) {
                            command = CommandFactory.create(CommandType.StartTraining, recognitionService);
                            b.setText(mActivity.getResources().getString(R.string.handshake_alertDialog_stop));
                        } else {
                            command = CommandFactory.create(CommandType.StopTraining, recognitionService);
                            b.setText(mActivity.getResources().getString(R.string.handshake_alertDialog_start));
                        }

                        command.execute();
                    }
                });
            }
        });
        d.show();
    }

    private AlertDialog createAlertDialog(Activity activity) {
        String allertDialogMessage =
                String.format("%s\n\n%s\n%s\n%s",
                        activity.getString(R.string.handshake_alertDialog_hint),
                        activity.getString(R.string.handshake_alertDialog_instruction1),
                        activity.getString(R.string.handshake_alertDialog_instruction2),
                        activity.getString(R.string.handshake_alertDialog_instruction3));

        return new AlertDialog.Builder(activity)
                .setTitle(R.string.handshake_alertDialog_title)
                .setMessage(allertDialogMessage)
                .setPositiveButton(R.string.handshake_alertDialog_start, null)
                .setNegativeButton(R.string.handshake_alertDialog_cancel, null)
                .setIcon(R.drawable.handshake)
                .create();
    }

    @Override
    public void onServiceConnected() {
        Log.e(TAG, "Connected to the background service");
        try {
            gestureConnectionService
                    .getRecognitionService()
                    .startClassificationMode(activeTrainingSet);
            Log.e(TAG, "Starting classification");
            gestureConnectionService
                    .getRecognitionService()
                    .registerListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
            Log.e(TAG, "Listener registered");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected() {

    }

    public void unbindService() {
        mActivity.unbindService(gestureConnectionService);
    }
}
