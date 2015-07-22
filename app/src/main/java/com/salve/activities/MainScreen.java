package com.salve.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.microsoft.band.ConnectionState;
import com.salve.R;
import com.salve.activities.adapters.ContactsListViewAdapter;
import com.salve.activities.navigation.NavigationManager;
import com.salve.agrf.gestures.GestureRecognitionService;

public class MainScreen extends AppCompatActivity {

    private static final String TAG = "MainScreen";

    private ConnectionState connectionState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Log.e(TAG, "onCreate() called");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            connectionState = (ConnectionState) extras.getSerializable(GestureRecognitionService.BAND_CONNECTION_STATUS);
            Log.e(TAG, "Band ConnectionState is: " + connectionState + "");
        }

        initializeFields();
    }

    private void initializeFields() {
        initializeNonUIFields();
        initializeUIFields();
    }

    private void initializeUIFields() {

        TextView mainScreenStatusMessage = (TextView) findViewById(R.id.mainScreenStatusMessage);
        TextView mainScreenInformationMessage = (TextView) findViewById(R.id.mainScreenInformationMessage);
        ListView mContactsListView = (ListView) findViewById(R.id.contacts_list_view);

        if (connectionState == ConnectionState.CONNECTED) {
            mainScreenStatusMessage.setText(getString(R.string.wearableIsConnected));
            mainScreenInformationMessage.setText(getString(R.string.mainScreenInfoMessageOnBandConnected));
        } else {
            mainScreenStatusMessage.setText(getString(R.string.wearableIsNotConnected));
            mainScreenStatusMessage.setTextColor(Color.RED);
            mainScreenInformationMessage.setText(getString(R.string.mainScreenInfoMessageOnBandNotConnected));
        }

        ContactsListViewAdapter mContactsListAdapter = new ContactsListViewAdapter(this);
        mContactsListView.setAdapter(mContactsListAdapter);
        mContactsListAdapter.setMode(Attributes.Mode.Single);
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
