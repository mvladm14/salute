package com.salve.activities.operations;

import android.content.Intent;
import android.content.IntentFilter;

import com.salve.activities.LoadingScreen;
import com.salve.broadcastReceivers.GestureRecognitionServiceReceiver;
import com.salve.agrf.gestures.GestureRecognitionService;

/**
 * Created by Vlad on 6/24/2015.
 */
public class LoadingScreenOpsImpl {

    private static final String TAG = "LoadingScreenOpsImpl";
    private LoadingScreen mActivity;
    private GestureRecognitionServiceReceiver receiver;

    public LoadingScreenOpsImpl(final LoadingScreen mActivity) {
        this.mActivity = mActivity;
    }

    public void LoadApplication() {
        receiver = new GestureRecognitionServiceReceiver();
        Intent bindIntent = new Intent(mActivity, GestureRecognitionService.class);
        mActivity.startService(bindIntent);
    }

    public void registerReceiver() {
        IntentFilter filter = new IntentFilter(GestureRecognitionServiceReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);

        mActivity.registerReceiver(receiver, filter);
    }

    public void unregisterReceiver() {
        if (receiver != null) mActivity.unregisterReceiver(receiver);
    }
}
