package com.salve.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.salve.R;
import com.salve.activities.operations.HandShakeOpsImpl;
import com.salve.preferences.SalvePreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HandShake extends AppCompatActivity {

    private HandShakeOpsImpl handShakeOps;

    private CheckBox myOwnGesture;
    private CheckBox defaultGesture;
    private TextView gestureLearnedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_shake);

        initializeFields();
    }

    private void initializeFields() {
        initializeNonUIFields();
        initializeUIFields();
    }

    private void initializeNonUIFields() {
        handShakeOps = new HandShakeOpsImpl(this);
    }

    private void initializeUIFields() {
        myOwnGesture = (CheckBox) findViewById(R.id.handshake_myOwnGesture);
        defaultGesture = (CheckBox) findViewById(R.id.handshake_defaultGesture);
        gestureLearnedTextView = (TextView) findViewById(R.id.handshake_gestureLearnedMessage);

        Context ctx = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        defaultGesture.setChecked(prefs.getBoolean(SalvePreferences.DEFAULT_GESTURE, true));
        myOwnGesture.setChecked(prefs.getBoolean(SalvePreferences.MY_OWN_GESTURE, false));

        myOwnGesture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    defaultGesture.setChecked(false);
                }
                Context ctx = getApplicationContext();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(SalvePreferences.MY_OWN_GESTURE, b);
                editor.apply();
            }
        });

        defaultGesture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    myOwnGesture.setChecked(false);
                }

                Context ctx = getApplicationContext();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(SalvePreferences.DEFAULT_GESTURE, b);
                editor.apply();
            }
        });
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

    @Override
    protected void onDestroy() {
        handShakeOps.unbindService();
        super.onDestroy();
    }

    public void defineHandshake(View view) {
        handShakeOps.defineHandShake();
    }

    public void onGestureLearned() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm");
        String currentDateandTime = sdf.format(new Date());

        gestureLearnedTextView.setText(String.format("%s", "Defined on " + currentDateandTime));
        myOwnGesture.setEnabled(true);
    }
}
