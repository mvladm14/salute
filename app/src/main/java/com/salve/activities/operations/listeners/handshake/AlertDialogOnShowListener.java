package com.salve.activities.operations.listeners.handshake;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;

public class AlertDialogOnShowListener implements DialogInterface.OnShowListener {

    private AlertDialog alertDialog;
    private Activity activity;

    public AlertDialogOnShowListener(AlertDialog alertDialog, Activity activity) {
        this.alertDialog = alertDialog;
        this.activity = activity;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new TrainButtonOnClickListener(activity, b));
    }
}
