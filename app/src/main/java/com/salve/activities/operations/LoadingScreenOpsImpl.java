package com.salve.activities.operations;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;

import com.salve.activities.LoadingScreen;
import com.salve.activities.receivers.GestureRecognitionServiceReceiver;
import com.salve.agrf.gestures.GestureRecognitionService;

/**
 * Created by Vlad on 6/24/2015.
 */
public class LoadingScreenOpsImpl implements ILoadingScreenOps {

    private static final String TAG = "LoadingScreenOpsImpl";
    private LoadingScreen mActivity;

    public LoadingScreenOpsImpl(final LoadingScreen mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void LoadApplication() {
        Intent bindIntent = new Intent(mActivity, GestureRecognitionService.class);
        mActivity.startService(bindIntent);
    }

    @Override
    public void registerReceiver(GestureRecognitionServiceReceiver receiver) {
        IntentFilter filter = new IntentFilter(GestureRecognitionServiceReceiver.PROCESS_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new GestureRecognitionServiceReceiver();
        mActivity.registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(GestureRecognitionServiceReceiver receiver) {
        if (receiver != null) mActivity.unregisterReceiver(receiver);
    }


    @Override
    public void goToActivity(Activity currentActivity, Class<? extends Activity> nextActivityClass) {
        Intent intent = new Intent(currentActivity, nextActivityClass);
        currentActivity.startActivity(intent);
    }
}
