package com.salve.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.microsoft.band.ConnectionState;
import com.salve.R;
import com.salve.activities.navigation.NavigationManager;
import com.salve.activities.operations.MainScreenOpsImpl;
import com.salve.band.tasks.BandConnectionAsyncResponseImpl;

public class MainScreen extends AppCompatActivity {

    private static final String TAG = "MainScreen";

    private MainScreenOpsImpl screenOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        ConnectionState connectionState = null;

        Log.e(TAG, "onCreate() called");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            connectionState = (ConnectionState) extras.getSerializable(BandConnectionAsyncResponseImpl.BAND_CONNECTION_STATUS);
            Log.e(TAG, "Band ConnectionState is: " + connectionState + "");
        }

        screenOps = new MainScreenOpsImpl(this, connectionState);

        initializeFields();
    }

    private void initializeFields() {
        initializeNonUIFields();
        initializeUIFields();
    }

    private void initializeUIFields() {
        screenOps.initializeUIFields();
    }

    private void initializeNonUIFields() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_preferences:
                NavigationManager.goToActivity(this, Preferences.class);
                break;
            case R.id.menu_testing:
                NavigationManager.goToActivity(this, TestingActivity.class);
                break;
            case R.id.menu_about:
                NavigationManager.goToActivity(this, About.class);
                break;
            case R.id.menu_handshake:
                NavigationManager.goToActivity(this, HandShake.class);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
