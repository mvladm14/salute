package com.salve.activities.operations.listeners.handshake;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Button;

import com.salve.agrf.gestures.GestureConnectionService;

/**
 * Created by Vlad on 7/22/2015.
 */
public class AlertDialogOnShowListener implements DialogInterface.OnShowListener {

    private AlertDialog alertDialog;
    private GestureConnectionService gestureConnectionService;
    private Activity activity;

    public AlertDialogOnShowListener(AlertDialog alertDialog, GestureConnectionService gestureConnectionService, Activity activity) {
        this.alertDialog = alertDialog;
        this.gestureConnectionService = gestureConnectionService;
        this.activity = activity;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new TrainButtonOnClickListener(activity, b, gestureConnectionService));
    }
}
