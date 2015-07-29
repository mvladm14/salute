package com.salve.broadcastReceivers;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandPendingResult;
import com.salve.activities.commands.SendNotificationCommand;
import com.salve.band.tasks.BandDisconnectTask;
import com.salve.band.tasks.IBandDisconnectionAsyncResponse;

public class StopServiceReceiver extends BroadcastReceiver implements IBandDisconnectionAsyncResponse {

    private static final String TAG = "StopServiceReceiver";

    public static final String RECEIVER_FILTER = "StopServiceReceiver";

    private Service mService;
    private BandClient mBandClient;

    public StopServiceReceiver(Service service, BandClient bandClient) {

        this.mService = service;
        this.mBandClient = bandClient;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive called. Service will be stopped");
        if (mBandClient != null) {
            BandPendingResult<Void> pendingResult = mBandClient.disconnect();
            BandDisconnectTask disconnectionTask = new BandDisconnectTask(this);
            disconnectionTask.execute(pendingResult);
        } else {
            onFinishedConnection(null);
        }
    }

    @Override
    public void onFinishedConnection(Void result) {

        Log.e(TAG, "cancelling notification");
        NotificationManager notificationManager = (NotificationManager)
                mService.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(SendNotificationCommand.NOTIFICATION_ID);

        Log.e(TAG, "service was stopped");
        mService.stopSelf();
    }
}
