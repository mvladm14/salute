package com.salve.activities;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.microsoft.band.ConnectionState;
import com.salve.R;
import com.salve.activities.operations.IMainScreenOps;
import com.salve.activities.operations.MainScreenOpsImpl;
import com.salve.agrf.gestures.GestureRecognitionService;

public class MainScreen extends AppCompatActivity {

    private IMainScreenOps mainScreenOps;
    private ConnectionState connectionState;

    private TextView mainScreenStatusMessage;
    private TextView mainScreenInformationMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            connectionState = (ConnectionState) extras.getSerializable(GestureRecognitionService.BAND_CONNECTION_STATUS);
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
                "Wearable is connected." : "Wearable is not connected");

        mainScreenInformationMessage = (TextView) findViewById(R.id.mainScreenInformationMessage);
        mainScreenInformationMessage.setText(connectionState == ConnectionState.CONNECTED ?
                "You can shake hands with your friends and we will help you stay in touch." :
                "In order to keep in touch with your friends, please make sure a connection is established between your wearable and your phone.");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
