package com.salve.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.salve.R;
import com.salve.activities.operations.LoadingScreenOpsImpl;

public class LoadingScreen extends Activity {

    private static final String TAG = "LoadingScreen";
    private LoadingScreenOpsImpl screenOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        initializeNonUIFields();

        screenOps.registerReceiver();
    }

    private void initializeNonUIFields() {
        screenOps = new LoadingScreenOpsImpl(this);
        screenOps.LoadApplication();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy() called.");
        screenOps.unregisterReceiver();
        super.onDestroy();
    }
}
