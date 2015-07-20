package com.salve.activities.operations;

import com.salve.activities.receivers.GestureRecognitionServiceReceiver;

/**
 * Created by Vlad on 6/24/2015.
 */
public interface ILoadingScreenOps extends IActivityOperations {

    void LoadApplication();

    void registerReceiver(GestureRecognitionServiceReceiver receiver);

    void unregisterReceiver(GestureRecognitionServiceReceiver receiver);
}
