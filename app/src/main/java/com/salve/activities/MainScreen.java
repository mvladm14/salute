package com.salve.activities;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.microsoft.band.ConnectionState;
import com.salve.R;
import com.salve.activities.adapters.ContactsListViewAdapter;
import com.salve.activities.operations.IMainScreenOps;
import com.salve.activities.operations.MainScreenOpsImpl;
import com.salve.agrf.gestures.GestureRecognitionService;

public class MainScreen extends AppCompatActivity {

    private static final String TAG = "MainScreen";

    private IMainScreenOps mainScreenOps;
    private ConnectionState connectionState;

    private TextView mainScreenStatusMessage;
    private TextView mainScreenInformationMessage;

    private ListView mContactsListView;
    private ContactsListViewAdapter mContactsListAdapter;

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
        View view = findViewById(R.id.circle);
        GradientDrawable bgDrawable = (GradientDrawable) view.getBackground();
        bgDrawable.setColor(connectionState == ConnectionState.CONNECTED ? Color.GREEN : Color.BLACK);

        mainScreenStatusMessage = (TextView) findViewById(R.id.mainScreenStatusMessage);
        mainScreenStatusMessage.setText(connectionState == ConnectionState.CONNECTED ?
                getString(R.string.wearableIsConnected) : getString(R.string.wearableIsNotConnected));

        mainScreenInformationMessage = (TextView) findViewById(R.id.mainScreenInformationMessage);
        mainScreenInformationMessage.setText(connectionState == ConnectionState.CONNECTED ?
                getString(R.string.mainScreenInfoMessageOnBandConnected) :
                getString(R.string.mainScreenInfoMessageOnBandNotConnected));

        mContactsListView = (ListView) findViewById(R.id.contacts_list_view);
        mContactsListAdapter = new ContactsListViewAdapter(this);
        mContactsListView.setAdapter(mContactsListAdapter);
        mContactsListAdapter.setMode(Attributes.Mode.Single);
    }

    private void initializeNonUIFields() {
        mainScreenOps = new MainScreenOpsImpl();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_preferences:
                mainScreenOps.goToActivity(this, Preferences.class);
                break;
            case R.id.menu_testing:
                mainScreenOps.goToActivity(this, TestingActivity.class);
                break;
            case R.id.menu_about:
                mainScreenOps.goToActivity(this, About.class);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
