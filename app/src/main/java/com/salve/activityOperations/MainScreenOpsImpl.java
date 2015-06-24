package com.salve.activityOperations;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Vlad on 6/24/2015.
 */
public class MainScreenOpsImpl implements IMainScreenOps {

    @Override
    public void goToActivity(Activity currentActivity, Class<? extends Activity> nextActivityClass) {
        Intent intent = new Intent(currentActivity, nextActivityClass);
        currentActivity.startActivity(intent);
    }
}
