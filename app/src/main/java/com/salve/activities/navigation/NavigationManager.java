package com.salve.activities.navigation;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Vlad on 7/22/2015.
 */
public class NavigationManager {

    public static void goToActivity(Activity currentActivity, Class<? extends Activity> nextActivityClass) {
        Intent intent = new Intent(currentActivity, nextActivityClass);
        currentActivity.startActivity(intent);
    }
}
