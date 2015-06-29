package com.salve.activities.operations;

import android.app.Activity;

/**
 * Created by Vlad on 6/24/2015.
 */
public interface IActivityOperations {

    void goToActivity(Activity currentActivity, Class<? extends Activity> nextActivityClass);
}
