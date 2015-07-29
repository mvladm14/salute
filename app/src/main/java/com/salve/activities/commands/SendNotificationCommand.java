package com.salve.activities.commands;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.salve.R;
import com.salve.broadcastReceivers.StopServiceReceiver;

/**
 * Created by Vlad on 7/20/2015.
 */
public class SendNotificationCommand implements Command {

    public static final int NOTIFICATION_ID = 24440;

    private static final String TAG = "SendNotificationCommand";
    private Service mService;

    public SendNotificationCommand(Service service) {
        this.mService = service;
    }

    @Override
    public void execute() {
        Log.e(TAG, "executing");

        PendingIntent stopServiceIntent = PendingIntent.getBroadcast(mService,
                0,
                new Intent(StopServiceReceiver.RECEIVER_FILTER),
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager mNotificationManager =
                (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);

        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mService)
                        .setSmallIcon(R.drawable.handshake)
                        .setContentTitle("Salve")
                        .setContentText(mService.getResources().getString(R.string.notification_message))
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setPriority(Notification.PRIORITY_MAX)
                        .addAction(R.drawable.stop_icon,
                                mService.getResources().getString(R.string.notification_stopService),
                                stopServiceIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
