package com.salve.activities.commands;

import com.salve.agrf.gestures.IGestureRecognitionService;

/**
 * Created by Vlad on 7/18/2015.
 */
public abstract class AbstractGestureRecognitionSvcCommand implements Command {

    protected IGestureRecognitionService mGestureRecognitionService;

    public AbstractGestureRecognitionSvcCommand(IGestureRecognitionService gestureRecognitionService) {
        this.mGestureRecognitionService = gestureRecognitionService;
    }
}
