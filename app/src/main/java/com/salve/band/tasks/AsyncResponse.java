package com.salve.band.tasks;

import com.microsoft.band.ConnectionState;

/**
 * Created by Vlad on 6/16/2015.
 */
public interface AsyncResponse {
    void onFinishedConnection(ConnectionState connectionState);
}
